import React from "react";
import Link from "next/link";
import { Car } from "../../types/Car";

interface CarListProps {
  cars: Car[];
}

export default function CarList({ cars }: CarListProps) {
  return (
    <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4'>
      {cars.map((car) => (
        <Link key={car.id} href={`/offers/${car.id}`}>
          <div className='border border-gray-300 rounded shadow-sm p-4 bg-white cursor-pointer hover:shadow-lg transition-shadow'>
            {/* TODO: zmienić zdjęcia na pobierane z bazy */}
            <img
              src='/images/car-placeholder.jpg'
              alt={`${car.brand} ${car.model}`}
              className='w-full h-40 object-cover rounded mb-4'
            />
            <h3 className='text-lg font-bold'>
              {car.brand} {car.model}
            </h3>
            <p className='text-sm text-gray-600'>Rok: {car.year}</p>
            <p className='text-sm text-gray-600'>Cena: {car.price} PLN</p>
          </div>
        </Link>
      ))}
    </div>
  );
}
