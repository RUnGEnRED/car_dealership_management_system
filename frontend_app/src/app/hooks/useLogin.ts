/**
 * Custom React hook for handling user login logic.
 * This hook encapsulates the state management for username, password,
 * loading status, and error messages during the login process.
 * It communicates with the backend API to authenticate the user and
 * handles token storage and redirection based on the user's assigned roles.
 */
import { useState } from 'react'; 
import { useRouter } from 'next/navigation';
import axios from 'axios';
import { AuthResponse, UseLoginResult } from '../types/auth';

const LOGIN_URL = 'http://localhost:3001/api/auth/signin';

export const useLogin = (): UseLoginResult => {
  const [username, setUsername] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const router = useRouter();

  const handleLogin = async (e: React.FormEvent) => { 
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const response = await axios.post<AuthResponse>(LOGIN_URL, {
        username: username,
        password: password,
      });

      const { token, roles } = response.data;

      if (token) {
        localStorage.setItem('jwt_token', token);
        console.log('Login successful! Token:', token);
        console.log('User roles:', roles);

        if (roles && roles.length > 0) {
          if (roles.includes('ROLE_MANAGER')) {
            router.push('/pages/manager/dashboard');
          } else if (roles.includes('ROLE_EMPLOYEE')) {
            router.push('/pages/employee/dashboard');
          } else if (roles.includes('ROLE_CUSTOMER')) {
            router.push('/pages/customer/dashboard');
          } else {
            setError('User roles not recognized. Please contact support.');
            localStorage.removeItem('jwt_token');
          }
        } else {
          setError('No roles assigned to this user. Please contact support.');
          localStorage.removeItem('jwt_token');
        }
      } else {
        setError('Login failed: No token received.');
      }
    } catch (err: any) {
      if (axios.isAxiosError(err)) {
        if (err.response) {
          if (err.response.status === 401) {
            setError('Invalid username or password.');
          } else if (err.response.data && err.response.data.message) {
            setError(err.response.data.message);
          } else {
            setError('An error occurred during login. Please try again.');
          }
        } else if (err.request) {
          setError('No response from server. Check your network connection or server status.');
        } else {
          setError('An unexpected error occurred. Please try again.');
        }
      } else {
        setError('An unexpected error occurred. Please try again.');
      }
      console.error('Login error:', err);
    } finally {
      setLoading(false);
    }
  };

  return {
    username,
    setUsername,
    password,
    setPassword,
    error,
    loading,
    handleLogin,
  };
};