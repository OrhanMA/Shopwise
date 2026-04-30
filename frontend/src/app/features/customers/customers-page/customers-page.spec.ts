import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { of } from 'rxjs';
import { CustomersPage } from './customers-page';
import { ShopwiseApiService } from '../../../core/shopwise-api.service';

describe('CustomersPage', () => {
  let fixture: ComponentFixture<CustomersPage>;
  let component: CustomersPage;

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
    createCustomer: () =>
      of({
        id: 2,
        firstName: 'Karim',
        lastName: 'Benali',
        email: 'karim@example.com',
        phone: null,
        loyaltyPoints: 0,
      }),
    updateCustomer: () =>
      of({
        id: 1,
        firstName: 'Alice',
        lastName: 'Martin',
        email: 'alice@example.com',
        phone: '+33601020305',
        loyaltyPoints: 100,
      }),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CustomersPage],
      providers: [
        provideNoopAnimations(),
        {
          provide: ShopwiseApiService,
          useValue: apiMock,
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CustomersPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should render loaded customers', () => {
    const nativeElement = fixture.nativeElement as HTMLElement;

    expect(nativeElement.textContent).toContain('Base clients');
    expect(nativeElement.textContent).toContain('Alice Martin');
    expect(nativeElement.textContent).toContain('100');
  });

  it('should switch to edit mode when a customer is selected', () => {
    const customer = (component as any).customers()[0];

    (component as any).startEdit(customer);
    fixture.detectChanges();

    expect((component as any).selectedCustomerId()).toBe(1);
    expect((component as any).customerForm.getRawValue().email).toBe('alice@example.com');
  });
});
