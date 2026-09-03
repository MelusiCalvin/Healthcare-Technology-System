import { apiClient, toApiClientError } from "@/lib/api-client";
import type { CreatePatientInput, Patient } from "@/types/api";

function unwrap<T>(payload: T | { data: T }): T {
  return typeof payload === "object" && payload !== null && "data" in payload ? payload.data : payload;
}

export const patientApi = {
  async list(): Promise<Patient[]> {
    try {
      const response = await apiClient.get<Patient[] | { data: Patient[] }>("/patients");
      return unwrap(response.data);
    } catch (error) {
      throw toApiClientError(error);
    }
  },

  async create(input: CreatePatientInput): Promise<Patient> {
    try {
      const response = await apiClient.post<Patient | { data: Patient }>("/patients", input);
      return unwrap(response.data);
    } catch (error) {
      throw toApiClientError(error);
    }
  },

  async get(patientId: string): Promise<Patient> {
    try {
      const response = await apiClient.get<Patient | { data: Patient }>(`/patients/${patientId}`);
      return unwrap(response.data);
    } catch (error) {
      throw toApiClientError(error);
    }
  },
};
