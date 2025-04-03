import { Car } from "../types/Car";

//Tymczasowe dane - będą pobierane z bazy danych
export const brandModelMap: { [key: string]: string[] } = {
  toyota: ["Corolla", "Yaris", "Camry", "RAV4"],
  audi: ["A3", "Q5", "A6", "TT"],
  bmw: ["X5", "X3", "M3", "i3"],
  ford: ["Focus", "Fiesta", "Mustang", "Kuga"],
  renault: ["Clio", "Megane"],
  mercedes: ["C-Class", "E-Class"],
};

//Tymczasowe dane - będą pobierane z bazy danych
export const cars: Car[] = [
  { id: 1, brand: "Toyota", model: "Corolla", year: 2020, price: 50000},
  { id: 2, brand: "Audi", model: "A3", year: 2019, price: 60000 },
  { id: 3, brand: "BMW", model: "X5", year: 2021, price: 120000 },
  { id: 4, brand: "Ford", model: "Focus", year: 2018, price: 40000 },
  { id: 5, brand: "Renault", model: "Clio", year: 2022, price: 30000 },
  { id: 6, brand: "Mercedes", model: "C-Class", year: 2020, price: 80000 },
  { id: 7, brand: "Toyota", model: "Yaris", year: 2017, price: 20000 },
  { id: 8, brand: "Audi", model: "Q5", year: 2021, price: 100000 },
  { id: 9, brand: "BMW", model: "X3", year: 2020, price: 90000 },
  { id: 10, brand: "Ford", model: "Fiesta", year: 2019, price: 35000 },
  { id: 11, brand: "Toyota", model: "Camry", year: 2021, price: 70000 },
  { id: 12, brand: "Audi", model: "A6", year: 2020, price: 75000 },
  { id: 13, brand: "BMW", model: "M3", year: 2022, price: 150000 },
  { id: 14, brand: "Ford", model: "Mustang", year: 2021, price: 130000 },
  { id: 15, brand: "Renault", model: "Megane", year: 2019, price: 45000 },
  { id: 16, brand: "Mercedes", model: "E-Class", year: 2022, price: 110000 },
  { id: 17, brand: "Toyota", model: "RAV4", year: 2023, price: 85000 },
  { id: 18, brand: "Audi", model: "TT", year: 2018, price: 65000 },
  { id: 19, brand: "BMW", model: "i3", year: 2020, price: 55000 },
  { id: 20, brand: "Ford", model: "Kuga", year: 2022, price: 60000 },
];

export function sortCars(cars: Car[], sortOption: string): Car[] {
  return [...cars].sort((a, b) => {
    if (sortOption === "asc") {
      return a.price - b.price;
    } else {
      return b.price - a.price;
    }
  });
}

export function filterCars(
    cars: Car[],
    selectedBrand: string,
    selectedModel: string,
    priceFrom: string,
    priceTo: string,
    yearFrom: string,
    yearTo: string
  ): Car[] {
    return cars.filter((car) => {
      const brandMatch =
        !selectedBrand || car.brand.toLowerCase() === selectedBrand.toLowerCase();
      const modelMatch =
        !selectedModel || car.model.toLowerCase() === selectedModel.toLowerCase();
      const priceMatch =
        (!priceFrom || car.price >= parseFloat(priceFrom)) &&
        (!priceTo || car.price <= parseFloat(priceTo));
      const yearMatch =
        (!yearFrom || car.year >= parseInt(yearFrom, 10)) &&
        (!yearTo || car.year <= parseInt(yearTo, 10));
  
      return brandMatch && modelMatch && priceMatch && yearMatch;
    });
  }
