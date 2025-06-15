export type Car = {
  id: string | number;
  vin: string;
  make: string;
  model: string;
  productionYear: number;
  price: number;
  vehicleCondition: string;
  availability: string;
  ownerId: string | number | null;
  active: boolean;
};

export interface AvailableCarsSectionProps {
  cars: Car[];
  loading: boolean;
  error: string | null;
  filterAvailableOnly: boolean;
  setFilterAvailableOnly: (value: boolean) => void;
  setSelectedVehicleId: (id: string | number | '') => void;
  setActiveRequestForm: (form: 'none' | 'service' | 'inspection' | 'purchase') => void;
  setActiveSection: (section: string) => void;
  resetRequestForm: () => void;
}

export interface MyCarsSectionProps {
  myCars: Car[];
  loading: boolean;
  error: string | null;
}

export interface UseMyCarsResult {
  myCars: Car[];
  loading: boolean;
  error: string | null;
  fetchMyCars: () => Promise<void>; 
}