import { User } from '../../lib/auth';

export interface AuthResponse {
  success: boolean;
  message: string;
  data: {
    accessToken: string;
    refreshToken: string;
    user: User;
  };
  timestamp: string;
}

export interface SignupResponse {
  success: boolean;
  message: string;
  data: User;
  timestamp: string;
}
