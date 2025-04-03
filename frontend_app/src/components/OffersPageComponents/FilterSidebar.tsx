import React from "react";
import { FilterSidebarProps } from "../../types/FilterSidebarProps";

export default function FilterSidebar({
  selectedBrand,
  selectedModel,
  models,
  onBrandChange,
  onModelChange,
  priceFrom,
  priceTo,
  onPriceChange,
  yearFrom,
  yearTo,
  onYearChange,
}: FilterSidebarProps) {
  return (
    <aside className='w-1/4 p-4 bg-white shadow-md'>
      <h2 className='text-xl font-bold mb-4'>Filtr wyszukiwania</h2>
      <form>
        {/* Marka */}
        <div className='mb-4'>
          <label className='block text-sm font-medium mb-2' htmlFor='brand'>
            Marka
          </label>
          <select
            id='brand'
            className='w-full p-2 border border-gray-300 rounded'
            value={selectedBrand || ""} // Ustawienie wartości domyślnej
            onChange={(e) => onBrandChange(e.target.value)}>
            <option value=''>Wszystkie</option>
            <option value='toyota'>Toyota</option>
            <option value='audi'>Audi</option>
            <option value='bmw'>BMW</option>
            <option value='ford'>Ford</option>
            <option value='renault'>Renault</option>
            <option value='mercedes'>Mercedes</option>
          </select>
        </div>

        {/* Model */}
        <div className='mb-4'>
          <label className='block text-sm font-medium mb-2' htmlFor='model'>
            Model
          </label>
          <select
            id='model'
            className='w-full p-2 border border-gray-300 rounded'
            value={selectedModel ? selectedModel : ""}
            onChange={(e) => onModelChange(e.target.value)}
            disabled={!selectedBrand}>
            <option value=''>Wszystkie</option>
            {models.map((model) => (
              <option key={model} value={model}>
                {model}
              </option>
            ))}
          </select>
        </div>

        {/* Cena */}
        <div className='mb-4'>
          <label className='block text-sm font-medium mb-2' htmlFor='price'>
            Cena
          </label>
          <div className='flex space-x-2'>
            <input
              type='number'
              id='price-from'
              className='w-1/2 p-2 border border-gray-300 rounded'
              placeholder='Od'
              value={priceFrom || ""} // Ustawienie wartości domyślnej
              onChange={(e) => onPriceChange("from", e.target.value)}
              min='0'
            />
            <input
              type='number'
              id='price-to'
              className='w-1/2 p-2 border border-gray-300 rounded'
              placeholder='Do'
              value={priceTo || ""} // Ustawienie wartości domyślnej
              onChange={(e) => onPriceChange("to", e.target.value)}
              min='0'
            />
          </div>
        </div>

        {/* Rok produkcji */}
        <div className='mb-4'>
          <label className='block text-sm font-medium mb-2' htmlFor='year'>
            Rok produkcji
          </label>
          <div className='flex space-x-2'>
            <input
              type='number'
              id='year-from'
              className='w-1/2 p-2 border border-gray-300 rounded'
              placeholder='Od'
              value={yearFrom || ""} // Ustawienie wartości domyślnej
              onChange={(e) => onYearChange("from", e.target.value)}
              min='1900'
              max={new Date().getFullYear()}
            />
            <input
              type='number'
              id='year-to'
              className='w-1/2 p-2 border border-gray-300 rounded'
              placeholder='Do'
              value={yearTo || ""} // Ustawienie wartości domyślnej
              onChange={(e) => onYearChange("to", e.target.value)}
              min='1900'
              max={new Date().getFullYear()}
            />
          </div>
        </div>

        {/* Przycisk wyszukiwania */}
        <button
          type='submit'
          className='w-full bg-[#19191f] text-white py-2 px-4 rounded hover:bg-[#2a2a34] focus:outline-none focus:ring-2 focus:ring-[#2a2a34]'>
          Wyszukaj
        </button>
      </form>
    </aside>
  );
}
