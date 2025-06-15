import axios from "axios";
import React, { useState } from "react";
import { CustomerProfile } from "../../types/customer/customer";
import { Car } from "../../types/car";
import { useCustomerSearch } from "../../hooks/employee/useCustomerSearch";

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

const CUSTOMER_VEHICLES_URL = (id: string | number) =>
  `http://localhost:3001/api/customers/${id}/vehicles`;

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
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [selectedCustomer, setSelectedCustomer] = useState<CustomerProfile | null>(null);
  const [customerVehicles, setCustomerVehicles] = useState<Car[]>([]);
  const [loadingCustomerVehicles, setLoadingCustomerVehicles] = useState<boolean>(false);
  const [errorCustomerVehicles, setErrorCustomerVehicles] = useState<string | null>(null);
  const [showingVehicles, setShowingVehicles] = useState<boolean>(false);

  const {
    searchId,
    setSearchId,
    searchedCustomer,
    loadingSearchedCustomer,
    errorSearchedCustomer,
    handleSearchById,
    clearSearchedCustomer,
  } = useCustomerSearch();

  const filteredCustomers = customers.filter((customer) => {
    if (!searchTerm) return true;
    const lower = searchTerm.toLowerCase();
    return (
      customer.firstName.toLowerCase().includes(lower) ||
      customer.lastName.toLowerCase().includes(lower) ||
      customer.email.toLowerCase().includes(lower) ||
      customer.username.toLowerCase().includes(lower) ||
      (customer.address?.city?.toLowerCase().includes(lower) ?? false) ||
      (customer.address?.country?.toLowerCase().includes(lower) ?? false)
    );
  });

  const handleShowVehicles = async (customer: CustomerProfile) => {
    setSelectedCustomer(customer);
    setLoadingCustomerVehicles(true);
    setErrorCustomerVehicles(null);
    setShowingVehicles(true);
    try {
      const token = localStorage.getItem("jwt_token");
      const response = await axios.get<Car[]>(CUSTOMER_VEHICLES_URL(customer.id), {
        headers: token ? { Authorization: `Bearer ${token}` } : {},
      });
      setCustomerVehicles(response.data);
    } catch (err: any) {
      setErrorCustomerVehicles("Failed to fetch vehicles for this customer.");
    } finally {
      setLoadingCustomerVehicles(false);
    }
  };

  const handleBackToCustomers = () => {
    setShowingVehicles(false);
    setSelectedCustomer(null);
    setCustomerVehicles([]);
    setErrorCustomerVehicles(null);
  };

  return (
    <div>
      {!showingVehicles ? (
        <>
          <h2 className="text-2xl font-semibold text-gray-700 mb-4">Manage Customer Profiles</h2>
          <div className="flex space-x-2 mb-4">
            <input
              type="text"
              placeholder="Search by name, email, username, city, country"
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          <div>
            <h3 className="text-lg font-semibold text-purple-700 mb-2">Search by Customer ID</h3>
            <form onSubmit={handleSearchById} className="flex gap-2">
              <input
                type="text"
                placeholder="Customer ID"
                className="shadow appearance-none border border-purple-200 rounded py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:ring-2 focus:ring-purple-300 w-full"
                value={searchId}
                onChange={(e) => setSearchId(e.target.value)}
              />
              <button
                type="submit"
                className="px-4 py-2 rounded font-semibold bg-purple-600 text-white hover:bg-purple-700 transition-colors"
              >
                Search
              </button>
            </form>
            {loadingSearchedCustomer && (
              <p className="text-purple-700 mt-2">Searching...</p>
            )}
            {errorSearchedCustomer && (
              <p className="text-red-600 mt-2">{errorSearchedCustomer}</p>
            )}
            {searchedCustomer && (
              <div className="bg-white mt-4 p-4 rounded-lg shadow border border-purple-200 relative">
                <button
                  className="absolute top-2 right-2 text-purple-400 hover:text-purple-700 text-xl font-bold"
                  onClick={clearSearchedCustomer}
                  title="Clear"
                  aria-label="Clear"
                >
                  ×
                </button>
                <p className="font-bold text-purple-900 text-lg">
                  {searchedCustomer.firstName} {searchedCustomer.lastName}
                </p>
                <p className="text-gray-800 text-sm">Username: {searchedCustomer.username}</p>
                <p className="text-gray-800 text-sm">Email: {searchedCustomer.email}</p>
                <p className="text-gray-800 text-sm">Phone: {searchedCustomer.phoneNumber || "N/A"}</p>
                <p className="text-gray-800 text-sm">
                  Address: {searchedCustomer.address.city}, {searchedCustomer.address.country}
                </p>
                <button
                  className="mt-3 px-4 py-2 rounded font-semibold bg-purple-100 text-purple-800 hover:bg-purple-200 transition-colors border border-purple-300"
                  onClick={() => handleShowVehicles(searchedCustomer)}
                >
                  Show Vehicles
                </button>
              </div>
            )}
          </div>
          <h3 className="text-xl font-semibold text-gray-700 mt-6 mb-4">Customer List</h3>
          {loadingCustomers ? (
            <p className="text-gray-700">Loading customers...</p>
          ) : errorCustomers ? (
            <p className="text-red-600">Error: {errorCustomers}</p>
          ) : filteredCustomers.length === 0 ? (
            <p className="text-gray-700">No customer profiles found matching current criteria.</p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {filteredCustomers.map((customer) => (
                <div
                  key={customer.id}
                  className="bg-white p-4 rounded-md shadow-sm border border-gray-200"
                >
                  <p className="font-bold text-gray-900 text-lg">
                    {customer.firstName} {customer.lastName}
                  </p>
                  <p className="text-gray-800 text-sm">Username: {customer.username}</p>
                  <p className="text-gray-800 text-sm">Email: {customer.email}</p>
                  <p className="text-gray-800 text-sm">Phone: {customer.phoneNumber || "N/A"}</p>
                  <p className="text-gray-800 text-sm">
                    Address: {customer.address.city}, {customer.address.country}
                  </p>
                  <button
                    className="mt-3 px-4 py-2 rounded font-semibold bg-purple-100 text-purple-800 hover:bg-purple-200 transition-colors border border-purple-300"
                    onClick={() => handleShowVehicles(customer)}
                  >
                    Show Vehicles
                  </button>
                </div>
              ))}
            </div>
          )}
        </>
      ) : (
        <div className="max-w-2xl mx-auto bg-white rounded-xl shadow-lg border border-purple-200 p-8 mt-4">
          <button
            className="mb-6 px-4 py-2 rounded font-semibold bg-purple-100 text-purple-800 hover:bg-purple-200 transition-colors border border-purple-300"
            onClick={handleBackToCustomers}
          >
            ← Back to Customers
          </button>
          <h4 className="text-2xl font-bold text-purple-700 mb-6 text-center">
            Vehicles of {selectedCustomer?.firstName} {selectedCustomer?.lastName}
          </h4>
          {loadingCustomerVehicles ? (
            <div className="flex justify-center items-center h-32">
              <svg className="animate-spin h-8 w-8 text-purple-500" viewBox="0 0 24 24">
                <circle
                  className="opacity-25"
                  cx="12"
                  cy="12"
                  r="10"
                  stroke="currentColor"
                  strokeWidth="4"
                  fill="none"
                />
                <path
                  className="opacity-75"
                  fill="currentColor"
                  d="M4 12a8 8 0 018-8v8z"
                />
              </svg>
            </div>
          ) : errorCustomerVehicles ? (
            <p className="text-red-600">{errorCustomerVehicles}</p>
          ) : customerVehicles.length === 0 ? (
            <p className="text-gray-600 text-center">No vehicles found for this customer.</p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {customerVehicles.map((car) => (
                <div
                  key={car.id}
                  className="border border-purple-300 rounded-lg p-6 bg-purple-50 shadow transition hover:shadow-lg"
                >
                  <div className="flex items-center justify-between mb-2">
                    <span className="inline-block px-3 py-1 bg-purple-200 text-purple-900 rounded-full text-xs font-semibold uppercase tracking-wide">
                      {car.vehicleCondition === "NEW" ? "New" : "Used"}
                    </span>
                    <span className={`inline-block px-3 py-1 rounded-full text-xs font-semibold
                      ${car.availability === "AVAILABLE"
                        ? "bg-emerald-200 text-emerald-900"
                        : "bg-red-200 text-red-900"}
                    `}>
                      {car.availability === "AVAILABLE" ? "Available" : "Unavailable"}
                    </span>
                  </div>
                  <h5 className="font-bold text-purple-900 text-lg mb-1">
                    {car.make} {car.model}
                  </h5>
                  <p className="text-gray-700 text-sm mb-2">
                    <span className="font-semibold">Year:</span> {car.productionYear}
                  </p>
                  <div className="mb-2">
                    <span className="font-semibold text-gray-700">VIN:</span>{" "}
                    <span className="font-mono text-gray-800">{car.vin}</span>
                  </div>
                  <div className="mb-2">
                    <span className="font-semibold text-gray-700">Price:</span>{" "}
                    <span className="text-gray-800">{car.price} PLN</span>
                  </div>
                  <div>
                    <span className="font-semibold text-gray-700">Active:</span>{" "}
                    <span className={car.active ? "text-emerald-700" : "text-red-700"}>
                      {car.active ? "Yes" : "No"}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
};
