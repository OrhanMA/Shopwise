import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { finalize } from 'rxjs';
import { Customer, CustomerPayload } from '../../../core/models';
import { ShopwiseApiService } from '../../../core/shopwise-api.service';

@Component({
  selector: 'app-customers-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatProgressBarModule,
    MatSnackBarModule,
  ],
  templateUrl: './customers-page.html',
  styleUrl: './customers-page.css',
})
export class CustomersPage implements OnInit {
  private readonly api = inject(ShopwiseApiService);
  private readonly formBuilder = inject(FormBuilder);
  private readonly snackBar = inject(MatSnackBar);

  protected readonly customers = signal<Customer[]>([]);
  protected readonly isLoading = signal(true);
  protected readonly isSaving = signal(false);
  protected readonly selectedCustomerId = signal<number | null>(null);
  protected readonly errorMessage = signal('');

  protected readonly customerForm = this.formBuilder.nonNullable.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    phone: [''],
  });

  ngOnInit(): void {
    this.loadCustomers();
  }

  protected submitCustomer(): void {
    if (this.customerForm.invalid) {
      this.customerForm.markAllAsTouched();
      return;
    }

    this.isSaving.set(true);
    this.errorMessage.set('');

    const payload = this.buildPayload();
    const selectedId = this.selectedCustomerId();
    const request$ = selectedId
      ? this.api.updateCustomer(selectedId, payload)
      : this.api.createCustomer(payload);

    request$
      .pipe(finalize(() => this.isSaving.set(false)))
      .subscribe({
        next: (customer) => {
          this.upsertCustomer(customer);
          this.snackBar.open(
            selectedId ? 'Fiche client mise à jour.' : 'Client ajouté à la base.',
            'Fermer',
            { duration: 3000 },
          );
          this.resetForm();
        },
        error: () => {
          this.errorMessage.set(
            'Impossible d’enregistrer la fiche client. Vérifiez les informations saisies.',
          );
        },
      });
  }

  protected startEdit(customer: Customer): void {
    this.selectedCustomerId.set(customer.id);
    this.errorMessage.set('');
    this.customerForm.setValue({
      firstName: customer.firstName,
      lastName: customer.lastName,
      email: customer.email,
      phone: customer.phone ?? '',
    });
  }

  protected resetForm(): void {
    this.selectedCustomerId.set(null);
    this.customerForm.reset({
      firstName: '',
      lastName: '',
      email: '',
      phone: '',
    });
  }

  protected customerLabel(customer: Customer): string {
    return `${customer.firstName} ${customer.lastName}`;
  }

  private loadCustomers(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.api
      .getCustomers()
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: (customers) => this.customers.set(this.sortCustomers(customers)),
        error: () => {
          this.errorMessage.set(
            'Impossible de charger les clients. Assurez-vous que le backend est démarré.',
          );
        },
      });
  }

  private buildPayload(): CustomerPayload {
    const rawValue = this.customerForm.getRawValue();

    return {
      firstName: rawValue.firstName.trim(),
      lastName: rawValue.lastName.trim(),
      email: rawValue.email.trim().toLowerCase(),
      phone: rawValue.phone.trim() || null,
    };
  }

  private upsertCustomer(customer: Customer): void {
    const customers = [...this.customers()];
    const existingIndex = customers.findIndex((item) => item.id === customer.id);

    if (existingIndex === -1) {
      customers.push(customer);
    } else {
      customers[existingIndex] = customer;
    }

    this.customers.set(this.sortCustomers(customers));
  }

  private sortCustomers(customers: Customer[]): Customer[] {
    return [...customers].sort((left, right) =>
      `${left.lastName} ${left.firstName}`.localeCompare(`${right.lastName} ${right.firstName}`, 'fr'),
    );
  }
}
