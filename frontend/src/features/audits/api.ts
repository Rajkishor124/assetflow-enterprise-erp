import { apiClient } from '@/lib/api-client';
import { 
  AuditCycleRequest, 
  AuditCycleResponse, 
  PaginatedAuditCycleResponse 
} from './types';

export const auditsApi = {
  getAllAuditCycles: async (page = 0, size = 10): Promise<PaginatedAuditCycleResponse> => {
    const response = await apiClient.get<PaginatedAuditCycleResponse>(
      `/api/v1/audits?page=${page}&size=${size}&sort=createdAt,desc`
    );
    return response.data;
  },

  createAuditCycle: async (data: AuditCycleRequest): Promise<AuditCycleResponse> => {
    const response = await apiClient.post<AuditCycleResponse>('/api/v1/audits', data);
    return response.data;
  },

  completeAuditCycle: async (id: number): Promise<AuditCycleResponse> => {
    const response = await apiClient.post<AuditCycleResponse>(`/api/v1/audits/${id}/complete`);
    return response.data;
  },
};
