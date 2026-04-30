import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ShopwiseApiService } from './shopwise-api.service';

describe('ShopwiseApiService', () => {
  let service: ShopwiseApiService;
  let httpController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(ShopwiseApiService);
    httpController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpController.verify();
  });

  it('should load customers', () => {
    service.getCustomers().subscribe();

    const request = httpController.expectOne('/api/customers');
    expect(request.request.method).toBe('GET');
    request.flush([]);
  });

  it('should create a customer', () => {
    service
      .createCustomer({
        firstName: 'Alice',
        lastName: 'Martin',
        email: 'alice@example.com',
        phone: null,
      })
      .subscribe();

    const request = httpController.expectOne('/api/customers');
    expect(request.request.method).toBe('POST');
    expect(request.request.body.email).toBe('alice@example.com');
    request.flush({});
  });

  it('should update a customer', () => {
    service
      .updateCustomer(7, {
        firstName: 'Alice',
        lastName: 'Martin',
        email: 'alice@example.com',
        phone: '123',
      })
      .subscribe();

    const request = httpController.expectOne('/api/customers/7');
    expect(request.request.method).toBe('PUT');
    request.flush({});
  });

  it('should simulate customer login and retrieve loyalty summary', () => {
    service.simulateCustomerLogin('alice@example.com').subscribe();
    service.getLoyaltySummary(7).subscribe();

    const loginRequest = httpController.expectOne('/api/customers/login');
    expect(loginRequest.request.method).toBe('POST');
    expect(loginRequest.request.body).toEqual({ email: 'alice@example.com' });
    loginRequest.flush({});

    const loyaltyRequest = httpController.expectOne('/api/customers/7/loyalty');
    expect(loyaltyRequest.request.method).toBe('GET');
    loyaltyRequest.flush({});
  });

  it('should load services and appointments with filters', () => {
    service.getServices().subscribe();
    service
      .getAppointments({
        date: '2026-04-30',
        status: 'SCHEDULED',
        customerId: 3,
      })
      .subscribe();

    const serviceRequest = httpController.expectOne('/api/services');
    expect(serviceRequest.request.method).toBe('GET');
    serviceRequest.flush([]);

    const appointmentsRequest = httpController.expectOne(
      (request) =>
        request.url === '/api/appointments' &&
        request.params.get('date') === '2026-04-30' &&
        request.params.get('status') === 'SCHEDULED' &&
        request.params.get('customerId') === '3',
    );

    expect(appointmentsRequest.request.method).toBe('GET');
    appointmentsRequest.flush([]);
  });

  it('should create and update appointment status', () => {
    service
      .createAppointment({
        customerId: 1,
        serviceId: 2,
        appointmentDate: '2026-05-01',
        appointmentTime: '10:30',
        notes: null,
      })
      .subscribe();
    service.honorAppointment(5).subscribe();
    service.cancelAppointment(5).subscribe();

    const createRequest = httpController.expectOne('/api/appointments');
    expect(createRequest.request.method).toBe('POST');
    createRequest.flush({});

    const honorRequest = httpController.expectOne('/api/appointments/5/honor');
    expect(honorRequest.request.method).toBe('POST');
    honorRequest.flush({});

    const cancelRequest = httpController.expectOne('/api/appointments/5/cancel');
    expect(cancelRequest.request.method).toBe('POST');
    cancelRequest.flush({});
  });
});
