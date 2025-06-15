import { useState, useEffect, useCallback } from "react";
import axios from "axios";
import {
  CustomerProfile,
  CustomerProfileFormData,
  CustomerProfileUpdate,
} from "../../types/customer";
import { UseCustomerProfileResult } from "../../types/useCustomerProfileResult";

/**
 * Custom hook to manage customer profile data.
 * It handles fetching, updating, and editing the customer's profile.
 */

const CUSTOMER_PROFILE_URL = "http://localhost:3001/api/customers/me";

export const useCustomerProfile = (): UseCustomerProfileResult => {
  const [profile, setProfile] = useState<CustomerProfile | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [editMode, setEditMode] = useState<boolean>(false);
  const [formData, setFormData] = useState<CustomerProfileFormData>({
    firstName: "",
    lastName: "",
    email: "",
    phoneNumber: "",
    address: {
      street: "",
      city: "",
      postalCode: "",
      country: "",
    },
  });
  const [saveLoading, setSaveLoading] = useState<boolean>(false);
  const [saveError, setSaveError] = useState<string | null>(null);

  const getAuthHeaders = useCallback(() => {
    const token = localStorage.getItem("jwt_token");
    if (!token) {
      setError("Authentication token not found. Please log in again.");
      return {};
    }
    return {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
  }, []);

  const fetchProfile = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.get<CustomerProfile>(
        CUSTOMER_PROFILE_URL,
        getAuthHeaders()
      );
      setProfile(response.data);
      setFormData({
        firstName: response.data.firstName,
        lastName: response.data.lastName,
        email: response.data.email,
        phoneNumber: response.data.phoneNumber,
        address: {
          street: response.data.address.street || "",
          city: response.data.address.city || "",
          postalCode: response.data.address.postalCode || "",
          country: response.data.address.country || "",
        },
      });
    } catch (err: any) {
      if (
        axios.isAxiosError(err) &&
        err.response &&
        err.response.status === 401
      ) {
        setError("Unauthorized. Please log in again.");
      } else {
        setError(err.message || "Failed to fetch profile data.");
      }
      console.error("Fetch profile error:", err);
    } finally {
      setLoading(false);
    }
  }, [getAuthHeaders]);

  useEffect(() => {
    fetchProfile();
  }, [fetchProfile]);

  const handleInputChange = useCallback(
    (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
      const { name, value } = e.target;
      setFormData((prevData) => ({
        ...prevData,
        [name]: value,
      }));
    },
    []
  );

  const handleAddressChange = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      const { name, value } = e.target;
      setFormData((prevData) => ({
        ...prevData,
        address: {
          ...prevData.address,
          [name]: value,
        },
      }));
    },
    []
  );

  const resetForm = useCallback(() => {
    if (profile) {
      setFormData({
        firstName: profile.firstName,
        lastName: profile.lastName,
        email: profile.email,
        phoneNumber: profile.phoneNumber,
        address: {
          street: profile.address.street || "",
          city: profile.address.city || "",
          postalCode: profile.address.postalCode || "",
          country: profile.address.country || "",
        },
      });
    }
    setSaveError(null);
  }, [profile]);

  const handleSubmit = useCallback(
    async (e: React.FormEvent) => {
      e.preventDefault();
      setSaveLoading(true);
      setSaveError(null);

      try {
        const payload: CustomerProfileUpdate = {
          firstName: formData.firstName,
          lastName: formData.lastName,
          email: formData.email,
          phoneNumber: formData.phoneNumber,
          address: {
            street: formData.address.street,
            city: formData.address.city,
            postalCode: formData.address.postalCode,
            country: formData.address.country,
          },
        };
        await axios.put(CUSTOMER_PROFILE_URL, payload, getAuthHeaders());
        setSaveLoading(false);
        setEditMode(false);
        fetchProfile();
      } catch (err: any) {
        if (
          axios.isAxiosError(err) &&
          err.response &&
          err.response.data &&
          err.response.data.message
        ) {
          setSaveError(err.response.data.message);
        } else {
          setSaveError(err.message || "Failed to save profile changes.");
        }
        console.error("Save profile error:", err);
        setSaveLoading(false);
      }
    },
    [formData, getAuthHeaders, fetchProfile]
  );

  return {
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
  };
};
