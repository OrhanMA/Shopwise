import { Routes } from '@angular/router';
import { AppointmentsPage } from './features/appointments/appointments-page/appointments-page';
import { ClientPortalPage } from './features/client-portal/client-portal-page/client-portal-page';
import { CustomersPage } from './features/customers/customers-page/customers-page';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'clients',
  },
  {
    path: 'clients',
    component: CustomersPage,
    title: 'ShopWise | Clients',
  },
  {
    path: 'rendez-vous',
    component: AppointmentsPage,
    title: 'ShopWise | Rendez-vous',
  },
  {
    path: 'portail-client',
    component: ClientPortalPage,
    title: 'ShopWise | Portail client',
  },
  {
    path: '**',
    redirectTo: 'clients',
  },
];
