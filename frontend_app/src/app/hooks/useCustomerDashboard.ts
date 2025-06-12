import { useState, useEffect } from 'react';
import { Car } from '../types/car'; 

/**
 * This hook manages fetching and filtering car data for the customer dashboard.
 * It handles loading states, errors, and provides functionality to filter cars
 * to show only those currently available.
 */

interface UseCustomerDashboardResult {
  cars: Car[];
  loading: boolean;
  error: string | null;
  filterAvailableOnly: boolean;
  setFilterAvailableOnly: React.Dispatch<React.SetStateAction<boolean>>;
}

export const useCustomerDashboard = (): UseCustomerDashboardResult => {
  const [cars, setCars] = useState<Car[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [filterAvailableOnly, setFilterAvailableOnly] = useState<boolean>(false);

  useEffect(() => {
    const fetchCars = async () => {
      setLoading(true);
      setError(null);
      try {
        const url = filterAvailableOnly
          ? 'http://localhost:3001/api/vehicles/available'
          : 'http://localhost:3001/api/vehicles';

        const response = await fetch(url);
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        setCars(data);
      } catch (e: any) {
        setError(e.message || 'An error occurred while fetching cars.');
      } finally {
        setLoading(false);
      }
    };

    fetchCars();
  }, [filterAvailableOnly]); 

  return {
    cars,
    loading,
    error,
    filterAvailableOnly,
    setFilterAvailableOnly,
  };
};