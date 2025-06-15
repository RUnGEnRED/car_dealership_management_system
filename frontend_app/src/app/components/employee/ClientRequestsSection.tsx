import React, { useState, useEffect } from 'react';
import { DetailedRequest, RequestStatus } from '../../types/request';

interface ClientRequestsSectionProps {
  requests: DetailedRequest[];
  loading: boolean;
  error: string | null;
  fetchRequests: () => Promise<void>;
  approveRequest: (requestId: number) => Promise<void>;
  rejectRequest: (requestId: number) => Promise<void>;
}

export const ClientRequestsSection: React.FC<ClientRequestsSectionProps> = ({
  requests,
  loading,
  error,
  fetchRequests,
  approveRequest,
  rejectRequest,
}) => {
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [displaySearchedRequest, setDisplaySearchedRequest] = useState<DetailedRequest | null>(null);

  const filteredRequests = requests.filter(request => {
    return searchTerm
      ? request.id.toString().includes(searchTerm) ||
        request.customerUsername.toLowerCase().includes(searchTerm.toLowerCase()) ||
        request.requestType.toLowerCase().includes(searchTerm.toLowerCase())
      : true;
  });

  const handleSearchRequestById = () => {
    const foundRequest = requests.find(req => req.id.toString() === searchTerm);
    setDisplaySearchedRequest(foundRequest || null);
    if (!foundRequest && searchTerm) {
      console.log(`Request with ID ${searchTerm} not found.`);
    }
  };

  useEffect(() => {
    setDisplaySearchedRequest(null);
  }, [searchTerm]);

  if (loading) {
    return <p className="text-gray-700">Loading requests...</p>;
  }
  if (error) {
    return <p className="text-red-600">Error: {error}</p>;
  }

  return (
    <div>
      <h2 className="text-2xl font-semibold text-gray-700 mb-4">Client Requests</h2>
      <div className="flex space-x-2 mb-4">
        <input
          type="text"
          placeholder="Search Request by ID, Customer, or Type"
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <button
          onClick={handleSearchRequestById}
          className="bg-purple-600 hover:bg-purple-700 text-white font-bold py-2 px-4 rounded transition duration-200"
        >
          Search
        </button>
      </div>

      {displaySearchedRequest ? (
        <div className="bg-gray-50 p-4 rounded-md shadow-sm mb-4">
          <h3 className="text-xl font-semibold text-gray-700 mb-2">Search Result</h3>
          <p className="font-bold text-gray-900 text-lg">Request ID: {displaySearchedRequest.id} - Type: {displaySearchedRequest.requestType}</p>
          <p className="text-gray-800 text-sm">Customer: {displaySearchedRequest.customerUsername}</p>
          <p className="text-gray-800 text-sm">Vehicle ID: {displaySearchedRequest.vehicleId}</p>
          <p className="text-gray-800 text-sm">Status: {displaySearchedRequest.status}</p>
          <p className="text-gray-800 text-sm">Notes: {displaySearchedRequest.customerNotes}</p>
          <p className="text-gray-800 text-sm">Created: {new Date(displaySearchedRequest.createdAt).toLocaleDateString()}</p>
          <div className="mt-2 space-x-2">
            {displaySearchedRequest.status === 'PENDING' && (
              <>
                <button onClick={() => approveRequest(displaySearchedRequest.id)} className="bg-green-500 hover:bg-green-600 text-white font-bold py-1 px-3 rounded text-xs">Approve</button>
                <button onClick={() => rejectRequest(displaySearchedRequest.id)} className="bg-red-500 hover:bg-red-600 text-white font-bold py-1 px-3 rounded text-xs">Reject</button>
              </>
            )}
          </div>
        </div>
      ) : (
        searchTerm && <p className="text-red-600">No request found matching "{searchTerm}".</p>
      )}

      <h3 className="text-xl font-semibold text-gray-700 mt-6 mb-4">All Client Requests</h3>
      {filteredRequests.length === 0 ? (
        <p className="text-gray-700">No client requests found. (Once connected to API, data will appear here)</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {filteredRequests.map((request) => (
            <div key={request.id} className="bg-gray-50 p-4 rounded-md shadow-sm">
              <p className="font-bold text-gray-900 text-lg">
                Request ID: {request.id} - {request.requestType}
              </p>
              <p className="text-gray-800 text-sm">Customer: {request.customerUsername}</p>
              <p className="text-gray-800 text-sm">Vehicle ID: {request.vehicleId}</p>
              <p className="text-gray-800 text-sm">Status: {request.status}</p>
              <p className="text-gray-800 text-sm truncate">Notes: {request.customerNotes}</p>
              <p className="text-gray-800 text-xs">Created: {new Date(request.createdAt).toLocaleDateString()}</p>
              <div className="mt-2 space-x-2">
                {request.status === 'PENDING' ? (
                  <>
                    <button onClick={() => approveRequest(request.id)} className="bg-green-500 hover:bg-green-600 text-white font-bold py-1 px-3 rounded text-xs">Approve</button>
                    <button onClick={() => rejectRequest(request.id)} className="bg-red-500 hover:bg-red-600 text-white font-bold py-1 px-3 rounded text-xs">Reject</button>
                  </>
                ) : (
                  <span className={`text-xs font-semibold ${request.status === 'ACCEPTED' ? 'text-green-600' : request.status === 'REJECTED' ? 'text-red-600' : 'text-gray-600'}`}>
                    {request.status}
                  </span>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
