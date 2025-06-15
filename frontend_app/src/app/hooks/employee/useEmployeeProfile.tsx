import { useState, useEffect, useCallback } from 'react';
import axios, { AxiosError } from 'axios';
import { EmployeeProfile, EmployeeProfileUpdatePayload } from '../../types/employee/employee'; 

interface UseEmployeeProfileResult {
  profile: EmployeeProfile | null;
  loading: boolean;
  error: string | null;
  editMode: boolean;
  formData: EmployeeProfileUpdatePayload; 
  saveLoading: boolean;
  saveError: string | null;
  setEditMode: (mode: boolean) => void;
  handleInputChange: (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => void;
  handleAddressChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleSubmit: (e: React.FormEvent) => Promise<void>;
  resetForm: () => void;
  fetchEmployeeProfile: () => Promise<void>;
}

const EMPLOYEE_PROFILE_URL = 'http://localhost:3001/api/employees/me';

export const useEmployeeProfile = (): UseEmployeeProfileResult => {
  const [profile, setProfile] = useState<EmployeeProfile | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [editMode, setEditMode] = useState<boolean>(false);
  const [formData, setFormData] = useState<EmployeeProfileUpdatePayload>({
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    address: { street: '', city: '', postalCode: '', country: '' },
  });
  const [saveLoading, setSaveLoading] = useState<boolean>(false);
  const [saveError, setSaveError] = useState<string | null>(null);

  const getAuthHeaders = useCallback(() => {
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
  }, []);

  const fetchEmployeeProfile = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.get<EmployeeProfile>(EMPLOYEE_PROFILE_URL, getAuthHeaders());
      setProfile(response.data);
      setFormData({
        firstName: response.data.firstName,
        lastName: response.data.lastName,
        email: response.data.email,
        phoneNumber: response.data.phoneNumber,
        address: response.data.address || { street: '', city: '', postalCode: '', country: '' }, 
      });
    } catch (err: any) {
      if (axios.isAxiosError(err) && err.response && err.response.status === 401) {
        setError('Unauthorized. Please log in again.');
      } else {
        setError(err.message || 'Failed to fetch employee profile.');
      }
      console.error('Fetch employee profile error:', err);
    } finally {
      setLoading(false);
    }
  }, [getAuthHeaders]);

  useEffect(() => {
    fetchEmployeeProfile();
  }, [fetchEmployeeProfile]);

  const handleInputChange = useCallback((e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  }, []);

  const handleAddressChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      address: {
        ...prevData.address,
        [name]: value,
      },
    }));
  }, []);

  const resetForm = useCallback(() => {
    if (profile) {
      setFormData({
        firstName: profile.firstName,
        lastName: profile.lastName,
        email: profile.email,
        phoneNumber: profile.phoneNumber,
        address: profile.address || { street: '', city: '', postalCode: '', country: '' },
      });
    }
    setSaveError(null);
  }, [profile]);

  const handleSubmit = useCallback(async (e: React.FormEvent) => {
    e.preventDefault();
    setSaveLoading(true);
    setSaveError(null);

    try {
      const payload: EmployeeProfileUpdatePayload = {
        firstName: formData.firstName,
        lastName: formData.lastName,
        email: formData.email,
        phoneNumber: formData.phoneNumber,
        address: formData.address,
      };

      await axios.put(EMPLOYEE_PROFILE_URL, payload, getAuthHeaders());
      await fetchEmployeeProfile(); 
      setEditMode(false); 
    } catch (err: any) {
      if (axios.isAxiosError(err) && err.response && err.response.data && err.response.data.message) {
        setSaveError(err.response.data.message);
      } else {
        setSaveError(err.message || 'Failed to save profile changes.');
      }
      console.error('Save employee profile error:', err);
    } finally {
      setSaveLoading(false);
    }
  }, [formData, getAuthHeaders, fetchEmployeeProfile]);

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
    fetchEmployeeProfile, 
  };
};
