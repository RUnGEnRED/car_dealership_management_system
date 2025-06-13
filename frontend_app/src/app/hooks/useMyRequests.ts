import { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import { DetailedRequest } from '../types/request'; 

/**
 * Custom hook to fetch and manage the user's requests.
 * It handles fetching requests from the API, managing loading and error states,   
 * and sorting requests by creation date.
 */

interface UseMyRequestsResult {
  myRequests: DetailedRequest[];
  loading: boolean;
  error: string | null;
  fetchMyRequests: () => Promise<void>;
}

const MY_REQUESTS_URL = 'http://localhost:3001/api/requests/my';

export const useMyRequests = (): UseMyRequestsResult => {
  const [myRequests, setMyRequests] = useState<DetailedRequest[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

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

  const fetchMyRequests = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.get<DetailedRequest[]>(MY_REQUESTS_URL, getAuthHeaders());
      const sortedRequests = response.data.sort((a, b) => {
        return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
      });
      setMyRequests(sortedRequests);
    } catch (err: any) {
      if (axios.isAxiosError(err) && err.response && err.response.status === 401) {
        setError('Unauthorized. Please log in again.');
      } else {
        setError(err.message || 'Failed to fetch your requests.');
      }
      console.error('Fetch my requests error:', err);
    } finally {
      setLoading(false);
    }
  }, [getAuthHeaders]); 

  useEffect(() => {
    fetchMyRequests();
  }, [fetchMyRequests]);

  return {
    myRequests,
    loading,
    error,
    fetchMyRequests,
  };
};
