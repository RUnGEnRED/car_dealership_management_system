import { Address } from '../address'; 

export interface EmployeeProfile {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  address: Address;
  username: string;
  position: 'MANAGER' | 'SALES' | 'SERVICE'; 
  active: boolean;
}

export interface EmployeeProfileUpdatePayload {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  address: Address;
}


export type ActiveEmployeeDashboardSection =
  | 'manageVehicles'
  | 'manageCustomers'
  | 'clientRequests'
  | 'employeeProfile';


export interface EmployeeDashboardPageProps {}
