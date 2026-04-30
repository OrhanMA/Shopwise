export type AppointmentStatus = 'SCHEDULED' | 'HONORED' | 'CANCELLED';

export type LoyaltyTransactionType = 'EARNED' | 'REDEEMED' | 'ADJUSTED';

export interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string | null;
  loyaltyPoints: number;
}

export interface CustomerPayload {
  firstName: string;
  lastName: string;
  email: string;
  phone: string | null;
}

export interface ServiceOffering {
  id: number;
  name: string;
  description: string;
  durationMinutes: number;
  pointsReward: number;
}

export interface Appointment {
  id: number;
  customer: Customer;
  service: ServiceOffering;
  appointmentDate: string;
  appointmentTime: string;
  status: AppointmentStatus;
  notes: string | null;
}

export interface AppointmentPayload {
  customerId: number;
  serviceId: number;
  appointmentDate: string;
  appointmentTime: string;
  notes: string | null;
}

export interface AppointmentFilters {
  date?: string | null;
  status?: AppointmentStatus | '' | null;
  customerId?: number | null;
}

export interface LoyaltyTransaction {
  id: number;
  points: number;
  reason: string;
  transactionType: LoyaltyTransactionType;
  createdAt: string;
}

export interface LoyaltySummary {
  customer: Customer;
  balance: number;
  transactions: LoyaltyTransaction[];
}
