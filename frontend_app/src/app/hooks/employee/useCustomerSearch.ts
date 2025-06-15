import { useState } from "react";
import axios from "axios";
import { CustomerProfile } from "../../types/customer/customer";

export const useCustomerSearch = () => {
  const [searchId, setSearchId] = useState<string>("");
  const [searchedCustomer, setSearchedCustomer] = useState<CustomerProfile | null>(null);
  const [loadingSearchedCustomer, setLoadingSearchedCustomer] = useState<boolean>(false);
  const [errorSearchedCustomer, setErrorSearchedCustomer] = useState<string | null>(null);

  const handleSearchById = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoadingSearchedCustomer(true);
    setErrorSearchedCustomer(null);
    setSearchedCustomer(null);
    try {
      const token = localStorage.getItem("jwt_token");
      const response = await axios.get<CustomerProfile>(
        `http://localhost:3001/api/customers/${searchId}`,
        {
          headers: token ? { Authorization: `Bearer ${token}` } : {},
        }
      );
      setSearchedCustomer(response.data);
    } catch (err: any) {
      setErrorSearchedCustomer("Failed to fetch customer with this ID.");
    } finally {
      setLoadingSearchedCustomer(false);
    }
  };

  const clearSearchedCustomer = () => {
    setSearchedCustomer(null);
    setSearchId("");
    setErrorSearchedCustomer(null);
  };

  return {
    searchId,
    setSearchId,
    searchedCustomer,
    loadingSearchedCustomer,
    errorSearchedCustomer,
    handleSearchById,
    clearSearchedCustomer,
  };
};