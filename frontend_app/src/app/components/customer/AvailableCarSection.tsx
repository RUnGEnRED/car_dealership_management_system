import React from "react";
import { AvailableCarsSectionProps } from "../../types/car";

/**
 * This component displays a list of cars available to the customer, allowing them to filter
 * by availability and initiate purchase requests for available vehicles.
 */

export const AvailableCarsSection: React.FC<AvailableCarsSectionProps> = ({
  cars,
  loading,
  error,
  filterAvailableOnly,
  setFilterAvailableOnly,
  setSelectedVehicleId,
  setActiveRequestForm,
  setActiveSection,
  resetRequestForm,
}) => {
  if (loading) {
    return <p className='text-gray-700'>Loading cars...</p>;
  }
  if (error) {
    return <p className='text-red-600'>Error: {error}</p>;
  }

  return (
    <div>
      <h2 className='text-2xl font-semibold text-gray-700 mb-4'>
        Available Cars
      </h2>
      <div className='mb-4 flex space-x-4'>
        <button
          onClick={() => setFilterAvailableOnly(true)}
          className={`py-2 px-4 rounded transition duration-200 ${
            filterAvailableOnly
              ? "bg-purple-600 text-white font-semibold"
              : "bg-gray-200 text-gray-800 hover:bg-gray-300"
          }`}>
          Show Available
        </button>
        <button
          onClick={() => setFilterAvailableOnly(false)}
          className={`py-2 px-4 rounded transition duration-200 ${
            !filterAvailableOnly
              ? "bg-purple-600 text-white font-semibold"
              : "bg-gray-200 text-gray-800 hover:bg-gray-300"
          }`}>
          Show All
        </button>
      </div>
      {cars.length === 0 ? (
        <p className='text-gray-700'>No cars matching criteria.</p>
      ) : (
        <p className='text-gray-600 mb-4'>List of cars:</p>
      )}

      {cars.map((car) => (
        <div key={car.id} className='bg-gray-50 p-4 rounded-md mt-4 shadow-sm'>
          <p className='font-bold text-gray-900 text-lg'>
            {car.make} {car.model}
          </p>
          <p className='text-gray-800 text-sm'>
            Production Year: {car.productionYear}, Price: {car.price} PLN,
            Condition: {car.vehicleCondition}
          </p>
          <div className='flex items-center justify-between text-gray-800 text-sm mt-2'>
            <p>Availability: {car.availability}</p>
            {car.availability === "AVAILABLE" ? (
              <button
                onClick={() => {
                  setSelectedVehicleId(car.id);
                  setActiveRequestForm("purchase");
                  setActiveSection("newRequest");
                  resetRequestForm();
                }}
                className='bg-purple-600 hover:bg-purple-700 text-white font-bold py-1 px-3 rounded text-xs transition duration-200'>
                Send Purchase Request
              </button>
            ) : (
              <span className='text-red-500 font-semibold text-xs'>
                Unavailable
              </span>
            )}
          </div>
          {car.vin && <p className='text-gray-800 text-xs'>VIN: {car.vin}</p>}
        </div>
      ))}
    </div>
  );
};
