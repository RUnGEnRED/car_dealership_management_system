'use client';

import React, { useState, useEffect, useCallback } from 'react';
import { useRouter } from 'next/navigation';

import { useCustomerDashboard } from '../../../hooks/useCustomerDashboard';
import { useCustomerProfile } from '../../../hooks/useCustomerProfile';
import { useMyCars } from '../../../hooks/useMyCars';
import { useRequestForms } from '../../../hooks/useRequestForm'; 
import { useMyRequests } from '../../../hooks/useMyRequests';

import { AvailableCarsSection } from '../../../components/AvailableCarSection'; 
import { MyProfileSection } from '../../../components/MyProfileSection';
import { MyCarsSection } from '../../../components/MyCarsSection';
import { NewRequestSection } from '../../../components/NewRequestSection';
import { MyRequestsSection } from '../../../components/MyRequestsSection';

/**
 * CustomerDashboardPage component serves as the main dashboard for customers.
 * It provides navigation to different sections like available cars, profile management,
 * my cars, new requests, and my requests.
 */

export default function CustomerDashboardPage() {
  const [activeSection, setActiveSection] = useState<string>('availableCars');
  const [activeRequestForm, setActiveRequestForm] = useState<'none' | 'service' | 'inspection' | 'purchase'>('none');
  const router = useRouter(); 

  const customerDashboard = useCustomerDashboard();
  const customerProfile = useCustomerProfile();
  const myCarsHook = useMyCars();
  const requestForms = useRequestForms();
  const myRequestsHook = useMyRequests();

  const { cars, loading: carsLoading, error: carsError, filterAvailableOnly, setFilterAvailableOnly } = customerDashboard;
  const {
    profile, loading: profileLoading, error: profileError, editMode, formData, saveLoading, saveError,
    setEditMode, handleInputChange, handleAddressChange, handleSubmit, resetForm
  } = customerProfile;
  const { myCars, loading: myCarsLoading, error: myCarsError } = myCarsHook;
  const { closeSuccessPopup, setSelectedVehicleId, resetRequestForm } = requestForms; 

  useEffect(() => {
    if (activeSection !== 'newRequest') {
      setActiveRequestForm('none');
      closeSuccessPopup();
    }
  }, [activeSection, closeSuccessPopup]); 

  useEffect(() => {
    if (activeSection === 'myRequests') {
      myRequestsHook.fetchMyRequests(); 
    }
  }, [activeSection, myRequestsHook.fetchMyRequests]); 

  const handleLogout = useCallback(() => {
    localStorage.removeItem('jwt_token'); 
    router.push('/'); 
  }, [router]); 

  const renderContent = () => {
    switch (activeSection) {
      case 'availableCars':
        return (
          <AvailableCarsSection
            cars={cars}
            loading={carsLoading}
            error={carsError}
            filterAvailableOnly={filterAvailableOnly}
            setFilterAvailableOnly={setFilterAvailableOnly}
            setSelectedVehicleId={setSelectedVehicleId}
            setActiveRequestForm={setActiveRequestForm}
            setActiveSection={setActiveSection}
            resetRequestForm={resetRequestForm}
          />
        );
      case 'profile':
        return (
          <MyProfileSection
            profile={profile}
            loading={profileLoading}
            error={profileError}
            editMode={editMode}
            formData={formData}
            saveLoading={saveLoading}
            saveError={saveError}
            setEditMode={setEditMode}
            handleInputChange={handleInputChange}
            handleAddressChange={handleAddressChange}
            handleSubmit={handleSubmit}
            resetForm={resetForm}
          />
        );
      case 'myCars':
        return (
          <MyCarsSection
            myCars={myCars}
            loading={myCarsLoading}
            error={myCarsError}
          />
        );
      case 'newRequest':
        return (
          <NewRequestSection
            activeRequestForm={activeRequestForm}
            setActiveRequestForm={setActiveRequestForm}
            closeSuccessPopup={closeSuccessPopup}
            setActiveSection={setActiveSection}
          />
        );
      case 'myRequests':
        return (
          <MyRequestsSection />
        );
      default:
        return (
          <div>
            <h2 className="text-2xl font-semibold text-gray-700 mb-4">Welcome to the Customer Panel!</h2>
            <p className="text-gray-600">Select an option from the side menu to begin.</p>
          </div>
        );
    }
  };

  const menuItems = [
    { id: 'availableCars', label: 'Browse Cars' },
    { id: 'profile', label: 'My Profile' },
    { id: 'myCars', label: 'My Cars' },
    { id: 'newRequest', label: 'Send New Request' },
    { id: 'myRequests', label: 'My Requests' },
  ];

  return (
    <div className="min-h-screen bg-gray-100 flex font-inter">
      {/* Sidebar navigation for the customer panel */}
      <aside className="w-64 bg-purple-700 text-white shadow-lg flex flex-col rounded-r-lg">
        <div className="p-6 border-b border-purple-600">
          <h1 className="text-2xl font-bold">Customer Panel</h1>
        </div>
        <nav className="flex-grow p-6">
          <ul>
            {menuItems.map((item) => (
              <li key={item.id} className="mb-3">
                <button
                  onClick={() => setActiveSection(item.id)}
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
