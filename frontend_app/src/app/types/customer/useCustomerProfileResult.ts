import { CustomerProfile, CustomerProfileFormData } from '../types/customer';

export interface UseCustomerProfileResult {
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