"use client";

import { useParams, useRouter } from "next/navigation";
import { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft } from "@fortawesome/free-solid-svg-icons";

// Typ dla błędów
type Errors = {
  name?: string;
  email?: string;
  phone?: string;
  address?: string;
  city?: string;
  zipCode?: string;
  message?: string;
};

export default function PurchaseFormPage() {
  const { id } = useParams(); //Dynamiczny parametr `id` z URL
  const router = useRouter();
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    phone: "",
    address: "",
    city: "",
    zipCode: "",
    message: "",
  });
  const [errors, setErrors] = useState<Errors>({});

  const carDetails = {
    brand: "Toyota",
    model: "Corolla",
    year: 2020,
    price: 50000,
    mileage: "50,000 km",
    condition: "Bardzo dobry",
  };

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const validateForm = () => {
    const newErrors: Errors = {};

    if (!formData.name.trim())
      newErrors.name = "Imię i nazwisko jest wymagane.";
    if (!formData.email.trim() || !/\S+@\S+\.\S+/.test(formData.email))
      newErrors.email = "Podaj poprawny adres e-mail.";
    if (!formData.phone.trim() || !/^\d{9}$/.test(formData.phone))
      newErrors.phone = "Podaj poprawny numer telefonu (9 cyfr).";
    if (!formData.address.trim()) newErrors.address = "Adres jest wymagany.";
    if (!formData.city.trim()) newErrors.city = "Miasto jest wymagane.";
    if (!formData.zipCode.trim() || !/^\d{2}-\d{3}$/.test(formData.zipCode))
      newErrors.zipCode = "Podaj poprawny kod pocztowy (XX-XXX).";

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (validateForm()) {
      router.push("/thank-you");
    }
  };

  return (
    <div className='min-h-screen bg-gray-100 p-6 relative'>
      {/* Przycisk powrotu */}
      <button
        onClick={() => router.back()}
        className='absolute top-4 left-4 bg-white text-gray-700 p-2 rounded-full shadow hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-gray-300'>
        <FontAwesomeIcon icon={faArrowLeft} size='lg' />
      </button>

      <div className='max-w-4xl mx-auto bg-white shadow-md rounded-lg p-6'>
        <h1 className='text-2xl font-bold mb-4'>Formularz zakupu</h1>
        <div className='mb-6'>
          <h2 className='text-lg font-semibold text-gray-800 mb-2'>
            Szczegóły pojazdu
          </h2>
          <ul className='text-gray-700'>
            <li>
              <strong>Marka:</strong> {carDetails.brand}
            </li>
            <li>
              <strong>Model:</strong> {carDetails.model}
            </li>
            <li>
              <strong>Rocznik:</strong> {carDetails.year}
            </li>
            <li>
              <strong>Cena:</strong> {carDetails.price} PLN
            </li>
            <li>
              <strong>Przebieg:</strong> {carDetails.mileage}
            </li>
            <li>
              <strong>Stan techniczny:</strong> {carDetails.condition}
            </li>
          </ul>
        </div>
        <form onSubmit={handleSubmit}>
          <div className='mb-4'>
            <label
              htmlFor='name'
              className='block text-gray-700 font-bold mb-2'>
              Imię i nazwisko
            </label>
            <input
              type='text'
              id='name'
              name='name'
              className={`w-full border ${
                errors.name ? "border-red-500" : "border-gray-300"
              } rounded p-2`}
              placeholder='Wpisz swoje imię i nazwisko'
              value={formData.name}
              onChange={handleInputChange}
            />
            {errors.name && (
              <p className='text-red-500 text-sm mt-1'>{errors.name}</p>
            )}
          </div>
          <div className='mb-4'>
            <label
              htmlFor='email'
              className='block text-gray-700 font-bold mb-2'>
              Adres e-mail
            </label>
            <input
              type='email'
              id='email'
              name='email'
              className={`w-full border ${
                errors.email ? "border-red-500" : "border-gray-300"
              } rounded p-2`}
              placeholder='Wpisz swój adres e-mail'
              value={formData.email}
              onChange={handleInputChange}
            />
            {errors.email && (
              <p className='text-red-500 text-sm mt-1'>{errors.email}</p>
            )}
          </div>
          <div className='mb-4'>
            <label
              htmlFor='phone'
              className='block text-gray-700 font-bold mb-2'>
              Numer telefonu
            </label>
            <input
              type='tel'
              id='phone'
              name='phone'
              className={`w-full border ${
                errors.phone ? "border-red-500" : "border-gray-300"
              } rounded p-2`}
              placeholder='Wpisz swój numer telefonu (9 cyfr)'
              value={formData.phone}
              onChange={handleInputChange}
            />
            {errors.phone && (
              <p className='text-red-500 text-sm mt-1'>{errors.phone}</p>
            )}
          </div>
          <div className='mb-4'>
            <label
              htmlFor='address'
              className='block text-gray-700 font-bold mb-2'>
              Adres
            </label>
            <input
              type='text'
              id='address'
              name='address'
              className={`w-full border ${
                errors.address ? "border-red-500" : "border-gray-300"
              } rounded p-2`}
              placeholder='Wpisz swój adres'
              value={formData.address}
              onChange={handleInputChange}
            />
            {errors.address && (
              <p className='text-red-500 text-sm mt-1'>{errors.address}</p>
            )}
          </div>
          <div className='mb-4'>
            <label
              htmlFor='city'
              className='block text-gray-700 font-bold mb-2'>
              Miasto
            </label>
            <input
              type='text'
              id='city'
              name='city'
              className={`w-full border ${
                errors.city ? "border-red-500" : "border-gray-300"
              } rounded p-2`}
              placeholder='Wpisz swoje miasto'
              value={formData.city}
              onChange={handleInputChange}
            />
            {errors.city && (
              <p className='text-red-500 text-sm mt-1'>{errors.city}</p>
            )}
          </div>
          <div className='mb-4'>
            <label
              htmlFor='zipCode'
              className='block text-gray-700 font-bold mb-2'>
              Kod pocztowy
            </label>
            <input
              type='text'
              id='zipCode'
              name='zipCode'
              className={`w-full border ${
                errors.zipCode ? "border-red-500" : "border-gray-300"
              } rounded p-2`}
              placeholder='XX-XXX'
              value={formData.zipCode}
              onChange={handleInputChange}
            />
            {errors.zipCode && (
              <p className='text-red-500 text-sm mt-1'>{errors.zipCode}</p>
            )}
          </div>
          <div className='mb-4'>
            <label
              htmlFor='message'
              className='block text-gray-700 font-bold mb-2'>
              Wiadomość
            </label>
            <textarea
              id='message'
              name='message'
              className='w-full border border-gray-300 rounded p-2'
              placeholder='Wpisz dodatkowe informacje'
              rows={4}
              value={formData.message}
              onChange={handleInputChange}></textarea>
          </div>
          <button
            type='submit'
            className='bg-[#19191f] text-white py-2 px-4 rounded hover:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-gray-800'>
            Wyślij zgłoszenie
          </button>
        </form>
      </div>
    </div>
  );
}
