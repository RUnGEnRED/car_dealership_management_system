import React, { useState, useEffect, useCallback } from "react";
import axios from "axios";
import { useRequestForms } from "../../hooks/customer/useRequestForm";
import { Car } from "../../types/car";
import { NewRequestSectionProps } from "../../types/request";

/**
 * This component provides forms for customers to send new service, inspection, or purchase requests.
 * It dynamically fetches available cars for purchase requests and handles form submission logic.
 */

const AVAILABLE_CARS_URL = "http://localhost:3001/api/vehicles/available";

export const NewRequestSection: React.FC<NewRequestSectionProps> = ({
  activeRequestForm,
  setActiveRequestForm,
  closeSuccessPopup,
  setActiveSection,
}) => {
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
    submitPurchaseRequest,
    resetRequestForm,
  } = useRequestForms();

  const [availableCarsForPurchase, setAvailableCarsForPurchase] = useState<
    Car[]
  >([]);
  const [loadingAvailableCars, setLoadingAvailableCars] =
    useState<boolean>(false);
  const [errorAvailableCars, setErrorAvailableCars] = useState<string | null>(
    null
  );

  const getAuthHeaders = useCallback(() => {
    const token = localStorage.getItem("jwt_token");
    if (!token) {
      return {};
    }
    return {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
  }, []);

  useEffect(() => {
    if (activeRequestForm === "purchase") {
      const fetchAvailableCars = async () => {
        setLoadingAvailableCars(true);
        setErrorAvailableCars(null);
        try {
          const response = await axios.get<Car[]>(
            AVAILABLE_CARS_URL,
            getAuthHeaders()
          );
          const availableCars = response.data.filter(
            (car) => car.availability === "AVAILABLE"
          );
          setAvailableCarsForPurchase(availableCars);

          if (
            selectedVehicleId &&
            availableCars.some((car) => car.id === selectedVehicleId)
          ) {
          } else if (availableCars.length > 0) {
            setSelectedVehicleId(availableCars[0].id);
          } else {
            setSelectedVehicleId("");
          }
        } catch (err: any) {
          if (
            axios.isAxiosError(err) &&
            err.response &&
            err.response.status === 401
          ) {
            setErrorAvailableCars(
              "Unauthorized. Please log in again to fetch available cars."
            );
          } else {
            setErrorAvailableCars(
              err.message || "Failed to fetch available cars."
            );
          }
          console.error("Fetch available cars error:", err);
        } finally {
          setLoadingAvailableCars(false);
        }
      };
      fetchAvailableCars();
    } else {
      setAvailableCarsForPurchase([]);
      setLoadingAvailableCars(false);
      setErrorAvailableCars(null);
    }
  }, [
    activeRequestForm,
    getAuthHeaders,
    selectedVehicleId,
    setSelectedVehicleId,
  ]);

  return (
    <div>
      <h2 className='text-2xl font-semibold text-gray-700 mb-4'>
        Send New Request
      </h2>
      <p className='text-gray-600 mb-4'>
        Choose the type of request you wish to send:
      </p>
      <div className='grid grid-cols-1 md:grid-cols-3 gap-4 mb-8'>
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
        <button
          onClick={() => {
            setActiveRequestForm("purchase");
            resetRequestForm();
          }}
          className='bg-violet-600 hover:bg-violet-700 text-white font-bold py-3 px-6 rounded shadow-md transition duration-300'>
          Car Purchase
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
                setActiveSection("myRequests");
              }}
              className='bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded transition duration-200'>
              Back to Requests
            </button>
          </div>
        </div>
      ) : (
        <>
          {activeRequestForm === "service" && (
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

          {activeRequestForm === "purchase" && (
            <div className='bg-gray-50 p-6 rounded-lg shadow-sm'>
              <h3 className='text-xl font-semibold text-gray-700 mb-4'>
                New Car Purchase Request
              </h3>
              {loadingAvailableCars ? (
                <p className='text-gray-700'>Loading available cars...</p>
              ) : errorAvailableCars ? (
                <p className='text-red-600'>
                  Error loading available cars: {errorAvailableCars}
                </p>
              ) : availableCarsForPurchase.length === 0 ? (
                <p className='text-gray-700'>No available cars for purchase.</p>
              ) : (
                <form
                  onSubmit={(e) => {
                    e.preventDefault();
                    submitPurchaseRequest();
                  }}
                  className='space-y-4'>
                  <div>
                    <label
                      htmlFor='purchaseVehicle'
                      className='block text-gray-700 text-sm font-bold mb-1'>
                      Select Car:
                    </label>
                    <select
                      id='purchaseVehicle'
                      name='purchaseVehicle'
                      value={selectedVehicleId}
                      onChange={(e) => setSelectedVehicleId(e.target.value)}
                      className='shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'
                      required>
                      <option value='' disabled>
                        -- Select a car --
                      </option>
                      {availableCarsForPurchase.map((car) => (
                        <option key={car.id} value={car.id}>
                          {car.make} {car.model} (VIN: {car.vin || "N/A"}) -{" "}
                          {car.price} PLN
                        </option>
                      ))}
                    </select>
                  </div>
                  <div>
                    <label
                      htmlFor='purchaseNotes'
                      className='block text-gray-700 text-sm font-bold mb-1'>
                      Notes (Optional):
                    </label>
                    <textarea
                      id='purchaseNotes'
                      name='purchaseNotes'
                      value={customerNotes}
                      onChange={(e) => setCustomerNotes(e.target.value)}
                      rows={4}
                      className='shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline'
                      placeholder='Describe any specific requirements or questions about the purchase...'></textarea>
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
                      className='bg-violet-600 hover:bg-violet-700 text-white font-bold py-2 px-4 rounded transition duration-200'
                      disabled={formLoading || !selectedVehicleId}>
                      {formLoading
                        ? "Submitting..."
                        : "Submit Purchase Request"}
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
        </>
      )}
    </div>
  );
};
