import { Car } from '../../types/car';

export interface UseCustomerDashboardResult {
  cars: Car[];
  loading: boolean;
  error: string | null;
  filterAvailableOnly: boolean;
  setFilterAvailableOnly: React.Dispatch<React.SetStateAction<boolean>>;
}
