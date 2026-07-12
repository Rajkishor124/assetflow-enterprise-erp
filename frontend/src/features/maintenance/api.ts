import { apiClient } from '@/lib/api-client';
import { 
  MaintenanceRecordRequest, 
  MaintenanceRecordResponse, 
  PaginatedMaintenanceResponse
} from './types';

export const maintenanceApi = {
  getAllMaintenanceRequests: async (page = 0, size = 10): Promise<PaginatedMaintenanceResponse> => {
    const response = await apiClient.get<PaginatedMaintenanceResponse>(
      `/api/v1/maintenance?page=${page}&size=${size}&sort=createdAt,desc`
    );
    return response.data;
  },

  createMaintenanceRequest: async (data: MaintenanceRecordRequest): Promise<MaintenanceRecordResponse> => {
    const response = await apiClient.post<MaintenanceRecordResponse>('/api/v1/maintenance', data);
    return response.data;
  },

  completeMaintenance: async (id: number): Promise<MaintenanceRecordResponse> => {
    const response = await apiClient.post<MaintenanceRecordResponse>(`/api/v1/maintenance/${id}/complete`);
    return response.data;
  },
};
