"use client";

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { patientApi } from "@/features/patients/patient-api";

export const patientQueryKeys = {
  all: ["patients"] as const,
};

export function usePatients() {
  return useQuery({
    queryKey: patientQueryKeys.all,
    queryFn: patientApi.list,
  });
}

export function useCreatePatient() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: patientApi.create,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: patientQueryKeys.all }),
  });
}

export function usePatient(patientId: string) {
  return useQuery({
    queryKey: [...patientQueryKeys.all, patientId],
    queryFn: () => patientApi.get(patientId),
    enabled: Boolean(patientId),
  });
}
