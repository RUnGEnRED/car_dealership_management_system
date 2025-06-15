import { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import { DetailedRequest, RequestStatus } from '../../types/request';

interface UseEmployeeRequestsResult {
  requests: DetailedRequest[];
  loading: boolean;
  error: string | null;
  fetchRequests: () => Promise<void>;
  approveRequest: (requestId: number) => Promise<void>;
  rejectRequest: (requestId: number) => Promise<void>;
}

const ALL_REQUESTS_URL = 'http://localhost:3001/api/requests'; 
const UPDATE_REQUEST_STATUS_URL = (id: number) => `http://localhost:3001/api/requests/${id}/status`;

export const useEmployeeRequests = (): UseEmployeeRequestsResult => {
  const [requests, setRequests] = useState<DetailedRequest[]>([]);
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

  const fetchRequests = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.get<DetailedRequest[]>(ALL_REQUESTS_URL, getAuthHeaders());
      const sortedRequests = response.data.sort((a, b) => {
        return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
      });
      setRequests(sortedRequests);
    } catch (err: any) {
      if (axios.isAxiosError(err) && err.response && err.response.status === 401) {
        setError('Unauthorized. Please log in again to fetch requests.');
      } else {
        setError(err.message || 'Failed to fetch client requests.');
      }
      console.error('Fetch client requests error:', err);
    } finally {
      setLoading(false);
    }
  }, [getAuthHeaders]);

  const updateRequestStatus = async (requestId: number, newStatus: RequestStatus) => {
    try {
      const updatedRequests = requests.map(req =>
        req.id === requestId ? { ...req, status: newStatus, updatedAt: new Date().toISOString() } : req
      );
      setRequests(updatedRequests);
      console.log(`Request ${requestId} status updated to ${newStatus}`);
    } catch (err: any) {
      console.error(`Failed to update request ${requestId} status to ${newStatus}:`, err);
    }
  };

  const approveRequest = useCallback(async (requestId: number) => {
    await updateRequestStatus(requestId, 'ACCEPTED');
  }, [updateRequestStatus]);

  const rejectRequest = useCallback(async (requestId: number) => {
    await updateRequestStatus(requestId, 'REJECTED');
  }, [updateRequestStatus]);

  useEffect(() => {
    fetchRequests();
  }, [fetchRequests]);

  return {
    requests,
    loading,
    error,
    fetchRequests,
    approveRequest,
    rejectRequest,
  };
};
