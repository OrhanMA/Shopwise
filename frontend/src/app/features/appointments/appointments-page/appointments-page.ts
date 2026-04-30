import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { forkJoin } from 'rxjs';
import { finalize } from 'rxjs/operators';
import {
  Appointment,
  AppointmentFilters,
  AppointmentPayload,
  AppointmentStatus,
  Customer,
  ServiceOffering,
} from '../../../core/models';
import { ShopwiseApiService } from '../../../core/shopwise-api.service';

@Component({
  selector: 'app-appointments-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatChipsModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatProgressBarModule,
    MatSelectModule,
    MatSnackBarModule,
  ],
  templateUrl: './appointments-page.html',
  styleUrl: './appointments-page.css',
})
export class AppointmentsPage implements OnInit {
  private readonly api = inject(ShopwiseApiService);
  private readonly formBuilder = inject(FormBuilder);
  private readonly snackBar = inject(MatSnackBar);

  protected readonly customers = signal<Customer[]>([]);
  protected readonly services = signal<ServiceOffering[]>([]);
  protected readonly appointments = signal<Appointment[]>([]);
  protected readonly isLoading = signal(true);
  protected readonly isSaving = signal(false);
  protected readonly activeActionId = signal<number | null>(null);
  protected readonly errorMessage = signal('');
  protected readonly statusOptions: AppointmentStatus[] = ['SCHEDULED', 'HONORED', 'CANCELLED'];

  protected readonly filterForm = this.formBuilder.group({
    date: [''],
    status: [''],
    customerId: [''],
  });

  protected readonly appointmentForm = this.formBuilder.nonNullable.group({
    customerId: [0, [Validators.required, Validators.min(1)]],
    serviceId: [0, [Validators.required, Validators.min(1)]],
    appointmentDate: ['', Validators.required],
    appointmentTime: ['', Validators.required],
    notes: [''],
  });

  ngOnInit(): void {
    this.loadReferencesAndAppointments();
  }

  protected applyFilters(): void {
    this.loadAppointments();
  }

  protected resetFilters(): void {
    this.filterForm.reset({
      date: '',
      status: '',
      customerId: '',
    });
    this.loadAppointments();
  }

  protected createAppointment(): void {
    if (this.appointmentForm.invalid) {
      this.appointmentForm.markAllAsTouched();
      return;
    }

    this.isSaving.set(true);
    this.errorMessage.set('');

    const request = this.appointmentForm.getRawValue();
    const payload: AppointmentPayload = {
      customerId: request.customerId,
      serviceId: request.serviceId,
      appointmentDate: request.appointmentDate,
      appointmentTime: request.appointmentTime,
      notes: request.notes.trim() || null,
    };

    this.api
      .createAppointment(payload)
      .pipe(finalize(() => this.isSaving.set(false)))
      .subscribe({
        next: (appointment) => {
          this.appointments.set(this.sortAppointments([appointment, ...this.appointments()]));
          this.appointmentForm.reset({
            customerId: 0,
            serviceId: 0,
            appointmentDate: '',
            appointmentTime: '',
            notes: '',
          });
          this.snackBar.open('Rendez-vous créé.', 'Fermer', { duration: 3000 });
        },
        error: () => {
          this.errorMessage.set(
            'Impossible de créer le rendez-vous. Vérifiez la date, l’heure et les sélections.',
          );
        },
      });
  }

  protected honorAppointment(appointment: Appointment): void {
    this.updateAppointmentStatus(appointment.id, 'honor');
  }

  protected cancelAppointment(appointment: Appointment): void {
    this.updateAppointmentStatus(appointment.id, 'cancel');
  }

  protected isActionDisabled(appointment: Appointment): boolean {
    return appointment.status !== 'SCHEDULED' || this.activeActionId() === appointment.id;
  }

  protected customerLabel(customer: Customer): string {
    return `${customer.firstName} ${customer.lastName}`;
  }

  protected formatDate(date: string): string {
    return new Intl.DateTimeFormat('fr-FR', {
      day: '2-digit',
      month: 'long',
      year: 'numeric',
    }).format(new Date(`${date}T12:00:00`));
  }

  protected formatTime(time: string): string {
    return time.slice(0, 5);
  }

  protected statusLabel(status: AppointmentStatus): string {
    if (status === 'HONORED') {
      return 'Honoré';
    }

    if (status === 'CANCELLED') {
      return 'Annulé';
    }

    return 'Planifié';
  }

  private loadReferencesAndAppointments(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    forkJoin({
      customers: this.api.getCustomers(),
      services: this.api.getServices(),
      appointments: this.api.getAppointments(),
    })
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: ({ customers, services, appointments }) => {
          this.customers.set(this.sortCustomers(customers));
          this.services.set(this.sortServices(services));
          this.appointments.set(this.sortAppointments(appointments));
        },
        error: () => {
          this.errorMessage.set(
            'Impossible de charger les rendez-vous. Vérifiez que le backend répond bien.',
          );
        },
      });
  }

  private loadAppointments(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.api
      .getAppointments(this.buildFilters())
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: (appointments) => this.appointments.set(this.sortAppointments(appointments)),
        error: () => {
          this.errorMessage.set('Impossible de filtrer les rendez-vous pour le moment.');
        },
      });
  }

  private buildFilters(): AppointmentFilters {
    const rawFilters = this.filterForm.getRawValue();

    return {
      date: rawFilters.date || null,
      status: (rawFilters.status as AppointmentStatus | '') || null,
      customerId: rawFilters.customerId ? Number(rawFilters.customerId) : null,
    };
  }

  private updateAppointmentStatus(id: number, action: 'honor' | 'cancel'): void {
    this.activeActionId.set(id);
    this.errorMessage.set('');

    const request$ = action === 'honor' ? this.api.honorAppointment(id) : this.api.cancelAppointment(id);

    request$
      .pipe(finalize(() => this.activeActionId.set(null)))
      .subscribe({
        next: (appointment) => {
          this.appointments.set(
            this.sortAppointments(
              this.appointments().map((item) => (item.id === appointment.id ? appointment : item)),
            ),
          );
          this.snackBar.open(
            action === 'honor'
              ? 'Rendez-vous marqué comme honoré. 100 points ont été attribués.'
              : 'Rendez-vous annulé.',
            'Fermer',
            { duration: 3200 },
          );
        },
        error: () => {
          this.errorMessage.set(
            action === 'honor'
              ? 'Impossible de marquer ce rendez-vous comme honoré.'
              : 'Impossible d’annuler ce rendez-vous.',
          );
        },
      });
  }

  private sortAppointments(appointments: Appointment[]): Appointment[] {
    return [...appointments].sort((left, right) => {
      const leftKey = `${left.appointmentDate}T${left.appointmentTime}`;
      const rightKey = `${right.appointmentDate}T${right.appointmentTime}`;
      return leftKey.localeCompare(rightKey);
    });
  }

  private sortCustomers(customers: Customer[]): Customer[] {
    return [...customers].sort((left, right) =>
      `${left.lastName} ${left.firstName}`.localeCompare(`${right.lastName} ${right.firstName}`, 'fr'),
    );
  }

  private sortServices(services: ServiceOffering[]): ServiceOffering[] {
    return [...services].sort((left, right) => left.name.localeCompare(right.name, 'fr'));
  }
}
