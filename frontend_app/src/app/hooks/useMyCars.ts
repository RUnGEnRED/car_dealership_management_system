import { useState, useEffect } from 'react';
import axios from 'axios';
import { Car } from '../types/car'; 

/**
 * This hook manages fetching and providing the list of cars owned by the authenticated customer.
 * It handles loading states, errors, and offers a function to manually refetch the car list.
 */


interface UseMyCarsResult {
  myCars: Car[];
  loading: boolean;
  error: string | null;
  fetchMyCars: () => Promise<void>; 
}

const MY_CARS_URL = 'http://localhost:3001/api/customers/me/vehicles';

export const useMyCars = (): UseMyCarsResult => {
  const [myCars, setMyCars] = useState<Car[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

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

  const fetchMyCars = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.get<Car[]>(MY_CARS_URL, getAuthHeaders());
      setMyCars(response.data);
    } catch (err: any) {
      if (axios.isAxiosError(err) && err.response && err.response.status === 401) {
        setError('Unauthorized. Please log in again.');
      } else {
        setError(err.message || 'Failed to fetch your cars.');
      }
      console.error('Fetch my cars error:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMyCars();
  }, []); 

  return {
    myCars,
    loading,
    error,
    fetchMyCars,
  };
};
