export interface BookableResourceResponse {
  id: number;
  name: string;
  resourceType: string;
  description?: string;
  categoryId?: number;
  categoryName?: string;
}

export interface ResourceBookingResponse {
  id: number;
  resourceId: number;
  resourceName: string;
  resourceType: string;
  userId: number;
  userName: string;
  startTime: string;
  endTime: string;
  bookingStatus: 'PENDING' | 'CONFIRMED' | 'CANCELLED' | 'COMPLETED';
  purpose?: string;
}

export interface ResourceBookingRequest {
  resourceId: number;
  userId: number;
  startTime: string;
  endTime: string;
  purpose?: string;
}

export interface PaginatedResourceResponse {
  content: BookableResourceResponse[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface PaginatedBookingResponse {
  content: ResourceBookingResponse[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
