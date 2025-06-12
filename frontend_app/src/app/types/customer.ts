import { Address } from './address'; 

export interface CustomerProfile {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  address: Address;
  username: string; 
  active: boolean; 
}

export interface CustomerProfileUpdate {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  address: Address;
}
