import { apiClient } from '../../lib/api-client';
import { ApiResponse, PageResponse } from '../../lib/types';
import {
  AssetCategoryRequest,
  AssetCategorySummaryResponse,
  AssetCategoryDetailResponse,
  AssetRequest,
  AssetSummaryResponse,
  AssetDetailResponse,
} from './types';

export const assetCategoryApi = {
  getAll: async (page = 0, size = 10, search?: string): Promise<ApiResponse<PageResponse<AssetCategorySummaryResponse>>> => {
    const response = await apiClient.get<ApiResponse<PageResponse<AssetCategorySummaryResponse>>>('/asset-categories', {
      params: { page, size, search },
    });
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<AssetCategoryDetailResponse>> => {
    const response = await apiClient.get<ApiResponse<AssetCategoryDetailResponse>>(`/asset-categories/${id}`);
    return response.data;
  },

  create: async (data: AssetCategoryRequest): Promise<ApiResponse<AssetCategoryDetailResponse>> => {
    const response = await apiClient.post<ApiResponse<AssetCategoryDetailResponse>>('/asset-categories', data);
    return response.data;
  },

  update: async (id: number, data: AssetCategoryRequest): Promise<ApiResponse<AssetCategoryDetailResponse>> => {
    const response = await apiClient.put<ApiResponse<AssetCategoryDetailResponse>>(`/asset-categories/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await apiClient.delete<ApiResponse<void>>(`/asset-categories/${id}`);
    return response.data;
  },
};

export const assetApi = {
  getAll: async (
    page = 0, 
    size = 10, 
    search?: string, 
    categoryId?: number, 
    status?: string, 
    assignedUserId?: number
  ): Promise<ApiResponse<PageResponse<AssetSummaryResponse>>> => {
    const response = await apiClient.get<ApiResponse<PageResponse<AssetSummaryResponse>>>('/assets', {
      params: { page, size, search, categoryId, status, assignedUserId },
    });
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<AssetDetailResponse>> => {
    const response = await apiClient.get<ApiResponse<AssetDetailResponse>>(`/assets/${id}`);
    return response.data;
  },

  create: async (data: AssetRequest): Promise<ApiResponse<AssetDetailResponse>> => {
    const response = await apiClient.post<ApiResponse<AssetDetailResponse>>('/assets', data);
    return response.data;
  },

  update: async (id: number, data: AssetRequest): Promise<ApiResponse<AssetDetailResponse>> => {
    const response = await apiClient.put<ApiResponse<AssetDetailResponse>>(`/assets/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await apiClient.delete<ApiResponse<void>>(`/assets/${id}`);
    return response.data;
  },
};
