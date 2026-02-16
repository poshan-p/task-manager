import React, { createContext, useContext, useState, useEffect } from "react";
import {
  authApi,
  type LoginRequest,
  type RegisterRequest,
  type User,
} from "../api/authApi";
import { tokenHelper } from "../api/axiosConfig";
import type { ApiResponse } from "../api/api";

type AuthContextType = {
    user?: User;
    login: (credentials: LoginRequest) => Promise<ApiResponse<User>>;
    register: (userData: RegisterRequest) => Promise<ApiResponse<User>>;
    logout: () => Promise<void>;
    logoutAll: () => Promise<void>;
    loading: boolean;
    isAuthenticated: boolean;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

type AuthContextProviderProps = {
  children: React.ReactNode;
};

export const AuthProvider = ({ children }: AuthContextProviderProps) => {
  const [user, setUser] = useState<User | undefined>();
  const [loading, setLoading] = useState<boolean>(true);
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

  // Load user on mount
  useEffect(() => {
    const loadUser = async () => {
      const accessToken = tokenHelper.getAccessToken();

      if (accessToken) {
        const result = await authApi.getCurrentUser();
        console.log(result);
        if (result.success) {
          setUser(result.data);
          setIsAuthenticated(true);
        } else {
          setUser(undefined);
          setIsAuthenticated(false);
        }
      }

      setLoading(false);
    };

    loadUser();
  }, []);

  const login = async (credentials: LoginRequest) => {
    const result = await authApi.login(credentials);
    if (result.success) {
      setUser(result.data);
      setIsAuthenticated(true);
    }
    return result;
  };

  const register = async (userData: RegisterRequest) => {
    const result = await authApi.register(userData);
    if (result.success) {
      setUser(result.data);
      setIsAuthenticated(true);
    }
    return result;
  };

  const logout = async () => {
    await authApi.logout();
    setUser(undefined);
    setIsAuthenticated(false);
  };

  const logoutAll = async () => {
    await authApi.logoutAll();
    setUser(undefined);
    setIsAuthenticated(false);
  };

  const value = {
    user,
    login,
    register,
    logout,
    logoutAll,
    loading,
    isAuthenticated,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
