import axios from 'axios';
import { getAccessToken, getRefreshToken, setAccessToken, setRefreshToken, clearAuth } from './auth';

export const apiClient = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
});

apiClient.interceptors.request.use((config) => {
  const token = getAccessToken();
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // Avoid infinite loops
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      const refreshToken = getRefreshToken();

      if (refreshToken) {
        try {
          // Attempt to refresh token
          const refreshResponse = await axios.post(
            `${apiClient.defaults.baseURL}/auth/refresh-token`,
            { refreshToken }
          );

          if (refreshResponse.data?.success && refreshResponse.data?.data) {
            const { accessToken: newAccessToken, refreshToken: newRefreshToken } = refreshResponse.data.data;
            
            // Save new tokens
            setAccessToken(newAccessToken);
            setRefreshToken(newRefreshToken);

            // Update header and retry original request
            originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
            return apiClient(originalRequest);
          }
        } catch (refreshError) {
          // Refresh token failed or expired
        }
      }

      // If refresh failed or no refresh token, log out
      clearAuth();
      if (typeof window !== 'undefined') {
        window.location.href = '/login';
      }
    }

    return Promise.reject(error);
  }
);
