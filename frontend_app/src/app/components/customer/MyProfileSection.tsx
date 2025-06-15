import React from "react";
import {
  CustomerProfile,
  CustomerProfileFormData,
  MyProfileSectionProps,
} from "../../types/customer/customer";

/**
 * Component props for MyProfileSection.
 * This section displays the customer's profile information and allows editing.
 * It handles loading states, errors, and form management for profile editing.
 */

export const MyProfileSection: React.FC<MyProfileSectionProps> = ({
  profile,
  loading,
  error,
  editMode,
  formData,
  saveLoading,
  saveError,
  setEditMode,
  handleInputChange,
  handleAddressChange,
  handleSubmit,
  resetForm,
}) => {
  if (loading) {
    return <p className='text-gray-700'>Loading profile...</p>;
  }
  if (error) {
    return <p className='text-red-600'>Error: {error}</p>;
  }
  if (!profile) {
    return <p className='text-gray-700'>No profile data available.</p>;
  }

  return (
    <div>
      <h2 className='text-2xl font-semibold text-gray-700 mb-4'>My Profile</h2>

      {editMode ? (
        <form onSubmit={handleSubmit} className='space-y-4'>
          {saveError && <p className='text-red-600 text-sm'>{saveError}</p>}
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
            {profile.address.street || "N/A"}, {profile.address.city || "N/A"}
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
};
