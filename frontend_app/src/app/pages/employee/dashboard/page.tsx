import React from 'react';

export default function EmployeeDashboardPage() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100 p-4">
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md text-center">
        <h1 className="text-3xl font-bold text-gray-800 mb-4">Witaj, Pracowniku!</h1>
        <div className="mt-6">
          <a href="/" className="text-blue-600 hover:underline">Wróć do strony głównej</a>
        </div>
      </div>
    </div>
  );
}