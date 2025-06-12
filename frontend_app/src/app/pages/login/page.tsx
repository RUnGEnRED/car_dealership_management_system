'use client';

import React, { FormEvent } from 'react'; 
import { useLogin } from '../../hooks/useLogin'; 

/**
 * User login page for the application.
 * It utilizes the `useLogin` custom hook to manage authentication logic,
 * such as handling form state, communicating with the backend API,
 * managing errors, and redirecting to the appropriate dashboard based on user roles.
 * The entire user interface for the login form is implemented directly on this page.
 */

export default function LoginPage() {
  const {
    username,
    setUsername,
    password,
    setPassword,
    error,
    loading,
    handleLogin,
  } = useLogin();

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
        <h1 className="text-2xl font-bold text-center mb-6 text-gray-800">Login</h1>
        <form onSubmit={handleLogin}>
          <div className="mb-4">
            <label htmlFor="username" className="block text-gray-700 text-sm font-bold mb-2">
              Username:
            </label>
            <input
              type="text"
              id="username"
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              disabled={loading}
            />
          </div>
          <div className="mb-6">
            <label htmlFor="password" className="block text-gray-700 text-sm font-bold mb-2">
              Password:
            </label>
            <input
              type="password"
              id="password"
              className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              disabled={loading}
            />
          </div>
          {error && <p className="text-red-500 text-xs italic mb-4">{error}</p>}
          <div className="flex items-center justify-between">
            <button
              type="submit"
              className="bg-purple-600 hover:bg-purple-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-full transform transition duration-300 hover:scale-105"
              disabled={loading}
            >
              {loading ? 'Logging In...' : 'Log In'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}