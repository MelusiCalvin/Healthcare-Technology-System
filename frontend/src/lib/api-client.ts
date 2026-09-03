import axios, { AxiosError } from "axios";
import type { ApiProblem } from "@/types/api";

export const apiClient = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080/api/v1",
  timeout: 15_000,
  headers: {
    Accept: "application/json",
    "Content-Type": "application/json",
  },
  withCredentials: true,
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
