'use client';
import React, { useState } from 'react';
import Link from 'next/link'; 

export default function CustomerDashboardPage() {
  const [activeSection, setActiveSection] = useState<string>('availableCars'); 

  const renderContent = () => {
    switch (activeSection) {
      case 'availableCars':
        return (
          <div>
            <h2 className="text-2xl font-semibold text-gray-700 mb-4">Dostępne Samochody</h2>
            <p className="text-gray-600">Tutaj będzie lista samochodów dostępnych do zakupu.</p>
            <div className="bg-gray-50 p-4 rounded-md mt-4">
              <p className="font-bold">Samochód A</p>
              <p>Model: XYZ, Cena: 50,000 PLN</p>
            </div>
            <div className="bg-gray-50 p-4 rounded-md mt-2">
              <p className="font-bold">Samochód B</p>
              <p>Model: ABC, Cena: 75,000 PLN</p>
            </div>
          </div>
        );
      case 'profile':
        return (
          <div>
            <h2 className="text-2xl font-semibold text-gray-700 mb-4">Mój Profil</h2>
            <p className="text-gray-600">Tutaj będą wyświetlone Twoje dane profilowe (imię, nazwisko, email, adres, etc.).</p>
            <div className="bg-gray-50 p-4 rounded-md mt-4">
              <p><strong>Nazwa użytkownika:</strong> davidclient</p>
              <p><strong>Email:</strong> david.client@example.com</p>
              <p><strong>Telefon:</strong> 555-0201</p>
              <p><strong>Adres:</strong> 1 Customer Lane, Suburbia, CS100, Clientville</p>
            </div>
          </div>
        );
      case 'myCars':
        return (
          <div>
            <h2 className="text-2xl font-semibold text-gray-700 mb-4">Moje Samochody</h2>
            <p className="text-gray-600">Tutaj będzie lista samochodów, które posiadasz.</p>
            <div className="bg-gray-50 p-4 rounded-md mt-4">
              <p className="font-bold">Moje Auto 1</p>
              <p>Marka: Toyota, Model: Camry, VIN: SOLDVIN0000000001</p>
            </div>
          </div>
        );
      case 'newRequest':
        return (
          <div>
            <h2 className="text-2xl font-semibold text-gray-700 mb-4">Wyślij Nową Prośbę</h2>
            <p className="text-gray-600 mb-4">Wybierz typ prośby, którą chcesz wysłać:</p>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <button className="bg-blue-500 hover:bg-blue-600 text-white font-bold py-3 px-6 rounded shadow-md transition duration-300">
                Serwis Auta
              </button>
              <button className="bg-green-500 hover:bg-green-600 text-white font-bold py-3 px-6 rounded shadow-md transition duration-300">
                Inspekcja Auta
              </button>
              <button className="bg-purple-500 hover:bg-purple-600 text-white font-bold py-3 px-6 rounded shadow-md transition duration-300">
                Zakup Auta
              </button>
            </div>
            <p className="text-gray-500 text-sm mt-4">Formularze do wypełnienia pojawią się po wyborze typu prośby.</p>
          </div>
        );
      case 'myRequests':
        return (
          <div>
            <h2 className="text-2xl font-semibold text-gray-700 mb-4">Moje Wysłane Prośby</h2>
            <p className="text-gray-600">Tutaj zobaczysz statusy wszystkich wysłanych przez Ciebie próśb (serwis, inspekcja, zakup).</p>
            <div className="bg-gray-50 p-4 rounded-md mt-4">
              <p className="font-bold">Prośba #123</p>
              <p>Typ: Serwis, Status: Oczekująca</p>
            </div>
            <div className="bg-gray-50 p-4 rounded-md mt-2">
              <p className="font-bold">Prośba #124</p>
              <p>Typ: Zakup, Status: Zaakceptowana (Samochód: Toyota Camry)</p>
            </div>
          </div>
        );
      default:
        return (
          <div>
            <h2 className="text-2xl font-semibold text-gray-700 mb-4">Witaj w Panelu Klienta!</h2>
            <p className="text-gray-600">Wybierz opcję z menu bocznego, aby rozpocząć.</p>
          </div>
        );
    }
  };

  const menuItems = [
    { id: 'availableCars', label: 'Przeglądaj Samochody' },
    { id: 'profile', label: 'Mój Profil' },
    { id: 'myCars', label: 'Moje Samochody' },
    { id: 'newRequest', label: 'Wyślij Nową Prośbę' },
    { id: 'myRequests', label: 'Moje Prośby' },
  ];

  return (
    <div className="min-h-screen bg-gray-100 flex">
      <aside className="w-64 bg-purple-700 text-white shadow-lg flex flex-col">
        <div className="p-6 border-b border-purple-600">
          <h1 className="text-2xl font-bold">Panel Klienta</h1>
        </div>
        <nav className="flex-grow p-6">
          <ul>
            {menuItems.map((item) => (
              <li key={item.id} className="mb-3">
                <button
                  onClick={() => setActiveSection(item.id)}
                  className={`block w-full text-left py-2 px-4 rounded transition duration-200
                    ${activeSection === item.id ? 'bg-purple-800 font-semibold' : 'hover:bg-purple-600'}`}
                >
                  {item.label}
                </button>
              </li>
            ))}
          </ul>
        </nav>
        <div className="p-6 border-t border-purple-600 mt-auto">
          <Link href="/" className="block text-center bg-purple-600 hover:bg-purple-500 py-2 px-4 rounded transition duration-200">
            Wróć do strony głównej
          </Link>
        </div>
      </aside>

      <main className="flex-grow p-8 bg-gray-100">
        <div className="bg-white p-8 rounded-lg shadow-md min-h-[calc(100vh-64px)]"> 
          {renderContent()}
        </div>
      </main>
    </div>
  );
}