import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faCar,
  faCalendar,
  faTachometerAlt,
  faCogs,
  faTag,
} from "@fortawesome/free-solid-svg-icons";

export default function CarDetails({ car }: { car: any }) {
  return (
    <div className='p-6'>
      <h1 className='text-3xl font-bold mb-4 text-gray-800'>
        {car.brand} {car.model}
      </h1>
      <ul className='grid grid-cols-2 gap-4 mb-6'>
        <li className='flex items-center text-gray-700'>
          <FontAwesomeIcon icon={faCar} className='mr-2 text-gray-500' />
          <strong>Marka:</strong> {car.brand}
        </li>
        <li className='flex items-center text-gray-700'>
          <FontAwesomeIcon icon={faCalendar} className='mr-2 text-gray-500' />
          <strong>Rocznik:</strong> {car.year}
        </li>
        <li className='flex items-center text-gray-700'>
          <FontAwesomeIcon
            icon={faTachometerAlt}
            className='mr-2 text-gray-500'
          />
          <strong>Przebieg:</strong> {car.mileage}
        </li>
        <li className='flex items-center text-gray-700'>
          <FontAwesomeIcon icon={faCogs} className='mr-2 text-gray-500' />
          <strong>Stan techniczny:</strong> {car.condition}
        </li>
        <li className='flex items-center text-gray-700'>
          <FontAwesomeIcon icon={faTag} className='mr-2 text-gray-500' />
          <strong>Cena:</strong> {car.price} PLN
        </li>
        <li className='flex items-center text-gray-700'>
          <FontAwesomeIcon icon={faCar} className='mr-2 text-gray-500' />
          <strong>Typ wyposażenia:</strong> {car.equipment}
        </li>
      </ul>
      <p className='text-gray-700 mb-6'>
        <strong>Opis:</strong> {car.description}
      </p>
    </div>
  );
}
