import React from "react";

type EditVehicleFormProps = {
  editForm: {
    make: string;
    model: string;
    productionYear: number;
    price: number;
    vehicleCondition: "NEW" | "USED";
    availability: "AVAILABLE" | "UNAVAILABLE" | "SOLD";
    active: boolean;
  };
  editError: string | null;
  editSuccess: boolean;
  editLoading: boolean;
  onChange: (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => void;
  onSubmit: (e: React.FormEvent) => void;
  onCancel: () => void;
  vehicleConditionOptions: { value: string; label: string }[];
  availabilityOptions: { value: string; label: string }[];
};

export const EditVehicleForm: React.FC<EditVehicleFormProps> = ({
  editForm,
  editError,
  editSuccess,
  editLoading,
  onChange,
  onSubmit,
  onCancel,
  vehicleConditionOptions,
  availabilityOptions,
}) => (
  <div className="max-w-xl mx-auto bg-white rounded-xl shadow-lg border border-purple-200 p-8">
    <h3 className="text-2xl font-bold text-purple-700 mb-6 text-center">
      Edit Vehicle
    </h3>
    {editError && <p className="text-red-600 mb-2">{editError}</p>}
    {editSuccess && (
      <p className="text-green-600 mb-2">Vehicle updated successfully!</p>
    )}
    <form onSubmit={onSubmit} className="space-y-4">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-gray-700 text-sm font-bold mb-1">
            Make
          </label>
          <input
            type="text"
            name="make"
            value={editForm.make}
            onChange={onChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 focus:outline-none focus:shadow-outline"
            required
          />
        </div>
        <div>
          <label className="block text-gray-700 text-sm font-bold mb-1">
            Model
          </label>
          <input
            type="text"
            name="model"
            value={editForm.model}
            onChange={onChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 focus:outline-none focus:shadow-outline"
            required
          />
        </div>
        <div>
          <label className="block text-gray-700 text-sm font-bold mb-1">
            Year
          </label>
          <input
            type="number"
            name="productionYear"
            value={editForm.productionYear}
            onChange={onChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 focus:outline-none focus:shadow-outline"
            required
          />
        </div>
        <div>
          <label className="block text-gray-700 text-sm font-bold mb-1">
            Price (PLN)
          </label>
          <input
            type="number"
            name="price"
            value={editForm.price}
            onChange={onChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 focus:outline-none focus:shadow-outline"
            required
          />
        </div>
        <div>
          <label className="block text-gray-700 text-sm font-bold mb-1">
            Condition
          </label>
          <select
            name="vehicleCondition"
            value={editForm.vehicleCondition}
            onChange={onChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 focus:outline-none focus:shadow-outline"
            required
          >
            {vehicleConditionOptions.map((opt) => (
              <option key={opt.value} value={opt.value}>
                {opt.label}
              </option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-gray-700 text-sm font-bold mb-1">
            Availability
          </label>
          <select
            name="availability"
            value={editForm.availability}
            onChange={onChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 focus:outline-none focus:shadow-outline"
            required
          >
            {availabilityOptions.map((opt) => (
              <option key={opt.value} value={opt.value}>
                {opt.label}
              </option>
            ))}
          </select>
        </div>
        <div className="flex items-center col-span-2">
          <input
            type="checkbox"
            name="active"
            checked={editForm.active}
            onChange={onChange}
            className="mr-2"
          />
          <label className="text-gray-700 text-sm font-bold">Active</label>
        </div>
      </div>
      <div className="flex justify-end space-x-4 mt-6">
        <button
          type="button"
          onClick={onCancel}
          className="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded transition duration-200"
        >
          Cancel
        </button>
        <button
          type="submit"
          disabled={editLoading}
          className="bg-purple-600 hover:bg-purple-700 text-white font-bold py-2 px-4 rounded transition duration-200"
        >
          {editLoading ? "Saving..." : "Save Changes"}
        </button>
      </div>
    </form>
  </div>
);