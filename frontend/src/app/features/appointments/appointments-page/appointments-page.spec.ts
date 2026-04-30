import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { of } from 'rxjs';
import { AppointmentsPage } from './appointments-page';
import { ShopwiseApiService } from '../../../core/shopwise-api.service';

describe('AppointmentsPage', () => {
  let fixture: ComponentFixture<AppointmentsPage>;
  let component: AppointmentsPage;

  const apiMock = {
    getCustomers: () =>
      of([
        {
          id: 1,
          firstName: 'Alice',
          lastName: 'Martin',
          email: 'alice@example.com',
          phone: '+33601020304',
          loyaltyPoints: 100,
        },
      ]),
    getServices: () =>
      of([
        {
          id: 2,
          name: 'Conseil produit',
          description: 'Conseil personnalisé',
          durationMinutes: 30,
          pointsReward: 100,
        },
      ]),
    getAppointments: () =>
      of([
        {
          id: 10,
          customer: {
            id: 1,
            firstName: 'Alice',
            lastName: 'Martin',
            email: 'alice@example.com',
            phone: '+33601020304',
            loyaltyPoints: 100,
          },
          service: {
            id: 2,
            name: 'Conseil produit',
            description: 'Conseil personnalisé',
            durationMinutes: 30,
            pointsReward: 100,
          },
          appointmentDate: '2026-05-01',
          appointmentTime: '10:30:00',
          status: 'SCHEDULED',
          notes: 'Prévoir la sélection locale.',
        },
      ]),
    createAppointment: () =>
      of({
        id: 11,
        customer: {
          id: 1,
          firstName: 'Alice',
          lastName: 'Martin',
          email: 'alice@example.com',
          phone: '+33601020304',
          loyaltyPoints: 100,
        },
        service: {
          id: 2,
          name: 'Conseil produit',
          description: 'Conseil personnalisé',
          durationMinutes: 30,
          pointsReward: 100,
        },
        appointmentDate: '2026-05-02',
        appointmentTime: '11:00:00',
        status: 'SCHEDULED',
        notes: null,
      }),
    honorAppointment: () =>
      of({
        id: 10,
        customer: {
          id: 1,
          firstName: 'Alice',
          lastName: 'Martin',
          email: 'alice@example.com',
          phone: '+33601020304',
          loyaltyPoints: 200,
        },
        service: {
          id: 2,
          name: 'Conseil produit',
          description: 'Conseil personnalisé',
          durationMinutes: 30,
          pointsReward: 100,
        },
        appointmentDate: '2026-05-01',
        appointmentTime: '10:30:00',
        status: 'HONORED',
        notes: 'Prévoir la sélection locale.',
      }),
    cancelAppointment: () =>
      of({
        id: 10,
        customer: {
          id: 1,
          firstName: 'Alice',
          lastName: 'Martin',
          email: 'alice@example.com',
          phone: '+33601020304',
          loyaltyPoints: 100,
        },
        service: {
          id: 2,
          name: 'Conseil produit',
          description: 'Conseil personnalisé',
          durationMinutes: 30,
          pointsReward: 100,
        },
        appointmentDate: '2026-05-01',
        appointmentTime: '10:30:00',
        status: 'CANCELLED',
        notes: 'Prévoir la sélection locale.',
      }),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppointmentsPage],
      providers: [
        provideNoopAnimations(),
        {
          provide: ShopwiseApiService,
          useValue: apiMock,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppointmentsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should render loaded appointments', () => {
    const nativeElement = fixture.nativeElement as HTMLElement;

    expect(nativeElement.textContent).toContain('Planning et suivi');
    expect(nativeElement.textContent).toContain('Alice Martin');
    expect(nativeElement.textContent).toContain('Conseil produit');
  });

  it('should expose readable labels for statuses', () => {
    expect((component as any).statusLabel('SCHEDULED')).toBe('Planifié');
    expect((component as any).statusLabel('HONORED')).toBe('Honoré');
    expect((component as any).statusLabel('CANCELLED')).toBe('Annulé');
  });
});
