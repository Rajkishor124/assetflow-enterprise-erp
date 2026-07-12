import { apiClient } from '../../lib/api-client';
import { ApiResponse, PageResponse } from '../../lib/types';
import {
  DepartmentRequest,
  DepartmentSummaryResponse,
  DepartmentDetailResponse,
  UserRequest,
  UserSummaryResponse,
  UserDetailResponse,
} from './types';

export const departmentApi = {
  getAll: async (page = 0, size = 10, search?: string): Promise<ApiResponse<PageResponse<DepartmentSummaryResponse>>> => {
    const response = await apiClient.get<ApiResponse<PageResponse<DepartmentSummaryResponse>>>('/departments', {
      params: { page, size, search },
    });
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<DepartmentDetailResponse>> => {
    const response = await apiClient.get<ApiResponse<DepartmentDetailResponse>>(`/departments/${id}`);
    return response.data;
  },

  create: async (data: DepartmentRequest): Promise<ApiResponse<DepartmentDetailResponse>> => {
    const response = await apiClient.post<ApiResponse<DepartmentDetailResponse>>('/departments', data);
    return response.data;
  },

  update: async (id: number, data: DepartmentRequest): Promise<ApiResponse<DepartmentDetailResponse>> => {
    const response = await apiClient.put<ApiResponse<DepartmentDetailResponse>>(`/departments/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await apiClient.delete<ApiResponse<void>>(`/departments/${id}`);
    return response.data;
  },
};

export const userApi = {
  getAll: async (page = 0, size = 10, search?: string, departmentId?: number, roleName?: string): Promise<ApiResponse<PageResponse<UserSummaryResponse>>> => {
    const response = await apiClient.get<ApiResponse<PageResponse<UserSummaryResponse>>>('/users', {
      params: { page, size, search, departmentId, roleName },
    });
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<UserDetailResponse>> => {
    const response = await apiClient.get<ApiResponse<UserDetailResponse>>(`/users/${id}`);
    return response.data;
  },

  create: async (data: UserRequest): Promise<ApiResponse<UserDetailResponse>> => {
    const response = await apiClient.post<ApiResponse<UserDetailResponse>>('/users', data);
    return response.data;
  },

  update: async (id: number, data: UserRequest): Promise<ApiResponse<UserDetailResponse>> => {
    const response = await apiClient.put<ApiResponse<UserDetailResponse>>(`/users/${id}`, data);
    return response.data;
  },

  updateRole: async (id: number, data: { role: string }): Promise<ApiResponse<UserDetailResponse>> => {
    const response = await apiClient.put<ApiResponse<UserDetailResponse>>(`/users/${id}/role`, data);
    return response.data;
  },
  
  delete: async (id: number): Promise<ApiResponse<void>> => {
    const response = await apiClient.delete<ApiResponse<void>>(`/users/${id}`);
    return response.data;
  },
};
