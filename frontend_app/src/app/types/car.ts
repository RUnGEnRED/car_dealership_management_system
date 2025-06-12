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