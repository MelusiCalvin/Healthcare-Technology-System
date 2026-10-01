import { apiClient, toApiClientError } from "@/lib/api-client";

interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  userId: string;
  username: string;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  roles: string[];
}

export interface AuthenticatedUser {
  id: string;
  username: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  roles: string[];
  accessToken: string;
  refreshToken: string;
}

export const authApi = {
  async login(input: { username: string; password: string }): Promise<AuthenticatedUser> {
    try {
      const response = await apiClient.post<LoginResponse>("/auth/login", {
        usernameOrEmail: input.username,
        password: input.password,
      });
      return {
        id: response.data.userId,
        username: response.data.username,
        firstName: response.data.firstName ?? undefined,
        lastName: response.data.lastName ?? undefined,
        email: response.data.email ?? undefined,
        roles: response.data.roles,
        accessToken: response.data.accessToken,
        refreshToken: response.data.refreshToken,
      };
    } catch (error) {
      throw toApiClientError(error);
    }
  },
};
