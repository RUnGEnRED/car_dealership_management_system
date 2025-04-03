"use client";

import { useState } from "react";
import FilterSidebar from "../../components/OffersPageComponents/FilterSidebar";
import CarList from "../../components/OffersPageComponents/CarList";
import Pagination from "../../components/OffersPageComponents/Pagination";
import {
  cars,
  sortCars,
  filterCars,
  brandModelMap,
} from "../../services/offers-service";

export default function OffersPage() {
  const [selectedBrand, setSelectedBrand] = useState("");
  const [selectedModel, setSelectedModel] = useState(""); 
  const [models, setModels] = useState<string[]>([]);
  const [priceFrom, setPriceFrom] = useState("");
  const [priceTo, setPriceTo] = useState("");
  const [yearFrom, setYearFrom] = useState("");
  const [yearTo, setYearTo] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [sortOption, setSortOption] = useState("asc");
  const itemsPerPage = 9;
  const filteredCars = filterCars(
    cars,
    selectedBrand,
    selectedModel,
    priceFrom,
    priceTo,
    yearFrom,
    yearTo
  );
  const sortedCars = sortCars(filteredCars, sortOption);
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = sortedCars.slice(indexOfFirstItem, indexOfLastItem);

  const handlePageChange = (direction: "prev" | "next") => {
    if (direction === "prev" && currentPage > 1) {
      setCurrentPage(currentPage - 1);
    } else if (direction === "next" && indexOfLastItem < filteredCars.length) {
      setCurrentPage(currentPage + 1);
    }
  };

  const handleBrandChange = (brand: string) => {
    setSelectedBrand(brand);
    setModels(brandModelMap[brand] || []);
    setSelectedModel(""); // Resetuj model po zmianie marki
  };

  const handleModelChange = (model: string) => {
    setSelectedModel(model); // Obsługa zmiany modelu
  };

  const handlePriceChange = (type: "from" | "to", value: string) => {
    if (type === "from") {
      setPriceFrom(value);
    } else {
      setPriceTo(value);
    }
  };

  const handleYearChange = (type: "from" | "to", value: string) => {
    if (type === "from") {
      setYearFrom(value);
    } else {
      setYearTo(value);
    }
  };

  const handleSortChange = (option: string) => {
    setSortOption(option);
  };

  return (
    <div className='flex min-h-screen bg-gray-100'>
      <FilterSidebar
        selectedBrand={selectedBrand}
        selectedModel={selectedModel}
        models={models}
        onBrandChange={handleBrandChange}
        onModelChange={handleModelChange} 
        priceFrom={priceFrom}
        priceTo={priceTo}
        onPriceChange={handlePriceChange}
        yearFrom={yearFrom}
        yearTo={yearTo}
        onYearChange={handleYearChange} 
      />
      <main className='flex-1 p-4'>
        <div className='flex justify-between items-center mb-4'>
          <p className='text-sm text-gray-600'>
            Znalezionych pojazdów:{" "}
            <span className='font-bold'>{filteredCars.length}</span>
          </p>
          <div className='flex items-center space-x-2'>
            <label htmlFor='sort' className='text-sm font-medium text-gray-600'>
              Sortuj według ceny:
            </label>
            <select
              id='sort'
              className='p-2 border border-gray-300 rounded'
              value={sortOption}
              onChange={(e) => handleSortChange(e.target.value)}>
              <option value='asc'>Rosnąco</option>
              <option value='desc'>Malejąco</option>
            </select>
          </div>
        </div>
        <CarList cars={currentItems} />
        <Pagination
          currentPage={currentPage}
          totalPages={Math.ceil(filteredCars.length / itemsPerPage)}
          onPageChange={handlePageChange}
        />
      </main>
    </div>
  );
}
