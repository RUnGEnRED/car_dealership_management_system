import { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import { Car } from '../types/car';
import { ServiceRequestPayload, InspectionRequestPayload, RequestResponse } from '../types/request';

/**
 * This hook manages the logic for handling car service and inspection request forms.
 * It handles fetching the user's cars, managing form input, submitting requests to the API,
 * and displaying success or error messages, including a success pop-up.
 */

interface UseRequestFormsResult {
  myCars: Car[];
  myCarsLoading: boolean;
  myCarsError: string | null;
  selectedVehicleId: string | number | '';
  customerNotes: string;
  formLoading: boolean;
  formError: string | null;
  formSuccess: string | null;
  showSuccessPopup: boolean; 
  setSelectedVehicleId: (id: string | number | '') => void;
  setCustomerNotes: (notes: string) => void;
  submitServiceRequest: () => Promise<void>;
  submitInspectionRequest: () => Promise<void>;
  resetRequestForm: () => void;
  closeSuccessPopup: () => void; 
}

const SERVICE_REQUEST_URL = 'http://localhost:3001/api/requests/service';
const INSPECTION_REQUEST_URL = 'http://localhost:3001/api/requests/inspection';
const MY_CARS_URL = 'http://localhost:3001/api/customers/me/vehicles';

export const useRequestForms = (): UseRequestFormsResult => {
  const [myCars, setMyCars] = useState<Car[]>([]);
  const [myCarsLoading, setMyCarsLoading] = useState<boolean>(true);
  const [myCarsError, setMyCarsError] = useState<string | null>(null);

  const [selectedVehicleId, setSelectedVehicleId] = useState<string | number | ''>('');
  const [customerNotes, setCustomerNotes] = useState<string>('');
  const [formLoading, setFormLoading] = useState<boolean>(false);
  const [formError, setFormError] = useState<string | null>(null);
  const [formSuccess, setFormSuccess] = useState<string | null>(null);
  const [showSuccessPopup, setShowSuccessPopup] = useState<boolean>(false);

  const getAuthHeaders = useCallback(() => {
    const token = localStorage.getItem('jwt_token');
    if (!token) {
      setFormError('Authentication token not found. Please log in again.');
      return {};
    }
    return {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
  }, []);

  const resetRequestForm = useCallback(() => {
    setCustomerNotes('');
    setFormError(null);
    setFormSuccess(null);
    if (myCars.length > 0) {
      setSelectedVehicleId(myCars[0].id);
    } else {
      setSelectedVehicleId('');
    }
  }, [myCars]);

  const closeSuccessPopup = useCallback(() => {
    setShowSuccessPopup(false);
    setFormSuccess(null);
    resetRequestForm(); 
  }, [resetRequestForm]);

  useEffect(() => {
    const fetchUserCars = async () => {
      setMyCarsLoading(true);
      setMyCarsError(null);
      try {
        const response = await axios.get<Car[]>(MY_CARS_URL, getAuthHeaders());
        setMyCars(response.data);
        if (response.data.length > 0) {
          setSelectedVehicleId(response.data[0].id); 
        }
      } catch (err: any) {
        if (axios.isAxiosError(err) && err.response && err.response.status === 401) {
          setMyCarsError('Unauthorized. Please log in again to see your cars.');
        } else {
          setMyCarsError(err.message || 'Failed to fetch your cars for the form.');
        }
        console.error('Fetch user cars for form error:', err);
      } finally {
        setMyCarsLoading(false);
      }
    };

    fetchUserCars();
  }, [getAuthHeaders]);

  const submitServiceRequest = async () => {
    setFormLoading(true);
    setFormError(null);
    setFormSuccess(null); 

    if (!selectedVehicleId) {
      setFormError('Please select a car.');
      setFormLoading(false);
      return;
    }

    try {
      const payload: ServiceRequestPayload = {
        vehicleId: selectedVehicleId,
        customerNotes: customerNotes,
      };
      const response = await axios.post<RequestResponse>(SERVICE_REQUEST_URL, payload, getAuthHeaders());
      setFormSuccess(response.data.message || 'Service request submitted successfully!');
      setShowSuccessPopup(true); 
    } catch (err: any) {
      if (axios.isAxiosError(err) && err.response && err.response.data && err.response.data.message) {
        setFormError(err.response.data.message);
      } else {
        setFormError(err.message || 'Failed to submit service request.');
      }
      console.error('Service request error:', err);
    } finally {
      setFormLoading(false);
    }
  };

  const submitInspectionRequest = async () => {
    setFormLoading(true);
    setFormError(null);
    setFormSuccess(null); 

    if (!selectedVehicleId) {
      setFormError('Please select a car.');
      setFormLoading(false);
      return;
    }

    try {
      const payload: InspectionRequestPayload = {
        vehicleId: selectedVehicleId,
        customerNotes: customerNotes,
      };
      const response = await axios.post<RequestResponse>(INSPECTION_REQUEST_URL, payload, getAuthHeaders());
      setFormSuccess(response.data.message || 'Inspection request submitted successfully!');
      setShowSuccessPopup(true); 
    } catch (err: any) {
      if (axios.isAxiosError(err) && err.response && err.response.data && err.response.data.message) {
        setFormError(err.response.data.message);
      } else {
        setFormError(err.message || 'Failed to submit inspection request.');
      }
      console.error('Inspection request error:', err);
    } finally {
      setFormLoading(false);
    }
  };

  return {
    myCars,
    myCarsLoading,
    myCarsError,
    selectedVehicleId,
    customerNotes,
    formLoading,
    formError,
    formSuccess,
    showSuccessPopup, 
    setSelectedVehicleId,
    setCustomerNotes,
    submitServiceRequest,
    submitInspectionRequest,
    resetRequestForm,
    closeSuccessPopup, 
  };
};
