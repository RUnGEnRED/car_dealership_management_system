"use client";

import React, { useState, useEffect } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useCustomerDashboard } from "../../../hooks/useCustomerDashboard";
import { useCustomerProfile } from "../../../hooks/useCustomerProfile";
import { useMyCars } from "../../../hooks/useMyCars";
import { useRequestForms } from "../../../hooks/useRequestForm";

export default function CustomerDashboardPage() {
  const [activeSection, setActiveSection] = useState<string>("availableCars");
  const [activeRequestForm, setActiveRequestForm] = useState<
    "none" | "service" | "inspection"
  >("none");
  const router = useRouter();

  const {
    cars,
    loading: carsLoading,
    error: carsError,
    filterAvailableOnly,
    setFilterAvailableOnly,
  } = useCustomerDashboard();

  const {
    profile,
    loading: profileLoading,
    error: profileError,
    editMode,
    formData,
    saveLoading,
    saveError,
    setEditMode,
    handleInputChange,
    handleAddressChange,
    handleSubmit,
    resetForm,
  } = useCustomerProfile();

  const { myCars, loading: myCarsLoading, error: myCarsError } = useMyCars();

  const {
    myCars: requestFormMyCars,
    myCarsLoading: requestFormMyCarsLoading,
    myCarsError: requestFormMyCarsError,
    selectedVehicleId,
    customerNotes,
    formLoading,
    formError,
    formSuccess,
    showSuccessPopup,
    setSelectedVehicleId,
    setCustomerNotes,
    submitServiceRequest,
    submitInspectionRequest,
    resetRequestForm,
    closeSuccessPopup,
  } = useRequestForms();

  useEffect(() => {
    if (activeSection !== "newRequest") {
      setActiveRequestForm("none");
      closeSuccessPopup();
    }
  }, [activeSection, closeSuccessPopup]);

  const handleLogout = () => {
    localStorage.removeItem("jwt_token");
    router.push("/");
  };

  const renderContent = () => {
    switch (activeSection) {
      case "availableCars":
        if (carsLoading) {
          return <p className='text-gray-700'>Loading cars...</p>;
        }
        if (carsError) {
          return <p className='text-red-600'>Error: {carsError}</p>;
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
              <div
                key={car.id}
                className='bg-gray-50 p-4 rounded-md mt-4 shadow-sm'>
                <p className='font-bold text-gray-900 text-lg'>
                  {car.make} {car.model}
                </p>
                <p className='text-gray-800 text-sm'>
                  Production Year: {car.productionYear}, Price: {car.price} PLN,
                  Condition: {car.vehicleCondition}
                </p>
                <p className='text-gray-800 text-sm'>
                  Availability: {car.availability}{" "}
                  {car.vin ? `(VIN: ${car.vin})` : ""}
                </p>
              </div>
            ))}
          </div>
        );
      case "profile":
        if (profileLoading) {
          return <p className='text-gray-700'>Loading profile...</p>;
        }
        if (profileError) {
          return <p className='text-red-600'>Error: {profileError}</p>;
        }
        if (!profile) {
          return <p className='text-gray-700'>No profile data available.</p>;
        }

        return (
          <div>
            <h2 className='text-2xl font-semibold text-gray-700 mb-4'>
              My Profile
            </h2>

            {editMode ? (
              <form onSubmit={handleSubmit} className='space-y-4'>
                {saveError && (
                  <p className='text-red-600 text-sm'>{saveError}</p>
                )}
                <div>
                  <label
                    htmlFor='firstName'
                    className='block text-gray-700 text-sm font-bold mb-1'>
                    First Name:
                  </label>
                  <input
                    type='text'
                    id='firstName'
                    name='firstName'
                    value={formData.firstName}
                    onChange={handleInputChange}
                    className='shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'
                    required
                  />
                </div>
                <div>
                  <label
                    htmlFor='lastName'
                    className='block text-gray-700 text-sm font-bold mb-1'>
                    Last Name:
                  </label>
                  <input
                    type='text'
                    id='lastName'
                    name='lastName'
                    value={formData.lastName}
                    onChange={handleInputChange}
                    className='shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'
                    required
                  />
                </div>
                <div>
                  <label
                    htmlFor='email'
                    className='block text-gray-700 text-sm font-bold mb-1'>
                    Email:
                  </label>
                  <input
                    type='email'
                    id='email'
                    name='email'
                    value={formData.email}
                    onChange={handleInputChange}
                    className='shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'
                    required
                  />
                </div>
                <div>
                  <label
                    htmlFor='phoneNumber'
                    className='block text-gray-700 text-sm font-bold mb-1'>
                    Phone Number:
                  </label>
                  <input
                    type='tel'
                    id='phoneNumber'
                    name='phoneNumber'
                    value={formData.phoneNumber}
                    onChange={handleInputChange}
                    className='shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'
                  />
                </div>
                <h3 className='text-lg font-semibold text-gray-700 mt-6 mb-2'>
                  Address:
                </h3>
                <div>
                  <label
                    htmlFor='street'
                    className='block text-gray-700 text-sm font-bold mb-1'>
                    Street:
                  </label>
                  <input
                    type='text'
                    id='street'
                    name='street'
                    value={formData.address.street}
                    onChange={handleAddressChange}
                    className='shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'
                  />
                </div>
                <div>
                  <label
                    htmlFor='city'
                    className='block text-gray-700 text-sm font-bold mb-1'>
                    City:
                  </label>
                  <input
                    type='text'
                    id='city'
                    name='city'
                    value={formData.address.city}
                    onChange={handleAddressChange}
                    className='shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'
                  />
                </div>
                <div>
                  <label
                    htmlFor='postalCode'
                    className='block text-gray-700 text-sm font-bold mb-1'>
                    Postal Code:
                  </label>
                  <input
                    type='text'
                    id='postalCode'
                    name='postalCode'
                    value={formData.address.postalCode}
                    onChange={handleAddressChange}
                    className='shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'
                  />
                </div>
                <div>
                  <label
                    htmlFor='country'
                    className='block text-gray-700 text-sm font-bold mb-1'>
                    Country:
                  </label>
                  <input
                    type='text'
                    id='country'
                    name='country'
                    value={formData.address.country}
                    onChange={handleAddressChange}
                    className='shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'
                  />
                </div>
                <div className='flex justify-end space-x-4 mt-6'>
                  <button
                    type='button'
                    onClick={() => {
                      setEditMode(false);
                      resetForm();
                    }}
                    className='bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded transition duration-200'
                    disabled={saveLoading}>
                    Cancel
                  </button>
                  <button
                    type='submit'
                    className='bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded transition duration-200'
                    disabled={saveLoading}>
                    {saveLoading ? "Saving..." : "Save Changes"}
                  </button>
                </div>
              </form>
            ) : (
              <div className='bg-gray-50 p-4 rounded-md mt-4 shadow-sm'>
                <p className='text-gray-800'>
                  <strong>Username:</strong> {profile.username}
                </p>
                <p className='text-gray-800'>
                  <strong>First Name:</strong> {profile.firstName}
                </p>
                <p className='text-gray-800'>
                  <strong>Last Name:</strong> {profile.lastName}
                </p>
                <p className='text-gray-800'>
                  <strong>Email:</strong> {profile.email}
                </p>
                <p className='text-gray-800'>
                  <strong>Phone Number:</strong> {profile.phoneNumber || "N/A"}
                </p>
                <p className='text-gray-800 mt-2'>
                  <strong>Address:</strong>
                </p>
                <p className='text-gray-800 pl-4'>
                  {profile.address.street || "N/A"},{" "}
                  {profile.address.city || "N/A"}
                </p>
                <p className='text-gray-800 pl-4'>
                  {profile.address.postalCode || "N/A"},{" "}
                  {profile.address.country || "N/A"}
                </p>
                <div className='mt-4 text-right'>
                  <button
                    onClick={() => setEditMode(true)}
                    className='bg-indigo-500 hover:bg-indigo-600 text-white font-bold py-2 px-4 rounded transition duration-200'>
                    Edit Profile
                  </button>
                </div>
              </div>
            )}
          </div>
        );
      case "myCars":
        if (myCarsLoading) {
          return <p className='text-gray-700'>Loading your cars...</p>;
        }
        if (myCarsError) {
          return <p className='text-red-600'>Error: {myCarsError}</p>;
        }
        if (myCars.length === 0) {
          return <p className='text-gray-700'>You don't own any cars yet.</p>;
        }
        return (
          <div>
            <h2 className='text-2xl font-semibold text-gray-700 mb-4'>
              My Cars
            </h2>
            <p className='text-gray-600 mb-4'>
              List of cars you currently own:
            </p>
            {myCars.map((car) => (
              <div
                key={car.id}
                className='bg-gray-50 p-4 rounded-md mt-4 shadow-sm'>
                <p className='font-bold text-gray-900 text-lg'>
                  {car.make} {car.model}
                </p>
                <p className='text-gray-800 text-sm'>
                  Production Year: {car.productionYear}, VIN: {car.vin}
                </p>
                <p className='text-gray-800 text-sm'>
                  Condition: {car.vehicleCondition}
                </p>
              </div>
            ))}
          </div>
        );
      case "newRequest":
        return (
          <div>
            <h2 className='text-2xl font-semibold text-gray-700 mb-4'>
              Send New Request
            </h2>
            <p className='text-gray-600 mb-4'>
              Choose the type of request you wish to send:
            </p>
            <div className='grid grid-cols-1 md:grid-cols-2 gap-4 mb-8'>
              {" "}
              <button
                onClick={() => {
                  setActiveRequestForm("service");
                  resetRequestForm();
                }}
                className='bg-sky-600 hover:bg-sky-700 text-white font-bold py-3 px-6 rounded shadow-md transition duration-300'>
                Car Service
              </button>
              <button
                onClick={() => {
                  setActiveRequestForm("inspection");
                  resetRequestForm();
                }}
                className='bg-emerald-600 hover:bg-emerald-700 text-white font-bold py-3 px-6 rounded shadow-md transition duration-300'>
                Car Inspection
              </button>
            </div>

            {showSuccessPopup ? (
              <div className='fixed inset-0 bg-gray-600 bg-opacity-75 flex items-center justify-center z-50'>
                <div className='bg-white p-8 rounded-lg shadow-xl max-w-sm w-full text-center'>
                  <h3 className='text-2xl font-semibold text-green-700 mb-4'>
                    Request Submitted!
                  </h3>
                  <p className='text-gray-700 mb-6'>{formSuccess}</p>
                  <button
                    onClick={() => {
                      setActiveRequestForm("none");
                      closeSuccessPopup();
                    }}
                    className='bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded transition duration-200'>
                    Back to Requests
                  </button>
                </div>
              </div>
            ) : (
              activeRequestForm === "service" && (
                <div className='bg-gray-50 p-6 rounded-lg shadow-sm'>
                  <h3 className='text-xl font-semibold text-gray-700 mb-4'>
                    New Car Service Request
                  </h3>
                  {requestFormMyCarsLoading && (
                    <p className='text-gray-700'>
                      Loading your cars for selection...
                    </p>
                  )}
                  {requestFormMyCarsError && (
                    <p className='text-red-600'>
                      Error loading cars: {requestFormMyCarsError}
                    </p>
                  )}
                  {requestFormMyCars.length === 0 &&
                    !requestFormMyCarsLoading &&
                    !requestFormMyCarsError && (
                      <p className='text-gray-700'>
                        You don't own any cars to request service for.
                      </p>
                    )}

                  {!requestFormMyCarsLoading &&
                    !requestFormMyCarsError &&
                    requestFormMyCars.length > 0 && (
                      <form
                        onSubmit={(e) => {
                          e.preventDefault();
                          submitServiceRequest();
                        }}
                        className='space-y-4'>
                        <div>
                          <label
                            htmlFor='serviceVehicle'
                            className='block text-gray-700 text-sm font-bold mb-1'>
                            Select Car:
                          </label>
                          <select
                            id='serviceVehicle'
                            name='serviceVehicle'
                            value={selectedVehicleId}
                            onChange={(e) =>
                              setSelectedVehicleId(e.target.value)
                            }
                            className='shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'
                            required>
                            {requestFormMyCars.map((car) => (
                              <option key={car.id} value={car.id}>
                                {car.make} {car.model} (VIN: {car.vin})
                              </option>
                            ))}
                          </select>
                        </div>
                        <div>
                          <label
                            htmlFor='serviceNotes'
                            className='block text-gray-700 text-sm font-bold mb-1'>
                            Notes (Optional):
                          </label>
                          <textarea
                            id='serviceNotes'
                            name='serviceNotes'
                            value={customerNotes}
                            onChange={(e) => setCustomerNotes(e.target.value)}
                            rows={4}
                            className='shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'
                            placeholder='Describe the service needed...'></textarea>
                        </div>
                        {formError && (
                          <p className='text-red-600 text-sm'>{formError}</p>
                        )}
                        <div className='flex justify-end space-x-4 mt-6'>
                          <button
                            type='button'
                            onClick={() => {
                              setActiveRequestForm("none");
                              resetRequestForm();
                            }}
                            className='bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded transition duration-200'
                            disabled={formLoading}>
                            Cancel
                          </button>
                          <button
                            type='submit'
                            className='bg-sky-600 hover:bg-sky-700 text-white font-bold py-2 px-4 rounded transition duration-200'
                            disabled={formLoading}>
                            {formLoading
                              ? "Submitting..."
                              : "Submit Service Request"}
                          </button>
                        </div>
                      </form>
                    )}
                </div>
              )
            )}

            {activeRequestForm === "inspection" && (
              <div className='bg-gray-50 p-6 rounded-lg shadow-sm'>
                <h3 className='text-xl font-semibold text-gray-700 mb-4'>
                  New Car Inspection Request
                </h3>
                {requestFormMyCarsLoading && (
                  <p className='text-gray-700'>
                    Loading your cars for selection...
                  </p>
                )}
                {requestFormMyCarsError && (
                  <p className='text-red-600'>
                    Error loading cars: {requestFormMyCarsError}
                  </p>
                )}
                {requestFormMyCars.length === 0 &&
                  !requestFormMyCarsLoading &&
                  !requestFormMyCarsError && (
                    <p className='text-gray-700'>
                      You don't own any cars to request inspection for.
                    </p>
                  )}

                {!requestFormMyCarsLoading &&
                  !requestFormMyCarsError &&
                  requestFormMyCars.length > 0 && (
                    <form
                      onSubmit={(e) => {
                        e.preventDefault();
                        submitInspectionRequest();
                      }}
                      className='space-y-4'>
                      <div>
                        <label
                          htmlFor='inspectionVehicle'
                          className='block text-gray-700 text-sm font-bold mb-1'>
                          Select Car:
                        </label>
                        <select
                          id='inspectionVehicle'
                          name='inspectionVehicle'
                          value={selectedVehicleId}
                          onChange={(e) => setSelectedVehicleId(e.target.value)}
                          className='shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'
                          required>
                          {requestFormMyCars.map((car) => (
                            <option key={car.id} value={car.id}>
                              {car.make} {car.model} (VIN: {car.vin})
                            </option>
                          ))}
                        </select>
                      </div>
                      <div>
                        <label
                          htmlFor='inspectionNotes'
                          className='block text-gray-700 text-sm font-bold mb-1'>
                          Notes (Optional):
                        </label>
                        <textarea
                          id='inspectionNotes'
                          name='inspectionNotes'
                          value={customerNotes}
                          onChange={(e) => setCustomerNotes(e.target.value)}
                          rows={4}
                          className='shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'
                          placeholder='Describe the reason for inspection...'></textarea>
                      </div>
                      {formError && (
                        <p className='text-red-600 text-sm'>{formError}</p>
                      )}
                      <div className='flex justify-end space-x-4 mt-6'>
                        <button
                          type='button'
                          onClick={() => {
                            setActiveRequestForm("none");
                            resetRequestForm();
                          }}
                          className='bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded transition duration-200'
                          disabled={formLoading}>
                          Cancel
                        </button>
                        <button
                          type='submit'
                          className='bg-emerald-600 hover:bg-emerald-700 text-white font-bold py-2 px-4 rounded transition duration-200'
                          disabled={formLoading}>
                          {formLoading
                            ? "Submitting..."
                            : "Submit Inspection Request"}
                        </button>
                      </div>
                    </form>
                  )}
              </div>
            )}

            {activeRequestForm === "none" && !showSuccessPopup && (
              <p className='text-gray-500 text-sm mt-4'>
                Select a request type above to show the corresponding form.
              </p>
            )}
          </div>
        );
      case "myRequests":
        return (
          <div>
            <h2 className='text-2xl font-semibold text-gray-700 mb-4'>
              My Sent Requests
            </h2>
            <p className='text-gray-600'>
              Here you will see the statuses of all your submitted requests
              (service, inspection, purchase).
            </p>
            <div className='bg-gray-50 p-4 rounded-md mt-4 shadow-sm'>
              <p className='font-bold text-gray-900'>Request #123</p>
              <p className='text-gray-800'>Type: Service, Status: Pending</p>
            </div>
            <div className='bg-gray-50 p-4 rounded-md mt-2 shadow-sm'>
              <p className='font-bold text-gray-900'>Request #124</p>
              <p className='text-gray-800'>
                Type: Purchase, Status: Accepted (Car: Toyota Camry)
              </p>
            </div>
          </div>
        );
      default:
        return (
          <div>
            <h2 className='text-2xl font-semibold text-gray-700 mb-4'>
              Welcome to the Customer Panel!
            </h2>
            <p className='text-gray-600'>
              Select an option from the side menu to begin.
            </p>
          </div>
        );
    }
  };

  const menuItems = [
    { id: "availableCars", label: "Browse Cars" },
    { id: "profile", label: "My Profile" },
    { id: "myCars", label: "My Cars" },
    { id: "newRequest", label: "Send New Request" },
    { id: "myRequests", label: "My Requests" },
  ];

  return (
    <div className='min-h-screen bg-gray-100 flex'>
      {/* Sidebar */}
      <aside className='w-64 bg-purple-700 text-white shadow-lg flex flex-col'>
        <div className='p-6 border-b border-purple-600'>
          <h1 className='text-2xl font-bold'>Customer Panel</h1>
        </div>
        <nav className='flex-grow p-6'>
          <ul>
            {menuItems.map((item) => (
              <li key={item.id} className='mb-3'>
                <button
                  onClick={() => setActiveSection(item.id)}
                  className={`block w-full text-left py-2 px-4 rounded transition duration-200
                    ${
                      activeSection === item.id
                        ? "bg-purple-800 font-semibold"
                        : "hover:bg-purple-600"
                    }`}>
                  {item.label}
                </button>
              </li>
            ))}
          </ul>
        </nav>
        <div className='p-6 border-t border-purple-600 mt-auto'>
          <button
            onClick={handleLogout}
            className='block text-center bg-purple-600 hover:bg-purple-500 py-2 px-4 rounded transition duration-200 w-full'>
            Logout
          </button>
        </div>
      </aside>

      {/* Main Content Area */}
      <main className='flex-grow p-8 bg-gray-100'>
        <div className='bg-white p-8 rounded-lg shadow-md min-h-[calc(100vh-64px)]'>
          {renderContent()}
        </div>
      </main>
    </div>
  );
}
