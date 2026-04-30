import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import {
  Appointment,
  AppointmentFilters,
  AppointmentPayload,
  Customer,
  CustomerPayload,
  LoyaltySummary,
  ServiceOffering,
} from './models';

@Injectable({
  providedIn: 'root',
})
export class ShopwiseApiService {
  private readonly http = inject(HttpClient);
  private readonly apiBaseUrl = '/api';

  getCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.apiBaseUrl}/customers`);
  }

  createCustomer(payload: CustomerPayload): Observable<Customer> {
    return this.http.post<Customer>(`${this.apiBaseUrl}/customers`, payload);
  }

  updateCustomer(id: number, payload: CustomerPayload): Observable<Customer> {
    return this.http.put<Customer>(`${this.apiBaseUrl}/customers/${id}`, payload);
  }

  simulateCustomerLogin(email: string): Observable<Customer> {
    return this.http.post<Customer>(`${this.apiBaseUrl}/customers/login`, { email });
  }

  getLoyaltySummary(customerId: number): Observable<LoyaltySummary> {
    return this.http.get<LoyaltySummary>(`${this.apiBaseUrl}/customers/${customerId}/loyalty`);
  }

  getServices(): Observable<ServiceOffering[]> {
    return this.http.get<ServiceOffering[]>(`${this.apiBaseUrl}/services`);
  }

  getAppointments(filters: AppointmentFilters = {}): Observable<Appointment[]> {
    let params = new HttpParams();

    if (filters.date) {
      params = params.set('date', filters.date);
    }

    if (filters.status) {
      params = params.set('status', filters.status);
    }

    if (filters.customerId) {
      params = params.set('customerId', String(filters.customerId));
    }

    return this.http.get<Appointment[]>(`${this.apiBaseUrl}/appointments`, { params });
  }

  createAppointment(payload: AppointmentPayload): Observable<Appointment> {
    return this.http.post<Appointment>(`${this.apiBaseUrl}/appointments`, payload);
  }

  honorAppointment(id: number): Observable<Appointment> {
    return this.http.post<Appointment>(`${this.apiBaseUrl}/appointments/${id}/honor`, {});
  }

  cancelAppointment(id: number): Observable<Appointment> {
    return this.http.post<Appointment>(`${this.apiBaseUrl}/appointments/${id}/cancel`, {});
  }
}
