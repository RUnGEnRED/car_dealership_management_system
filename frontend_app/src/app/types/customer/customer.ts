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

export interface CustomerProfileFormData {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  address: {
    street: string;
    city: string;
    postalCode: string;
    country: string;
  };
}

export interface MyProfileSectionProps {
  profile: CustomerProfile | null;
  loading: boolean;
  error: string | null;
  editMode: boolean;
  formData: CustomerProfileFormData;
  saveLoading: boolean;
  saveError: string | null;
  setEditMode: (mode: boolean) => void;
  handleInputChange: (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => void;
  handleAddressChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleSubmit: (e: React.FormEvent) => void;
  resetForm: () => void;
}
