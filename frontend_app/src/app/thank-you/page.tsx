"use client";

import { useRouter } from "next/navigation";

export default function ThankYouPage() {
  const router = useRouter();

  return (
    <div className='min-h-screen bg-gray-100 flex items-center justify-center'>
      <div className='bg-white shadow-md rounded-lg p-6 max-w-md text-center'>
        <h1 className='text-2xl font-bold text-gray-800 mb-4'>
          Twoje zgłoszenie zostało wysłane!
        </h1>
        <p className='text-gray-700 mb-6'>
          Pracownik niedługo się z Tobą skontaktuje. Dziękujemy za skorzystanie
          z naszych usług!
        </p>
        <button
          onClick={() => router.push("/")}
          className='bg-[#19191f] text-white py-2 px-4 rounded hover:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-gray-800'>
          Wróć na stronę główną
        </button>
      </div>
    </div>
  );
}
