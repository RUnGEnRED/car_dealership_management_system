'use client';

import React, { useState, useCallback } from 'react';
import { useRouter } from 'next/navigation';

import { useEmployeeProfile } from '../../../hooks/employee/useEmployeeProfile';
import { useEmployeeDashboard } from '../../../hooks/employee/useEmployeeDashboard';
import { useEmployeeRequests } from '../../../hooks/employee/useEmployeeRequests';

import { MyProfileSection } from '../../../components/employee/MyProfileSection';
import { ManageVehiclesSection } from '../../../components/employee/ManageVehiclesSection';
import { ManageCustomersSection } from '../../../components/employee/ManageCustomersSection';
import { ClientRequestsSection } from '../../../components/employee/ClientRequestsSection';


export default function EmployeeDashboardPage() {
  const [activeSection, setActiveSection] = useState<string>('manageVehicles'); 

  const router = useRouter();

  const {
    profile,
    loading: employeeProfileLoading,
    error: employeeProfileError,
    editMode: editEmployeeProfileMode,
    formData: employeeProfileFormData,
    saveLoading: employeeProfileSaveLoading,
    saveError: employeeProfileSaveError,
    setEditMode: setEditEmployeeProfileMode,
    handleInputChange: handleEmployeeProfileInputChange,
    handleAddressChange: handleEmployeeProfileAddressChange,
    handleSubmit: handleEmployeeProfileSubmit,
    resetForm: resetEmployeeProfileForm,
    fetchEmployeeProfile
  } = useEmployeeProfile();

  const {
    vehicles,
    customers,
    loadingVehicles,
    errorVehicles,
    loadingCustomers,
    errorCustomers,
    fetchVehicles,
    fetchCustomers,
  } = useEmployeeDashboard();

  const {
    requests,
    loading: requestsLoading,
    error: requestsError,
    fetchRequests,
    approveRequest,
    rejectRequest,
  } = useEmployeeRequests();

  const handleLogout = useCallback(() => {
    localStorage.removeItem('jwt_token'); 
    router.push('/'); 
  }, [router]);

  const renderContent = () => {
    switch (activeSection) {
      case 'manageVehicles':
        return (
          <ManageVehiclesSection
            vehicles={vehicles}
            loading={loadingVehicles}
            error={errorVehicles}
            fetchVehicles={fetchVehicles} 
          />
        );
      case 'manageCustomers':
        return (
          <ManageCustomersSection
            customers={customers}
            vehicles={vehicles} 
            loadingCustomers={loadingCustomers}
            errorCustomers={errorCustomers}
            loadingVehicles={loadingVehicles}
            errorVehicles={errorVehicles}
            fetchCustomers={fetchCustomers}
            fetchVehicles={fetchVehicles}
          />
        );
      case 'clientRequests':
        return (
          <ClientRequestsSection
            requests={requests}
            loading={requestsLoading}
            error={requestsError}
            fetchRequests={fetchRequests}
            approveRequest={approveRequest}
            rejectRequest={rejectRequest}
          />
        );
      case 'employeeProfile':
        return (
          <MyProfileSection /> 
        );
      default:
        return (
          <div>
            <h2 className="text-2xl font-semibold text-gray-700 mb-4">Welcome to the Employee Panel!</h2>
            <p className="text-gray-600">Select an option from the side menu to begin.</p>
          </div>
        );
    }
  };

  const menuItems = [
    { id: 'manageVehicles', label: 'Manage Vehicles' },
    { id: 'manageCustomers', label: 'Manage Customers' },
    { id: 'clientRequests', label: 'Client Requests' },
    { id: 'employeeProfile', label: 'My Profile' },
  ];

  return (
    <div className="min-h-screen bg-gray-100 flex font-inter">
      {/* Sidebar navigation */}
      <aside className="w-64 bg-purple-700 text-white shadow-lg flex flex-col rounded-r-lg">
        <div className="p-6 border-b border-purple-600">
          <h1 className="text-2xl font-bold">Employee Panel</h1>
        </div>
        <nav className="flex-grow p-6">
          <ul>
            {menuItems.map((item) => (
              <li key={item.id} className="mb-3">
                <button
                  onClick={() => {
                    setActiveSection(item.id);
                  }}
                  className={`block w-full text-left py-2 px-4 rounded-md transition duration-200
                    ${activeSection === item.id ? 'bg-purple-800 font-semibold shadow-md' : 'hover:bg-purple-600'}`}
                >
                  {item.label}
                </button>
              </li>
            ))}
          </ul>
        </nav>
        <div className="p-6 border-t border-purple-600 mt-auto">
          <button
            onClick={handleLogout}
            className="block text-center bg-purple-600 hover:bg-purple-500 py-2 px-4 rounded-md transition duration-200 w-full shadow-md"
          >
            Logout
          </button>
        </div>
      </aside>

      {/* Main content area */}
      <main className="flex-grow p-8 bg-gray-100">
        <div className="bg-white p-8 rounded-lg shadow-xl min-h-[calc(100vh-64px)]">
          {renderContent()}
        </div>
      </main>
    </div>
  );
}
