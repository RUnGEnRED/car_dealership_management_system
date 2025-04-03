"use client";

export default function AccountPage() {
  return (
    <div className='min-h-screen bg-gray-100 p-6'>
      <div className='max-w-4xl mx-auto bg-white shadow-md rounded-lg p-6'>
        <h1 className='text-2xl font-bold mb-4'>Twoje konto</h1>

        {/* Informacje o użytkowniku */}
        <div className='mb-6'>
          <h2 className='text-xl font-semibold mb-2'>
            Informacje o użytkowniku
          </h2>
          <p className='text-gray-700'>
            <strong>Imię i nazwisko:</strong> Jan Kowalski
          </p>
          <p className='text-gray-700'>
            <strong>Email:</strong> jan.kowalski@example.com
          </p>
          <p className='text-gray-700'>
            <strong>Data rejestracji:</strong> 01.01.2023
          </p>
        </div>

        {/* Zarządzanie kontem */}
        <div>
          <h2 className='text-xl font-semibold mb-2'>Zarządzanie kontem</h2>
          <button className='bg-[#19191f] text-white py-2 px-4 rounded hover:bg-[#2a2a34] focus:outline-none focus:ring-2 focus:ring-[#2a2a34]'>
            Edytuj dane
          </button>
          <button className='bg-red-500 text-white py-2 px-4 rounded ml-4 hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-600'>
            Usuń konto
          </button>
        </div>
      </div>
    </div>
  );
}
