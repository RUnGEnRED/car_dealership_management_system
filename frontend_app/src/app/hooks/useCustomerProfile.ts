import { useState, useEffect } from 'react';
import axios from 'axios';
import { CustomerProfile, CustomerProfileUpdate } from '../types/customer'; 
/**
 * This hook manages fetching, displaying, and editing customer profile data.
 * It allows switching between view and edit modes,
 * handles form changes, and sends updated data to the API.
 */

interface UseCustomerProfileResult {
  profile: CustomerProfile | null;
  loading: boolean;
  error: string | null;
  editMode: boolean;
  formData: CustomerProfileUpdate;
  saveLoading: boolean;
  saveError: string | null;
  setEditMode: (mode: boolean) => void;
  handleInputChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleAddressChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleSubmit: (e: React.FormEvent) => Promise<void>;
  resetForm: () => void; 
  fetchProfile: () => Promise<void>; 
}

const CUSTOMER_PROFILE_URL = 'http://localhost:3001/api/customers/me';

export const useCustomerProfile = (): UseCustomerProfileResult => {
  const [profile, setProfile] = useState<CustomerProfile | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const [editMode, setEditMode] = useState<boolean>(false);
  const [formData, setFormData] = useState<CustomerProfileUpdate>({
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    address: { street: '', city: '', postalCode: '', country: '' },
  });
  const [saveLoading, setSaveLoading] = useState<boolean>(false);
  const [saveError, setSaveError] = useState<string | null>(null);

  const getAuthHeaders = () => {
    const token = localStorage.getItem('jwt_token');
    if (!token) {
      setError('Authentication token not found. Please log in again.');
      return {}; 
    }
    return {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
  };

  const fetchProfile = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.get<CustomerProfile>(CUSTOMER_PROFILE_URL, getAuthHeaders());
      setProfile(response.data);
      setFormData({
        firstName: response.data.firstName,
        lastName: response.data.lastName,
        email: response.data.email,
        phoneNumber: response.data.phoneNumber,
        address: response.data.address,
      });
    } catch (err: any) {
      if (axios.isAxiosError(err) && err.response && err.response.status === 401) {
        setError('Unauthorized. Please log in again.');
      } else {
        setError(err.message || 'Failed to fetch profile data.');
      }
      console.error('Fetch profile error:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProfile();
  }, []); 

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleAddressChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      address: {
        ...prev.address,
        [name]: value,
      },
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaveLoading(true);
    setSaveError(null);
    try {
      await axios.put(CUSTOMER_PROFILE_URL, formData, getAuthHeaders());
      await fetchProfile();
      setEditMode(false); 
    } catch (err: any) {
      if (axios.isAxiosError(err) && err.response && err.response.data && err.response.data.message) {
        setSaveError(err.response.data.message);
      } else {
        setSaveError(err.message || 'Failed to update profile.');
      }
      console.error('Update profile error:', err);
    } finally {
      setSaveLoading(false);
    }
  };

  const resetForm = () => {
    if (profile) {
      setFormData({
        firstName: profile.firstName,
        lastName: profile.lastName,
        email: profile.email,
        phoneNumber: profile.phoneNumber,
        address: profile.address,
      });
    }
  };

  return {
    profile,
    loading,
    error,
    editMode,
    formData,
    saveLoading,
    saveError,
    setEditMode,
    handleInputChange,
    handleAddressChange,
    handleSubmit,
    resetForm,
    fetchProfile,
  };
};
