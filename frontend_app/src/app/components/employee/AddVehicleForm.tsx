import React from "react";

type AddVehicleFormProps = {
  addForm: {
    vin: string;
    make: string;
    model: string;
    productionYear: number | "";
    price: number | "";
    vehicleCondition: "NEW" | "USED";
  };
  vinError: string | null;
  addError: string | null;
  addSuccess: boolean;
  addLoading: boolean;
  onChange: (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => void;
  onSubmit: (e: React.FormEvent) => void;
  onCancel: () => void;
};

export const AddVehicleForm: React.FC<AddVehicleFormProps> = ({
  addForm,
  vinError,
  addError,
  addSuccess,
  addLoading,
  onChange,
  onSubmit,
  onCancel,
}) => (
  <div className="max-w-xl mx-auto bg-white rounded-xl shadow-lg border border-green-200 p-8 mb-8">
    <h3 className="text-2xl font-bold text-green-700 mb-6 text-center">
      Add New Vehicle
    </h3>
    {addError && <p className="text-red-600 mb-2">{addError}</p>}
    {vinError && <p className="text-red-600 mb-2">{vinError}</p>}
    {addSuccess && (
      <p className="text-green-600 mb-2">Vehicle added successfully!</p>
    )}
    <form onSubmit={onSubmit} className="space-y-4">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-gray-700 text-sm font-bold mb-1">
            VIN
          </label>
          <input
            type="text"
            name="vin"
            value={addForm.vin}
            onChange={onChange}
            className={`shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 focus:outline-none focus:shadow-outline ${
              vinError ? "border-red-500" : ""
            }`}
            required
            minLength={17}
            maxLength={17}
          />
        </div>
        <div>
          <label className="block text-gray-700 text-sm font-bold mb-1">
            Make
          </label>
          <input
            type="text"
            name="make"
            value={addForm.make}
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
            value={addForm.model}
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
            value={addForm.productionYear}
            onChange={onChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 focus:outline-none focus:shadow-outline"
            required
            min={1900}
            max={2100}
          />
        </div>
        <div>
          <label className="block text-gray-700 text-sm font-bold mb-1">
            Price (PLN)
          </label>
          <input
            type="number"
            name="price"
            value={addForm.price}
            onChange={onChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 focus:outline-none focus:shadow-outline"
            required
            min={0}
          />
        </div>
        <div>
          <label className="block text-gray-700 text-sm font-bold mb-1">
            Condition
          </label>
          <select
            name="vehicleCondition"
            value={addForm.vehicleCondition}
            onChange={onChange}
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 focus:outline-none focus:shadow-outline"
            required
          >
            <option value="NEW">New</option>
            <option value="USED">Used</option>
          </select>
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
          disabled={addLoading}
          className="bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded transition duration-200"
        >
          {addLoading ? "Adding..." : "Add Vehicle"}
        </button>
      </div>
    </form>
  </div>
);