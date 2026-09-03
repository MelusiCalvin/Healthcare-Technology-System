import { apiClient, toApiClientError } from "@/lib/api-client";

interface LegacyLoginResponse {
  userId: string;
  username: string;
  roles: string[];
}

export interface AuthenticatedUser {
  id: string;
  username: string;
  roles: string[];
}

/**
 * The API adapter is intentionally the only frontend entry point for authentication.
 * The current backend returns identity metadata; the planned JWT/refresh-token contract
 * can replace this mapping without changing UI forms or pages.
 */
export const authApi = {
  async login(input: { username: string; password: string }): Promise<AuthenticatedUser> {
    try {
      const response = await apiClient.post<LegacyLoginResponse>("/auth/login", input);
      return {
        id: response.data.userId,
        username: response.data.username,
        roles: response.data.roles,
      };
    } catch (error) {
      throw toApiClientError(error);
    }
  },
};
