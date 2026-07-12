
export interface AuthResponse {
  success: boolean;
  message: string;
  data: {
    accessToken: string;
    refreshToken: string;
  };
}

export interface SignupResponse {
  success: boolean;
  message: string;
  data: string | null;
  timestamp: string;
}
