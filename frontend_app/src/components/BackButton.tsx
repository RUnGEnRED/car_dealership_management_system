"use client";

import { useRouter } from "next/navigation";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft } from "@fortawesome/free-solid-svg-icons";

export default function BackButton() {
  const router = useRouter();

  return (
    <button
      onClick={() => router.back()}
      className='absolute top-4 left-4 bg-white text-gray-700 p-2 rounded-full shadow hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-gray-300'>
      <FontAwesomeIcon icon={faArrowLeft} size='lg' />
    </button>
  );
}
