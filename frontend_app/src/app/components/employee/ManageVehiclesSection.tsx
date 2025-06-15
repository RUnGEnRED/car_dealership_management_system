import React, { useState, useEffect } from 'react';
import { Car } from '../../types/car';

interface ManageVehiclesSectionProps {
  vehicles: Car[];
  loading: boolean;
  error: string | null;
  fetchVehicles: () => Promise<void>;
}

export const ManageVehiclesSection: React.FC<ManageVehiclesSectionProps> = ({
  vehicles,
  loading,
  error,
  fetchVehicles,
}) => {
  const [filterAvailableOnly, setFilterAvailableOnly] = useState<boolean>(false);
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [displaySearchedVehicle, setDisplaySearchedVehicle] = useState<Car | null>(null);
  const [showAddVehicleForm, setShowAddVehicleForm] = useState<boolean>(false);

  const filteredVehicles = vehicles.filter(car => {
    const matchesAvailability = filterAvailableOnly ? car.availability === 'AVAILABLE' : true;
    const matchesSearchTerm = searchTerm
      ? car.id.toString().includes(searchTerm) ||
        car.make.toLowerCase().includes(searchTerm.toLowerCase()) ||
        car.model.toLowerCase().includes(searchTerm.toLowerCase())
      : true;
    return matchesAvailability && matchesSearchTerm;
  });

  const handleSearchVehicleById = () => {
    const foundVehicle = vehicles.find(car => car.id.toString() === searchTerm);
    setDisplaySearchedVehicle(foundVehicle || null);
    if (!foundVehicle && searchTerm) {
      console.log(`Vehicle with ID ${searchTerm} not found.`);
    }
  };

  const handleAddVehicleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Mock: Adding new vehicle...');
    setShowAddVehicleForm(false); 
    setSearchTerm('');
    setFilterAvailableOnly(false); 
    alert('Mock: New vehicle added!');
  };

  useEffect(() => {
    setDisplaySearchedVehicle(null);
  }, [filterAvailableOnly, searchTerm]);


  if (loading) {
    return <p className="text-gray-700">Loading vehicles...</p>;
  }
  if (error) {
    return <p className="text-red-600">Error: {error}</p>;
  }

  return (
    <div>
      <h2 className="text-2xl font-semibold text-gray-700 mb-4">Manage Vehicles</h2>
      <div className="mb-4 flex space-x-4">
        <button
          onClick={() => { setFilterAvailableOnly(true); setShowAddVehicleForm(false); setSearchTerm(''); setDisplaySearchedVehicle(null); }}
          className={`py-2 px-4 rounded transition duration-200 ${
            filterAvailableOnly && !showAddVehicleForm
              ? 'bg-purple-600 text-white font-semibold'
              : 'bg-gray-200 text-gray-800 hover:bg-gray-300'
          }`}
        >
          Show Available
        </button>
        <button
          onClick={() => { setFilterAvailableOnly(false); setShowAddVehicleForm(false); setSearchTerm(''); setDisplaySearchedVehicle(null); }}
          className={`py-2 px-4 rounded transition duration-200 ${
            !filterAvailableOnly && !showAddVehicleForm
              ? 'bg-purple-600 text-white font-semibold'
              : 'bg-gray-200 text-gray-800 hover:bg-gray-300'
          }`}
        >
          Show All
        </button>
        <button
          onClick={() => { setShowAddVehicleForm(true); setSearchTerm(''); setFilterAvailableOnly(false); setDisplaySearchedVehicle(null); }}
          className={`py-2 px-4 rounded transition duration-200 ${
            showAddVehicleForm
              ? 'bg-purple-600 text-white font-semibold'
              : 'bg-gray-200 text-gray-800 hover:bg-gray-300'
          }`}
        >
          Add New Vehicle
        </button>
      </div>

      {showAddVehicleForm ? (
        <div className="bg-gray-50 p-6 rounded-lg shadow-sm">
          <h3 className="text-xl font-semibold text-gray-700 mb-4">Add New Vehicle Form (Mock)</h3>
          <form onSubmit={handleAddVehicleSubmit} className="space-y-4">
            <div>
              <label htmlFor="make" className="block text-gray-700 text-sm font-bold mb-1">Make:</label>
              <input type="text" id="make" name="make" className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" placeholder="Toyota" required />
            </div>
            <div>
              <label htmlFor="model" className="block text-gray-700 text-sm font-bold mb-1">Model:</label>
              <input type="text" id="model" name="model" className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" placeholder="Camry" required />
            </div>
            <div>
              <label htmlFor="year" className="block text-gray-700 text-sm font-bold mb-1">Year:</label>
              <input type="number" id="year" name="year" className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" placeholder="2020" required />
            </div>
            <div>
              <label htmlFor="vin" className="block text-gray-700 text-sm font-bold mb-1">VIN:</label>
              <input type="text" id="vin" name="vin" className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" placeholder="ABC123XYZ..." required />
            </div>
            <div>
              <label htmlFor="price" className="block text-gray-700 text-sm font-bold mb-1">Price (PLN):</label>
              <input type="number" id="price" name="price" className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" placeholder="75000" required />
            </div>
            <div>
              <label htmlFor="condition" className="block text-gray-700 text-sm font-bold mb-1">Condition:</label>
              <input type="text" id="condition" name="condition" className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" placeholder="Excellent" required />
            </div>
            <div>
              <label htmlFor="availability" className="block text-gray-700 text-sm font-bold mb-1">Availability:</label>
              <select id="availability" name="availability" className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
                <option value="AVAILABLE">AVAILABLE</option>
                <option value="UNAVAILABLE">UNAVAILABLE</option>
                <option value="SOLD">SOLD</option>
              </select>
            </div>
            <div className="flex justify-end space-x-4 mt-6">
              <button type="button" onClick={() => setShowAddVehicleForm(false)} className="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded transition duration-200">
                  Cancel
              </button>
              <button type="submit" className="bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded transition duration-200">
                Add Vehicle (Mock)
              </button>
            </div>
          </form>
        </div>
      ) : (
        <>
          <div className="flex space-x-2 mb-4">
            <input
              type="text"
              placeholder="Search Vehicle by ID, Make, or Model"
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <button
              onClick={handleSearchVehicleById}
              className="bg-purple-600 hover:bg-purple-700 text-white font-bold py-2 px-4 rounded transition duration-200"
            >
              Search
            </button>
          </div>
          {displaySearchedVehicle ? (
            <div className="bg-gray-50 p-4 rounded-md shadow-sm mb-4">
              <h3 className="text-xl font-semibold text-gray-700 mb-2">Search Result</h3>
              <p className="font-bold text-gray-900 text-lg">
                {displaySearchedVehicle.make} {displaySearchedVehicle.model} ({displaySearchedVehicle.productionYear})
              </p>
              <p className="text-gray-800 text-sm">ID: {displaySearchedVehicle.id}</p>
              <p className="text-gray-800 text-sm">VIN: {displaySearchedVehicle.vin}</p>
              <p className="text-gray-800 text-sm">Price: {displaySearchedVehicle.price} PLN</p>
              <p className="text-gray-800 text-sm">Condition: {displaySearchedVehicle.vehicleCondition}</p>
              <p className="text-gray-800 text-sm">Availability: {displaySearchedVehicle.availability}</p>
            </div>
          ) : (
            searchTerm && <p className="text-red-600">No vehicle found matching "{searchTerm}".</p>
          )}

          <h3 className="text-xl font-semibold text-gray-700 mt-6 mb-4">Vehicle List</h3>
          {filteredVehicles.length === 0 ? (
            <p className="text-gray-700">No vehicles found matching current criteria. (Once connected to API, data will appear here)</p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {filteredVehicles.map((car) => (
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
        </>
      )}
    </div>
  );
};
