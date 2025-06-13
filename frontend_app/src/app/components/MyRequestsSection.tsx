import React from 'react';
import { useMyRequests } from '../hooks/useMyRequests'; 

/**
 * MyRequestsSection component displays the user's sent requests.
 * It handles loading states, errors, and displays each request's details.
 */

export const MyRequestsSection: React.FC = () => {
  const {
    myRequests,
    loading: myRequestsLoading,
    error: myRequestsError,
  } = useMyRequests(); 

  if (myRequestsLoading) {
    return <p className="text-gray-700">Loading your requests...</p>;
  }
  if (myRequestsError) {
    return <p className="text-red-600">Error: {myRequestsError}</p>;
  }
  if (myRequests.length === 0) {
    return <p className="text-gray-700">You haven't sent any requests yet.</p>;
  }

  return (
    <div>
      <h2 className="text-2xl font-semibold text-gray-700 mb-4">My Sent Requests</h2>
      <p className="text-gray-600 mb-4">
        Here you will see the statuses of all your submitted requests:
      </p>
      {myRequests.map((request) => (
        <div key={request.id} className="bg-gray-50 p-4 rounded-md mt-4 shadow-sm">
          <p className="font-bold text-gray-900">Request #{request.id}</p>
          <p className="text-gray-800">Type: {request.requestType}, Status: {request.status}</p>
          {request.vehicleVin && <p className="text-gray-800 text-sm">Vehicle VIN: {request.vehicleVin}</p>}
          {request.customerNotes && <p className="text-gray-800 text-sm">Notes: {request.customerNotes}</p>}
          <p className="text-gray-800 text-sm">Created At: {new Date(request.createdAt).toLocaleString()}</p>
        </div>
      ))}
    </div>
  );
};