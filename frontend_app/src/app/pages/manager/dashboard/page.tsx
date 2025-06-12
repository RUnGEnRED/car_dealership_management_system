import React from 'react';

export default function ManagerDashboardPage() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100 p-4">
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md text-center">
        <h1 className="text-3xl font-bold text-gray-800 mb-4">Witaj, Menadżerze!</h1>
        <p className="text-gray-600">To jest Twój panel menadżerski. Masz pełen wgląd w operacje salonu i zarządzanie personelem.</p>
        <div className="mt-6">
          <a href="/" className="text-blue-600 hover:underline">Wróć do strony głównej</a>
        </div>
      </div>
    </div>
  );
}