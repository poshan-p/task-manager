import axios from "axios";
import { jwtDecode } from "jwt-decode";
import type { AxiosInstance } from "axios";

class TokenHelper {
  private TOKEN_KEY: string = "access_token";

  public getAccessToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  public setAccessToken(accessToken: string | null) {
    if (accessToken) {
      localStorage.setItem(this.TOKEN_KEY, accessToken);
    }
  }

  public clearAccessToken() {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  public isTokenExpired(token: string): boolean {
    if (!token) return true;

    try {
      const decoded = jwtDecode(token);
      const currentTime = Date.now() / 1000;
      if (!decoded.exp) return true;
      return decoded.exp < currentTime + 30;
    } catch (error) {
      return true;
    }
  }
}

class RefreshTokenHelper {
  private tokenHelper: TokenHelper;
  private refreshing: boolean;
  private failedQueue: any[];

  constructor(tokenHelper: TokenHelper) {
    this.tokenHelper = tokenHelper;
    this.refreshing = false;
    this.failedQueue = [];
  }

  public isRefreshing(): boolean {
    return this.refreshing;
  }

  public setRefreshing(refreshing: boolean) {
    this.refreshing = refreshing;
  }

  public processQueue(error: any, token: any) {
    this.failedQueue.forEach((prom: PromiseConstructor) => {
      if (error) {
        prom.reject(error);
      } else {
        prom.resolve(token);
      }
    });

    this.failedQueue = [];
  }

  public async refreshAccessToken(): Promise<string> {
    try {
      const response = await axios.post(`${API_BASE_URL}/auth/refresh`, null, {
        withCredentials: true,
      });

      const { accessToken } = response.data.data;
      this.tokenHelper.setAccessToken(accessToken);

      return accessToken;
    } catch (error) {
      this.tokenHelper.clearAccessToken();
      throw error;
    }
  }

  public addToFailQueue(item: any) {
    this.failedQueue.push(item);
  }
}

class ApiClient {
  private apiBaseUrl: string;
  private tokenHelper: TokenHelper;
  private refreshTokenHelper: RefreshTokenHelper;
  private axiosClient: AxiosInstance;

  constructor(
    apiBaseUrl: string,
    tokenHelper: TokenHelper,
    refreshTokenHelper: RefreshTokenHelper,
  ) {
    this.tokenHelper = tokenHelper;
    this.refreshTokenHelper = refreshTokenHelper;
    this.apiBaseUrl = apiBaseUrl;
    this.axiosClient = axios.create({
      baseURL: this.apiBaseUrl,
      headers: {
        "Content-Type": "application/json",
      },
      withCredentials: true,
    });

    // request interceptor
    this.axiosClient.interceptors.request.use(
      async (config) => {
        let accessToken = this.tokenHelper.getAccessToken();
        // Refresh if token is expired
        if (accessToken && this.tokenHelper.isTokenExpired(accessToken)) {
          if (!this.refreshTokenHelper.isRefreshing()) {
            try {
              accessToken = await this.refreshTokenHelper.refreshAccessToken();
            } catch (error) {
              this.tokenHelper.clearAccessToken();
              window.location.href = "/login";
              return Promise.reject(error);
            }
          }
        }

        if (accessToken) {
          config.headers.Authorization = `Bearer ${accessToken}`;
        }

        return config;
      },
      (error) => {
        return Promise.reject(error);
      },
    );

    this.axiosClient.interceptors.response.use(
      (response) => response,
      async (error) => {
        const originalRequest = error.config;

        if (error.response?.status === 401 && !originalRequest._retry) {
          if (this.refreshTokenHelper.isRefreshing()) {
            return new Promise((resolve, reject) => {
              this.refreshTokenHelper.addToFailQueue({ resolve, reject });
            })
              .then((token) => {
                originalRequest.headers.Authorization = `Bearer ${token}`;
                return this.axiosClient(originalRequest);
              })
              .catch((err) => Promise.reject(err));
          }

          originalRequest._retry = true;
          this.refreshTokenHelper.setRefreshing(true);

          try {
            const newAccessToken =
              await this.refreshTokenHelper.refreshAccessToken();
            this.refreshTokenHelper.processQueue(null, newAccessToken);
            originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
            return this.axiosClient(originalRequest);
          } catch (refreshError) {
            this.refreshTokenHelper.processQueue(refreshError, null);
            this.tokenHelper.clearAccessToken();
            window.location.href = "/login";
            return Promise.reject(refreshError);
          } finally {
            this.refreshTokenHelper.setRefreshing(false);
          }
        }

        return Promise.reject(error);
      },
    );
  }

  public getClient(): AxiosInstance {
    return this.axiosClient;
  }
}

const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api/v1";

export const tokenHelper = new TokenHelper();
export const refreshTokenHelper = new RefreshTokenHelper(tokenHelper);
const apiClient = new ApiClient(API_BASE_URL, tokenHelper, refreshTokenHelper);

export default apiClient.getClient();
