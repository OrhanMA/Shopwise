import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { switchMap } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { Customer, LoyaltySummary, LoyaltyTransaction } from '../../../core/models';
import { ShopwiseApiService } from '../../../core/shopwise-api.service';

@Component({
  selector: 'app-client-portal-page',
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
  templateUrl: './client-portal-page.html',
  styleUrl: './client-portal-page.css',
})
export class ClientPortalPage {
  private readonly api = inject(ShopwiseApiService);
  private readonly formBuilder = inject(FormBuilder);
  private readonly snackBar = inject(MatSnackBar);

  protected readonly loginForm = this.formBuilder.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
  });

  protected readonly isLoading = signal(false);
  protected readonly errorMessage = signal('');
  protected readonly customer = signal<Customer | null>(null);
  protected readonly loyaltySummary = signal<LoyaltySummary | null>(null);

  protected login(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set('');

    const email = this.loginForm.getRawValue().email.trim().toLowerCase();

    this.api
      .simulateCustomerLogin(email)
      .pipe(
        switchMap((customer) =>
          this.api.getLoyaltySummary(customer.id).pipe(
            finalize(() => {
              this.customer.set(customer);
            }),
          ),
        ),
        finalize(() => this.isLoading.set(false)),
      )
      .subscribe({
        next: (summary) => {
          this.loyaltySummary.set(summary);
          this.snackBar.open('Portail client chargé.', 'Fermer', { duration: 3000 });
        },
        error: () => {
          this.customer.set(null);
          this.loyaltySummary.set(null);
          this.errorMessage.set(
            'Aucun client trouvé avec cet email. Utilisez une adresse existante de la base.',
          );
        },
      });
  }

  protected disconnect(): void {
    this.customer.set(null);
    this.loyaltySummary.set(null);
    this.loginForm.reset({ email: '' });
  }

  protected customerName(customer: Customer): string {
    return `${customer.firstName} ${customer.lastName}`;
  }

  protected transactionLabel(transaction: LoyaltyTransaction): string {
    if (transaction.transactionType === 'EARNED') {
      return 'Points gagnés';
    }

    if (transaction.transactionType === 'ADJUSTED') {
      return 'Ajustement';
    }

    return 'Points utilisés';
  }

  protected formatDateTime(value: string): string {
    return new Intl.DateTimeFormat('fr-FR', {
      day: '2-digit',
      month: 'short',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    }).format(new Date(value));
  }
}
