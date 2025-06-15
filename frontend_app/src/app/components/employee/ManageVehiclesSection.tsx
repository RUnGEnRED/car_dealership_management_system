import React, { useState } from "react";
import axios from "axios";
import { Car } from "../../types/car";
import { AddVehicleForm } from "./AddVehicleForm";
import { EditVehicleForm } from "./EditVehicleForm";

interface ManageVehiclesSectionProps {
  vehicles: Car[];
  loading: boolean;
  error: string | null;
  fetchVehicles: () => Promise<void>;
}

type EditVehicleFormType = Omit<Car, "id" | "vin" | "ownerId"> & {
  vehicleCondition: "NEW" | "USED";
  availability: "AVAILABLE" | "UNAVAILABLE" | "SOLD";
};
type AddVehicleFormType = {
  vin: string;
  make: string;
  model: string;
  productionYear: number | "";
  price: number | "";
  vehicleCondition: "NEW" | "USED";
};

const vehicleConditionOptions = [
  { value: "NEW", label: "New" },
  { value: "USED", label: "Used" },
];
const availabilityOptions = [
  { value: "AVAILABLE", label: "Available" },
  { value: "UNAVAILABLE", label: "Unavailable" },
  { value: "SOLD", label: "Sold" },
];

export const ManageVehiclesSection: React.FC<ManageVehiclesSectionProps> = ({
  vehicles,
  loading,
  error,
  fetchVehicles,
}) => {
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [editingVehicle, setEditingVehicle] = useState<Car | null>(null);
  const [editForm, setEditForm] = useState<EditVehicleFormType | null>(null);
  const [editLoading, setEditLoading] = useState<boolean>(false);
  const [editError, setEditError] = useState<string | null>(null);
  const [editSuccess, setEditSuccess] = useState<boolean>(false);
  const [showAvailableOnly, setShowAvailableOnly] = useState<boolean>(false);
  const [availableVehicles, setAvailableVehicles] = useState<Car[] | null>(null);
  const [loadingAvailable, setLoadingAvailable] = useState<boolean>(false);
  const [errorAvailable, setErrorAvailable] = useState<string | null>(null);

  const [showAddForm, setShowAddForm] = useState<boolean>(false);
  const [addForm, setAddForm] = useState<AddVehicleFormType>({
    vin: "",
    make: "",
    model: "",
    productionYear: new Date().getFullYear(),
    price: 0,
    vehicleCondition: "NEW",
  });
  const [addLoading, setAddLoading] = useState<boolean>(false);
  const [addError, setAddError] = useState<string | null>(null);
  const [addSuccess, setAddSuccess] = useState<boolean>(false);
  const [vinError, setVinError] = useState<string | null>(null);

  const [searchId, setSearchId] = useState<string>("");
  const [searchedVehicle, setSearchedVehicle] = useState<Car | null>(null);
  const [loadingSearchedVehicle, setLoadingSearchedVehicle] = useState<boolean>(false);
  const [errorSearchedVehicle, setErrorSearchedVehicle] = useState<string | null>(null);

  const filteredVehicles =
    showAvailableOnly && availableVehicles
      ? availableVehicles.filter((car) => {
          if (!searchTerm) return true;
          const lower = searchTerm.toLowerCase();
          return (
            car.make.toLowerCase().includes(lower) ||
            car.model.toLowerCase().includes(lower) ||
            car.vin.toLowerCase().includes(lower) ||
            car.id.toString().includes(lower)
          );
        })
      : vehicles.filter((car) => {
          if (!searchTerm) return true;
          const lower = searchTerm.toLowerCase();
          return (
            car.make.toLowerCase().includes(lower) ||
            car.model.toLowerCase().includes(lower) ||
            car.vin.toLowerCase().includes(lower) ||
            car.id.toString().includes(lower)
          );
        });

  const handleEditClick = (car: Car) => {
    setEditingVehicle(car);
    setEditForm({
      make: car.make,
      model: car.model,
      productionYear: car.productionYear,
      price: car.price,
      vehicleCondition: car.vehicleCondition as "NEW" | "USED",
      availability: car.availability as "AVAILABLE" | "UNAVAILABLE" | "SOLD",
      active: car.active,
    });
    setEditError(null);
    setEditSuccess(false);
  };

  const handleEditFormChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    if (!editForm) return;
    const { name, value, type } = e.target;
    if (type === "checkbox") {
      const checked = (e.target as HTMLInputElement).checked;
      setEditForm({
        ...editForm,
        [name]: checked,
      });
    } else if (type === "number") {
      setEditForm({
        ...editForm,
        [name]: Number(value),
      });
    } else if (name === "vehicleCondition") {
      setEditForm({
        ...editForm,
        vehicleCondition: value as "NEW" | "USED",
      });
    } else if (name === "availability") {
      setEditForm({
        ...editForm,
        availability: value as "AVAILABLE" | "UNAVAILABLE" | "SOLD",
      });
    } else {
      setEditForm({
        ...editForm,
        [name]: value,
      });
    }
  };

  const handleEditFormSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!editingVehicle || !editForm) return;
    setEditLoading(true);
    setEditError(null);
    setEditSuccess(false);
    try {
      const token = localStorage.getItem("jwt_token");
      await axios.put(
        `http://localhost:3001/api/vehicles/${editingVehicle.id}`,
        editForm,
        {
          headers: token ? { Authorization: `Bearer ${token}` } : {},
        }
      );
      setEditSuccess(true);
      setTimeout(() => {
        setEditingVehicle(null);
        setEditForm(null);
        setEditSuccess(false);
      }, 1000);
      await fetchVehicles();
      if (showAvailableOnly) {
        await handleShowAvailableVehicles();
      }
    } catch (err: any) {
      setEditError("Failed to update vehicle.");
    } finally {
      setEditLoading(false);
    }
  };

  const handleEditCancel = () => {
    setEditingVehicle(null);
    setEditForm(null);
    setEditError(null);
    setEditSuccess(false);
  };

  const handleAddFormChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value, type } = e.target;
    if (name === "vin") {
      if (value.length !== 17) {
        setVinError("VIN must be exactly 17 characters.");
      } else {
        setVinError(null);
      }
    }
    setAddForm((prev) => ({
      ...prev,
      [name]: type === "number" ? (value === "" ? "" : Number(value)) : value,
    }));
  };

  const handleAddVehicle = async (e: React.FormEvent) => {
    e.preventDefault();
    if (addForm.vin.length !== 17) {
      setVinError("VIN must be exactly 17 characters.");
      return;
    }
    setAddLoading(true);
    setAddError(null);
    setAddSuccess(false);
    try {
      const token = localStorage.getItem("jwt_token");
      await axios.post("http://localhost:3001/api/vehicles", addForm, {
        headers: token ? { Authorization: `Bearer ${token}` } : {},
      });
      setAddSuccess(true);
      setAddForm({
        vin: "",
        make: "",
        model: "",
        productionYear: new Date().getFullYear(),
        price: 0,
        vehicleCondition: "NEW",
      });
      setTimeout(() => {
        setShowAddForm(false);
        setAddSuccess(false);
      }, 1000);
      await fetchVehicles();
      if (showAvailableOnly) {
        await handleShowAvailableVehicles();
      }
    } catch (err: any) {
      setAddError("Failed to add vehicle.");
    } finally {
      setAddLoading(false);
    }
  };

  const handleShowAvailableVehicles = async () => {
    setShowAvailableOnly(true);
    setLoadingAvailable(true);
    setErrorAvailable(null);
    try {
      const token = localStorage.getItem("jwt_token");
      const response = await axios.get<Car[]>(
        "http://localhost:3001/api/vehicles/available",
        {
          headers: token ? { Authorization: `Bearer ${token}` } : {},
        }
      );
      setAvailableVehicles(response.data);
    } catch (err: any) {
      setErrorAvailable("Failed to fetch available vehicles.");
    } finally {
      setLoadingAvailable(false);
    }
  };

  const handleShowAllVehicles = () => {
    setShowAvailableOnly(false);
    setErrorAvailable(null);
    setAvailableVehicles(null);
  };

  const handleSearchById = async (e: React.FormEvent) => {
    e.preventDefault();
    setSearchedVehicle(null);
    setErrorSearchedVehicle(null);
    if (!searchId.trim()) return;
    setLoadingSearchedVehicle(true);
    try {
      const token = localStorage.getItem("jwt_token");
      const response = await axios.get<Car>(
        `http://localhost:3001/api/vehicles/${searchId.trim()}`,
        {
          headers: token ? { Authorization: `Bearer ${token}` } : {},
        }
      );
      setSearchedVehicle(response.data);
    } catch (err: any) {
      setErrorSearchedVehicle("Vehicle not found.");
    } finally {
      setLoadingSearchedVehicle(false);
    }
  };

  const handleClearSearchedVehicle = () => {
    setSearchedVehicle(null);
    setSearchId("");
    setErrorSearchedVehicle(null);
  };

  return (
    <div>
      <h2 className="text-2xl font-semibold text-gray-700 mb-4">
        Manage Vehicles
      </h2>
      <div className="flex flex-col md:flex-row gap-6 mb-4 items-start">
        <div className="md:w-2/3 w-full flex items-center gap-2">
          <input
            type="text"
            placeholder="Search by make, model, VIN or ID"
            className="shadow appearance-none border rounded py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline w-full"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <div
          className="md:w-1/3 w-full bg-purple-50 rounded-xl p-4 shadow border border-purple-100 flex flex-col gap-2"
          style={{ minWidth: 320, maxWidth: 400 }}
        >
          <form onSubmit={handleSearchById} className="flex gap-2">
            <input
              type="text"
              placeholder="Search vehicle by ID"
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
          {loadingSearchedVehicle && (
            <p className="text-purple-700 mt-2">Searching...</p>
          )}
          {errorSearchedVehicle && (
            <p className="text-red-600 mt-2">{errorSearchedVehicle}</p>
          )}
          {searchedVehicle && (
            <div className="bg-white mt-2 p-3 rounded-lg shadow border border-purple-200 relative">
              <button
                className="absolute top-2 right-2 text-purple-400 hover:text-purple-700 text-xl font-bold"
                onClick={handleClearSearchedVehicle}
                title="Clear"
                aria-label="Clear"
              >
                ×
              </button>
              <p className="font-bold text-purple-900 text-lg">
                {searchedVehicle.make} {searchedVehicle.model} ({searchedVehicle.productionYear})
              </p>
              <p className="text-gray-800 text-sm">ID: {searchedVehicle.id}</p>
              <p className="text-gray-800 text-sm">VIN: {searchedVehicle.vin}</p>
              <p className="text-gray-800 text-sm">Price: {searchedVehicle.price} PLN</p>
              <p className="text-gray-800 text-sm">Condition: {searchedVehicle.vehicleCondition === "NEW" ? "New" : "Used"}</p>
              <p className="text-gray-800 text-sm">
                Status: <span className="font-semibold">{searchedVehicle.availability === "SOLD" ? "Sold" : searchedVehicle.availability.charAt(0) + searchedVehicle.availability.slice(1).toLowerCase()}</span>
              </p>
              <p className="text-gray-800 text-sm">
                Active: <span className="font-semibold">{searchedVehicle.active ? "Yes" : "No"}</span>
              </p>
              <button
                className="mt-3 px-4 py-2 rounded font-semibold bg-yellow-100 text-yellow-800 hover:bg-yellow-200 transition-colors border border-yellow-300"
                onClick={() => handleEditClick(searchedVehicle)}
              >
                Edit
              </button>
            </div>
          )}
        </div>
      </div>
      <div className="flex flex-wrap gap-2 mb-4 items-center">
        <button
          onClick={handleShowAllVehicles}
          className={`py-2 px-4 rounded transition duration-200 ${
            !showAvailableOnly
              ? "bg-purple-600 text-white font-semibold"
              : "bg-gray-200 text-gray-800 hover:bg-gray-300"
          }`}
        >
          Show All Vehicles
        </button>
        <button
          onClick={handleShowAvailableVehicles}
          className={`py-2 px-4 rounded transition duration-200 ${
            showAvailableOnly
              ? "bg-purple-600 text-white font-semibold"
              : "bg-gray-200 text-gray-800 hover:bg-gray-300"
          }`}
        >
          Show Only Available
        </button>
        <button
          onClick={() => setShowAddForm((v) => !v)}
          className={`flex items-center gap-2 py-2 px-5 rounded-lg transition duration-200 font-semibold shadow-sm border-2 border-green-500
      ${
        showAddForm
          ? "bg-white text-green-700 border-green-700"
          : "bg-green-500 text-white hover:bg-green-600"
      }
    `}
          style={{ marginLeft: "auto" }}
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-5 w-5"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            strokeWidth={2}
          >
            <path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" />
          </svg>
          {showAddForm ? "Cancel Adding" : "Add Vehicle"}
        </button>
      </div>

      {showAddForm && (
        <AddVehicleForm
          addForm={addForm}
          vinError={vinError}
          addError={addError}
          addSuccess={addSuccess}
          addLoading={addLoading}
          onChange={handleAddFormChange}
          onSubmit={handleAddVehicle}
          onCancel={() => setShowAddForm(false)}
        />
      )}

      {editingVehicle && editForm ? (
        <EditVehicleForm
          editForm={editForm}
          editError={editError}
          editSuccess={editSuccess}
          editLoading={editLoading}
          onChange={handleEditFormChange}
          onSubmit={handleEditFormSubmit}
          onCancel={handleEditCancel}
          vehicleConditionOptions={vehicleConditionOptions}
          availabilityOptions={availabilityOptions}
        />
      ) : (
        <>
          <h3 className="text-xl font-semibold text-purple-700 mb-4">
            Vehicle List
          </h3>
          {loading || (showAvailableOnly && loadingAvailable) ? (
            <p className="text-gray-700">Loading vehicles...</p>
          ) : error || (showAvailableOnly && errorAvailable) ? (
            <p className="text-red-600">
              {errorAvailable ? errorAvailable : error}
            </p>
          ) : filteredVehicles.length === 0 ? (
            <p className="text-gray-700">
              No vehicles found matching current criteria.
            </p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {filteredVehicles.map((car) => (
                <div
                  key={car.id}
                  className="bg-white p-4 rounded-md shadow-sm border border-gray-200"
                >
                  <p className="font-bold text-purple-900 text-lg">
                    {car.make} {car.model} <span className="text-gray-500">({car.productionYear})</span>
                  </p>
                  <p className="text-gray-800 text-sm">VIN: <span className="font-mono">{car.vin}</span></p>
                  <p className="text-gray-800 text-sm">Price: {car.price} PLN</p>
                  <p className="text-gray-800 text-sm">Condition: {car.vehicleCondition === "NEW" ? "New" : "Used"}</p>
                  <p className="text-gray-800 text-sm">
                    Status: <span className="font-semibold">{car.availability === "SOLD" ? "Sold" : car.availability.charAt(0) + car.availability.slice(1).toLowerCase()}</span>
                  </p>
                  <p className="text-gray-800 text-sm">
                    Active: <span className="font-semibold">{car.active ? "Yes" : "No"}</span>
                  </p>
                  <button
                    className="mt-3 px-4 py-2 rounded font-semibold bg-yellow-100 text-yellow-800 hover:bg-yellow-200 transition-colors border border-yellow-300"
                    onClick={() => handleEditClick(car)}
                  >
                    Edit
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
