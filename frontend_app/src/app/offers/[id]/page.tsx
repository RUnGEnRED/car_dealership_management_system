"use client";

import { useParams, useRouter } from "next/navigation";
import { useState } from "react";
import BackButton from "../../../components/BackButton";
import CarDetails from "../../../components/CarDetails";
import FavoriteButton from "../../../components/FavoriteButton";
import ImageModal from "../../../components/ImageModal";

export default function CarDetailsPage() {
  const { id } = useParams();
  const router = useRouter();
  const [isModalOpen, setIsModalOpen] = useState(false);

  //Dane tymczasowe - do poprawy po zrobieniu bazy danych
  const car = {
    brand: "Toyota",
    model: "Corolla",
    year: 2020,
    price: 50000,
    mileage: "50,000 km",
    condition: "Bardzo dobry",
    equipment: "Standard",
    description:
      "Toyota Corolla to ikona niezawodności i nowoczesnego designu. Rocznik 2020 oferuje dynamiczny silnik, oszczędność paliwa oraz zaawansowane systemy bezpieczeństwa. Wnętrze zapewnia komfort i nowoczesne technologie, a elegancka linia nadwozia podkreśla nowoczesny charakter pojazdu. Idealny wybór do miasta i na długie trasy.",
    availability: "Dostępny",
    image: "/images/car-placeholder.jpg",
  };

  if (!car) {
    return (
      <p className='text-center text-red-500 font-bold mt-10'>
        Błąd - ten pojazd nie istnieje
      </p>
    );
  }

  return (
    <div className='min-h-screen bg-gray-100 p-6 relative'>
      <BackButton />
      <div className='max-w-4xl mx-auto bg-white shadow-md rounded-lg overflow-hidden'>
        <div className='relative'>
          <img
            src={car.image}
            alt={`${car.brand} ${car.model}`}
            className='w-full h-64 object-cover cursor-pointer'
            onClick={() => setIsModalOpen(true)}
          />
          <FavoriteButton />
        </div>
        <CarDetails car={car} />
        <div className='flex space-x-4 p-6'>
          <button
            className='bg-[#19191f] text-white py-2 px-4 rounded hover:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-gray-800 flex items-center'
            onClick={() => router.push(`/purchase-form/${id}`)}>
            Wyślij zgłoszenie zakupu
          </button>
        </div>
      </div>
      <ImageModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        imageSrc={car.image}
        altText={`${car.brand} ${car.model}`}
      />
    </div>
  );
}
