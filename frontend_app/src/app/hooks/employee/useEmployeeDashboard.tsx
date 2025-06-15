import { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import { Car } from '../../types/car';
import { CustomerProfile } from '../../types/customer/customer';

interface UseEmployeeDashboardResult {
  vehicles: Car[];
  customers: CustomerProfile[];
  loadingVehicles: boolean;
  errorVehicles: string | null;
  loadingCustomers: boolean;
  errorCustomers: string | null;
  fetchVehicles: () => Promise<void>;
  fetchCustomers: () => Promise<void>;
}

const ALL_VEHICLES_URL = 'http://localhost:3001/api/vehicles';
const ALL_CUSTOMERS_URL = 'http://localhost:3001/api/customers';


export const useEmployeeDashboard = (): UseEmployeeDashboardResult => {
  const [vehicles, setVehicles] = useState<Car[]>([]);
  const [customers, setCustomers] = useState<CustomerProfile[]>([]);
  const [loadingVehicles, setLoadingVehicles] = useState<boolean>(true);
  const [errorVehicles, setErrorVehicles] = useState<string | null>(null);
  const [loadingCustomers, setLoadingCustomers] = useState<boolean>(true);
  const [errorCustomers, setErrorCustomers] = useState<string | null>(null);

  const getAuthHeaders = useCallback(() => {
    const token = localStorage.getItem('jwt_token');
    if (!token) {
      return {};
    }
    return {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
  }, []);

  const fetchVehicles = useCallback(async () => {
    setLoadingVehicles(true);
    setErrorVehicles(null);
    try {
      const response = await axios.get<Car[]>(ALL_VEHICLES_URL, getAuthHeaders());
      setVehicles(response.data);
    } catch (err: any) {
      if (axios.isAxiosError(err) && err.response && err.response.status === 401) {
        setErrorVehicles('Unauthorized. Please log in again to see vehicles.');
      } else {
        setErrorVehicles(err.message || 'Failed to fetch vehicles.');
      }
      console.error('Fetch vehicles error:', err);
    } finally {
      setLoadingVehicles(false);
    }
  }, [getAuthHeaders]);

  const fetchCustomers = useCallback(async () => {
    setLoadingCustomers(true);
    setErrorCustomers(null);
    try {
      const response = await axios.get<CustomerProfile[]>(ALL_CUSTOMERS_URL, getAuthHeaders());
      setCustomers(response.data);
    } catch (err: any) {
      if (axios.isAxiosError(err) && err.response && err.response.status === 401) {
        setErrorCustomers('Unauthorized. Please log in again to see customers.');
      } else {
        setErrorCustomers(err.message || 'Failed to fetch customers.');
      }
      console.error('Fetch customers error:', err);
    } finally {
      setLoadingCustomers(false);
    }
  }, [getAuthHeaders]);

  useEffect(() => {
    fetchVehicles();
    fetchCustomers();
  }, [fetchVehicles, fetchCustomers]);

  return {
    vehicles,
    customers,
    loadingVehicles,
    errorVehicles,
    loadingCustomers,
    errorCustomers,
    fetchVehicles,
    fetchCustomers,
  };
};
