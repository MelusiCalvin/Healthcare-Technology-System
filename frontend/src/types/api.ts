export interface ApiEnvelope<T> {
  data: T;
  timestamp: string;
  correlationId: string;
}

export interface ApiProblem {
  type?: string;
  title?: string;
  status?: number;
  detail?: string;
  instance?: string;
  code?: string;
  correlationId?: string;
  violations?: Record<string, string>;
}

export interface Patient {
  id: string;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  sex: string;
  phoneNumber?: string | null;
  email?: string | null;
}

export interface CreatePatientInput {
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  sex: "FEMALE" | "MALE" | "INTERSEX" | "UNSPECIFIED";
  phoneNumber?: string;
  email?: string;
}
