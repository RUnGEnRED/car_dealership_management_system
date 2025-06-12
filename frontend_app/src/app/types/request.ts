export interface ServiceRequestPayload {
  vehicleId: string | number;
  customerNotes: string;
}


export interface InspectionRequestPayload {
  vehicleId: string | number;
  customerNotes: string;
}

export interface RequestResponse {
  message: string;
  requestId?: string; 
}
