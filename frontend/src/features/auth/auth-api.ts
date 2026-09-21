import { apiClient, toApiClientError } from "@/lib/api-client";

interface AuthenticationResponse {
  accessToken: string;
  refreshToken: string;
  userId: string;
  username: string;
  roles: string[];
}

export interface AuthenticatedUser {
  id: string;
  username: string;
  roles: string[];
}

export interface AuthSession {
  user: AuthenticatedUser;
  accessToken: string;
  refreshToken: string;
}

/**
 * The API adapter is intentionally the only frontend entry point for authentication.
 */
export const authApi = {
  async login(input: { username: string; password: string }): Promise<AuthSession> {
    try {
      const response = await apiClient.post<AuthenticationResponse>("/auth/login", {
        usernameOrEmail: input.username,
        password: input.password,
      });
      return {
        user: {
          id: response.data.userId,
          username: response.data.username,
          roles: response.data.roles,
        },
        accessToken: response.data.accessToken,
        refreshToken: response.data.refreshToken,
      };
    } catch (error) {
      throw toApiClientError(error);
    }
  },

  async register(input: {
    firstName: string;
    lastName: string;
    username: string;
    email: string;
    sex: string;
    password: string;
  }): Promise<AuthSession> {
    try {
      const response = await apiClient.post<AuthenticationResponse>("/auth/register", input);
      return {
        user: {
          id: response.data.userId,
          username: response.data.username,
          roles: response.data.roles,
        },
        accessToken: response.data.accessToken,
        refreshToken: response.data.refreshToken,
      };
    } catch (error) {
      throw toApiClientError(error);
    }
  },

  async logout(refreshToken: string): Promise<void> {
    try {
      await apiClient.post("/auth/logout", { refreshToken });
    } catch (error) {
      throw toApiClientError(error);
    }
  },
};
