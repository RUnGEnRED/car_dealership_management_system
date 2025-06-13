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
  vehicleVin?: string; // Optional, as VIN might not always be immediately available or relevant for all request types
  assignedEmployeeId: number | null; // Can be null if not yet assigned to an employee
  assignedEmployeeUsername: string | null; // Can be null if not yet assigned
  requestType: RequestType;
  status: RequestStatus;
  customerNotes: string;
  createdAt: string; // ISO 8601 date string
  updatedAt: string; // ISO 8601 date string
}
