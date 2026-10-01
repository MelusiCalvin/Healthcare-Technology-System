import axios, { AxiosError } from "axios";
import type { AuthenticatedUser } from "@/features/auth/auth-api";
import { AUTH_SESSION_STORAGE_KEY } from "@/features/auth/auth-session";
import type { ApiProblem } from "@/types/api";

export const apiClient = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_BASE_URL,
  timeout: 15_000,
  headers: {
    Accept: "application/json",
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

apiClient.interceptors.request.use((config) => {
  if (typeof window !== "undefined") {
    try {
      const stored = window.sessionStorage.getItem(AUTH_SESSION_STORAGE_KEY);
      if (stored) {
        const session = JSON.parse(stored) as AuthenticatedUser;
        if (session.accessToken) {
          config.headers.set("Authorization", `Bearer ${session.accessToken}`);
        }
      }
    } catch {
      window.sessionStorage.removeItem(AUTH_SESSION_STORAGE_KEY);
    }
  }
  return config;
});

export class ApiClientError extends Error {
  readonly status?: number;
  readonly correlationId?: string;
  readonly violations?: Record<string, string>;

  constructor(message: string, problem?: ApiProblem) {
    super(message);
    this.name = "ApiClientError";
    this.status = problem?.status;
    this.correlationId = problem?.correlationId;
    this.violations = problem?.violations;
  }
}

export function toApiClientError(error: unknown): ApiClientError {
  if (!axios.isAxiosError(error)) {
    return new ApiClientError("We could not complete that request. Please try again.");
  }

  const axiosError = error as AxiosError<ApiProblem>;
  const problem = axiosError.response?.data;
  return new ApiClientError(
    problem?.detail ?? "We could not complete that request. Please try again.",
    problem,
  );
}
