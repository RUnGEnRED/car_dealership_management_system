import React, { useState, useEffect } from 'react';
import { CustomerProfile } from '../../types/customer/customer';
import { Car } from '../../types/car'; 

interface ManageCustomersSectionProps {
  customers: CustomerProfile[];
  vehicles: Car[]; 
  loadingCustomers: boolean;
  errorCustomers: string | null;
  loadingVehicles: boolean; 
  errorVehicles: string | null; 
  fetchCustomers: () => Promise<void>;
  fetchVehicles: () => Promise<void>; 
}

export const ManageCustomersSection: React.FC<ManageCustomersSectionProps> = ({
  customers,
  vehicles,
  loadingCustomers,
  errorCustomers,
  loadingVehicles,
  errorVehicles,
  fetchCustomers,
  fetchVehicles, 
}) => {
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [displaySearchedCustomer, setDisplaySearchedCustomer] = useState<CustomerProfile | null>(null);
  const [viewingCustomerVehiclesId, setViewingCustomerVehiclesId] = useState<string | number | null>(null);

  const filteredCustomers = customers.filter(customer => {
    return searchTerm
      ? customer.id.toString().includes(searchTerm) ||
        customer.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        customer.lastName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        customer.email.toLowerCase().includes(searchTerm.toLowerCase())
      : true;
  });

  const handleSearchCustomerById = () => {
    const foundCustomer = customers.find(customer => customer.id.toString() === searchTerm);
    setDisplaySearchedCustomer(foundCustomer || null);
    if (!foundCustomer && searchTerm) {
      console.log(`Customer with ID ${searchTerm} not found.`);
    }
  };

  const handleViewCustomerVehicles = (customerId: string | number) => {
    setViewingCustomerVehiclesId(customerId);
    setDisplaySearchedCustomer(null); 
    setSearchTerm(''); 
  };

  const handleBackToCustomers = () => {
    setViewingCustomerVehiclesId(null);
    setDisplaySearchedCustomer(null);
    setSearchTerm('');
    fetchCustomers(); 
  };

  useEffect(() => {
    setDisplaySearchedCustomer(null);
  }, [searchTerm]);

  if (viewingCustomerVehiclesId) {
    const customerForVehicles = customers.find(c => c.id.toString() === viewingCustomerVehiclesId);
    const customerVehicles = vehicles.filter(car => car.ownerId?.toString() === viewingCustomerVehiclesId.toString());

    return (
      <div>
        <h2 className="text-2xl font-semibold text-gray-700 mb-4">Vehicles for {customerForVehicles?.username || 'Selected Customer'}</h2>
        {loadingVehicles ? (
          <p className="text-gray-700">Loading customer vehicles...</p>
        ) : errorVehicles ? (
          <p className="text-red-600">Error: {errorVehicles}</p>
        ) : customerVehicles.length === 0 ? (
          <p className="text-gray-700">No vehicles found for this customer.</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {customerVehicles.map((car) => (
              <div key={car.id} className="bg-gray-50 p-4 rounded-md shadow-sm">
                <p className="font-bold text-gray-900 text-lg">
                  {car.make} {car.model} ({car.productionYear})
                </p>
                <p className="text-gray-800 text-sm">VIN: {car.vin}</p>
                <p className="text-gray-800 text-sm">Price: {car.price} PLN</p>
                <p className="text-gray-800 text-sm">Condition: {car.vehicleCondition}</p>
                <p className="text-gray-800 text-sm">Availability: {car.availability}</p>
              </div>
            ))}
          </div>
        )}
        <button
          onClick={handleBackToCustomers}
          className="mt-6 bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded transition duration-200"
        >
          Back to All Customers
        </button>
      </div>
    );
  }

  return (
    <div>
      <h2 className="text-2xl font-semibold text-gray-700 mb-4">Manage Customer Profiles</h2>
      <div className="flex space-x-2 mb-4">
        <input
          type="text"
          placeholder="Search Customer by ID, Name, or Email"
          className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <button
          onClick={handleSearchCustomerById}
          className="bg-purple-600 hover:bg-purple-700 text-white font-bold py-2 px-4 rounded transition duration-200"
        >
          Search
        </button>
      </div>

      {loadingCustomers ? (
        <p className="text-gray-700">Loading customers...</p>
      ) : errorCustomers ? (
        <p className="text-red-600">Error: {errorCustomers}</p>
      ) : (
        <>
          {displaySearchedCustomer ? (
            <div className="bg-gray-50 p-4 rounded-md shadow-sm mb-4">
              <h3 className="text-xl font-semibold text-gray-700 mb-2">Search Result</h3>
              <p className="font-bold text-gray-900 text-lg">{displaySearchedCustomer.firstName} {displaySearchedCustomer.lastName}</p>
              <p className="text-gray-800 text-sm">ID: {displaySearchedCustomer.id}</p>
              <p className="text-gray-800 text-sm">Username: {displaySearchedCustomer.username}</p>
              <p className="text-gray-800 text-sm">Email: {displaySearchedCustomer.email}</p>
              <p className="text-gray-800 text-sm">Phone: {displaySearchedCustomer.phoneNumber || 'N/A'}</p>
              <p className="text-gray-800 text-sm">Address: {displaySearchedCustomer.address.city}, {displaySearchedCustomer.address.country}</p>
              <button
                onClick={() => handleViewCustomerVehicles(displaySearchedCustomer.id)}
                className="mt-2 bg-purple-500 hover:bg-purple-600 text-white font-bold py-1 px-3 rounded text-xs transition duration-200"
              >
                View Vehicles
              </button>
            </div>
          ) : (
            searchTerm && <p className="text-red-600">No customer found matching "{searchTerm}".</p>
          )}

          <h3 className="text-xl font-semibold text-gray-700 mt-6 mb-4">Customer List</h3>
          {filteredCustomers.length === 0 ? (
            <p className="text-gray-700">No customer profiles found matching current criteria. (Once connected to API, data will appear here)</p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {filteredCustomers.map((customer) => (
                <div key={customer.id} className="bg-gray-50 p-4 rounded-md shadow-sm">
                  <p className="font-bold text-gray-900 text-lg">{customer.firstName} {customer.lastName}</p>
                  <p className="text-gray-800 text-sm">Username: {customer.username}</p>
                  <p className="text-gray-800 text-sm">Email: {customer.email}</p>
                  <p className="text-gray-800 text-sm">Phone: {customer.phoneNumber || 'N/A'}</p>
                  <p className="text-gray-800 text-sm">Address: {customer.address.city}, {customer.address.country}</p>
                  <button
                    onClick={() => handleViewCustomerVehicles(customer.id)}
                    className="mt-2 bg-purple-500 hover:bg-purple-600 text-white font-bold py-1 px-3 rounded text-xs transition duration-200"
                  >
                    View Vehicles
                  </button>
                </div>
              ))}
            </div>
          )}
        </>
      )}
    </div>
  );
};
