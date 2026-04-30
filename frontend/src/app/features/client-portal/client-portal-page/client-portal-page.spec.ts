import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { of } from 'rxjs';
import { ClientPortalPage } from './client-portal-page';
import { ShopwiseApiService } from '../../../core/shopwise-api.service';

describe('ClientPortalPage', () => {
  let fixture: ComponentFixture<ClientPortalPage>;
  let component: ClientPortalPage;

  const apiMock = {
    simulateCustomerLogin: () =>
      of({
        id: 1,
        firstName: 'Alice',
        lastName: 'Martin',
        email: 'alice@example.com',
        phone: '+33601020304',
        loyaltyPoints: 100,
      }),
    getLoyaltySummary: () =>
      of({
        customer: {
          id: 1,
          firstName: 'Alice',
          lastName: 'Martin',
          email: 'alice@example.com',
          phone: '+33601020304',
          loyaltyPoints: 100,
        },
        balance: 100,
        transactions: [
          {
            id: 1,
            points: 100,
            reason: 'Rendez-vous honoré',
            transactionType: 'EARNED',
            createdAt: '2026-05-01T10:30:00',
          },
        ],
      }),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientPortalPage],
      providers: [
        provideNoopAnimations(),
        {
          provide: ShopwiseApiService,
          useValue: apiMock,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ClientPortalPage);
    component = fixture.componentInstance;
  });

  it('should render the loyalty summary after a successful login', () => {
    (component as any).loginForm.setValue({ email: 'alice@example.com' });
    (component as any).login();
    fixture.detectChanges();

    const nativeElement = fixture.nativeElement as HTMLElement;
    expect(nativeElement.textContent).toContain('Connexion simulée');
    expect(nativeElement.textContent).toContain('Alice Martin');
    expect(nativeElement.textContent).toContain('Rendez-vous honoré');
    expect(nativeElement.textContent).toContain('100');
  });
});
