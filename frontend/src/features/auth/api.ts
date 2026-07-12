import { apiClient } from '../../lib/api-client';
import { LoginInput, SignupInput } from '../../lib/validators';
import { AuthResponse, SignupResponse } from './types';

export const authApi = {
  login: async (data: LoginInput): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>('/auth/login', data);
    return response.data;
  },

  signup: async (data: SignupInput): Promise<SignupResponse> => {
    const response = await apiClient.post<SignupResponse>('/auth/signup', data);
    return response.data;
  },

  logout: async (refreshToken: string): Promise<void> => {
    const response = await apiClient.post('/auth/logout', { refreshToken });
    return response.data;
  },
};
