import apiClient, { tokenHelper } from "./axiosConfig";
import type { ApiResponse } from "./api";

export type LoginRequest = {
  login: string;
  password: string;
};

export type RegisterRequest = {
  username: string;
  email: string;
  password: string;
};

export type User = {
  id: number;
  username: string;
  email: string;
  createdAt: Date;
  updatedAt: Date;
};

export type AuthResponse = User & {
  accessToken: string;
};

export const authApi = {
  // Login
  login: async (
    credentials: LoginRequest,
  ): Promise<ApiResponse<User>> => {
    try {
      const response = await apiClient.post("/auth/login", credentials);
      const res: ApiResponse<AuthResponse> = response.data;
      if (res.success) {
        tokenHelper.setAccessToken(res.data!.accessToken);
      }
      return res;
    } catch (error: any) {
      return {
        success: false,
        message: error.response?.data?.message || "Login failed",
      };
    }
  },

  // Register
  register: async (
    userData: RegisterRequest,
  ): Promise<ApiResponse<User>> => {
    try {
      const response = await apiClient.post("/auth/register", userData);
      const res: ApiResponse<AuthResponse> = response.data;
      if (res.success) {
        tokenHelper.setAccessToken(res.data!.accessToken);
      }
      return res;
    } catch (error: any) {
      return {
        success: false,
        message: error.response?.data?.message || "Registration failed",
      };
    }
  },

  // Get current user
  getCurrentUser: async (): Promise<ApiResponse<User>> => {
    try {
      const response = await apiClient.get("/auth/me");
      const res: ApiResponse<User> = response.data;
      return res;
    } catch (error: any) {
      return {
        success: false,
        message: error.response?.data?.message || "Failed to fetch user",
      };
    }
  },

  // Logout
  logout: async (): Promise<ApiResponse<undefined>> => {
    try {
      await apiClient.post("/auth/logout");
      tokenHelper.clearAccessToken();
      return { success: true };
    } catch (error) {
      tokenHelper.clearAccessToken();
      return { success: false };
    }
  },

  // Logout from all devices
  logoutAll: async (): Promise<ApiResponse<undefined>> => {
    try {
      await apiClient.post("/auth/logout-all");
      tokenHelper.clearAccessToken();
      return { success: true };
    } catch (error) {
      tokenHelper.clearAccessToken();
      return { success: false };
    }
  },
};
