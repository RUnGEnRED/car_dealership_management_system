import React from 'react';
import { Car } from '../types/car'; 

/**
 * This component displays a list of cars currently owned by the authenticated customer.
 * It handles loading states and errors when fetching the customer's vehicle data.
 */

interface MyCarsSectionProps {
  myCars: Car[];
  loading: boolean;
  error: string | null;
}

export const MyCarsSection: React.FC<MyCarsSectionProps> = ({ myCars, loading, error }) => {
  if (loading) {
    return <p className="text-gray-700">Loading your cars...</p>;
  }
  if (error) {
    return <p className="text-red-600">Error: {error}</p>;
  }
  if (myCars.length === 0) {
    return <p className="text-gray-700">You don't own any cars yet.</p>;
  }

  return (
    <div>
      <h2 className="text-2xl font-semibold text-gray-700 mb-4">My Cars</h2>
      <p className="text-gray-600 mb-4">List of cars you currently own:</p>
      {myCars.map((car) => (
        <div key={car.id} className="bg-gray-50 p-4 rounded-md mt-4 shadow-sm">
          <p className="font-bold text-gray-900 text-lg">
            {car.make} {car.model}
          </p>
          <p className="text-gray-800 text-sm">
            Production Year: {car.productionYear}, VIN: {car.vin}
          </p>
          <p className="text-gray-800 text-sm">
            Condition: {car.vehicleCondition}
          </p>
        </div>
      ))}
    </div>
  );
};