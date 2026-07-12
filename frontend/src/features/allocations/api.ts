import { apiClient } from '@/lib/api-client';
import { AssetAllocationRequest, AssetAllocationResponse, PaginatedAllocationResponse } from './types';

export const allocationApi = {
  getAll: async (page = 0, size = 10): Promise<PaginatedAllocationResponse> => {
    const response = await apiClient.get<PaginatedAllocationResponse>(
      `/api/v1/allocations?page=${page}&size=${size}&sort=allocationDate,desc`
    );
    return response.data;
  },

  allocate: async (data: AssetAllocationRequest): Promise<AssetAllocationResponse> => {
    const response = await apiClient.post<AssetAllocationResponse>('/api/v1/allocations', data);
    return response.data;
  },

  returnAsset: async (id: number, notes?: string): Promise<AssetAllocationResponse> => {
    const url = `/api/v1/allocations/${id}/return` + (notes ? `?notes=${encodeURIComponent(notes)}` : '');
    const response = await apiClient.post<AssetAllocationResponse>(url);
    return response.data;
  },
};
