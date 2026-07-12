import { apiClient } from '@/lib/api-client';
import { 
  ResourceBookingRequest, 
  ResourceBookingResponse, 
  PaginatedBookingResponse,
  PaginatedResourceResponse
} from './types';

export const bookingApi = {
  getAllBookings: async (page = 0, size = 10): Promise<PaginatedBookingResponse> => {
    const response = await apiClient.get<PaginatedBookingResponse>(
      `/api/v1/bookings?page=${page}&size=${size}&sort=startTime,desc`
    );
    return response.data;
  },

  getAllResources: async (page = 0, size = 100): Promise<PaginatedResourceResponse> => {
    const response = await apiClient.get<PaginatedResourceResponse>(
      `/api/v1/resources?page=${page}&size=${size}&sort=name,asc`
    );
    return response.data;
  },

  createBooking: async (data: ResourceBookingRequest): Promise<ResourceBookingResponse> => {
    const response = await apiClient.post<ResourceBookingResponse>('/api/v1/bookings', data);
    return response.data;
  },

  cancelBooking: async (id: number): Promise<ResourceBookingResponse> => {
    const response = await apiClient.post<ResourceBookingResponse>(`/api/v1/bookings/${id}/cancel`);
    return response.data;
  },
};
