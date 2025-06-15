import { Car } from '../types/car';

/**
 * Defines the possible types of requests a customer can make.
 */
export type RequestType = 'SERVICE' | 'INSPECTION' | 'PURCHASE';

/**
 * Defines the possible statuses for a customer request.
 */
export type RequestStatus = 'PENDING' | 'ACCEPTED' | 'REJECTED' | 'COMPLETED';

/**
 * Represents the payload for a service request.
 */
export interface ServiceRequestPayload {
  vehicleId: string | number;
  customerNotes: string;
}

/**
 * Represents the payload for an inspection request.
 */
export interface InspectionRequestPayload {
  vehicleId: string | number;
  customerNotes: string;
}

/**
 * Represents the payload for a purchase request.
 */
export interface PurchaseRequestPayload {
  vehicleId: string | number;
  customerNotes: string;
}

/**
 * Represents the simplified response object returned immediately after a request is successfully created.
 * This is used for API responses that just confirm the creation of a request.
 */
export interface RequestCreationResponse {
  message: string;
  requestId?: string | number;
}

/**
 * Represents a detailed object for a customer request, typically used when fetching a list of existing requests.
 * Contains comprehensive information about the request and associated entities.
 */
export interface DetailedRequest {
  id: number;
  customerId: number;
  customerUsername: string;
  vehicleId: number;
  vehicleVin?: string; 
  assignedEmployeeId: number | null; 
  assignedEmployeeUsername: string | null; 
  requestType: RequestType;
  status: RequestStatus;
  customerNotes: string;
  createdAt: string; 
  updatedAt: string; 
}
/**
 * Defines the props for the NewRequestSection component.
 * It specifies the active form type, functions to set active form and section,
 * and a function to close the success popup.
 */

export interface NewRequestSectionProps {
  activeRequestForm: 'none' | 'service' | 'inspection' | 'purchase';
  setActiveRequestForm: (form: 'none' | 'service' | 'inspection' | 'purchase') => void;
  closeSuccessPopup: () => void;
  setActiveSection: (section: string) => void; 
}

/**
 * Defines the return type for the useMyRequests hook.
 * It provides the list of requests made by the user, loading status, error messages,
 * and a function to manually refetch the requests.
 */
export interface UseMyRequestsResult {
  myRequests: DetailedRequest[];
  loading: boolean;
  error: string | null;
  fetchMyRequests: () => Promise<void>;
}

/**
 * Defines the return type for the useRequestForms hook.
 * It encapsulates states and functions related to submitting various customer requests
 * (service, inspection, purchase), including car selection, notes, and form submission status.
 */
export interface UseRequestFormsResult {
  myCars: Car[];
  myCarsLoading: boolean;
  myCarsError: string | null;
  selectedVehicleId: string | number | '';
  customerNotes: string;
  formLoading: boolean;
  formError: string | null;
  formSuccess: string | null;
  showSuccessPopup: boolean;
  setSelectedVehicleId: (id: string | number | '') => void;
  setCustomerNotes: (notes: string) => void;
  submitServiceRequest: () => Promise<void>;
  submitInspectionRequest: () => Promise<void>;
  submitPurchaseRequest: () => Promise<void>;
  resetRequestForm: () => void;
  closeSuccessPopup: () => void;
}

