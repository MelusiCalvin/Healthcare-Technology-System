import type { Metadata } from "next";
import { PatientSummary } from "@/features/patients/patient-summary";

export const metadata: Metadata = { title: "Patient record" };

export default async function PatientDetailPage({ params }: { params: Promise<{ patientId: string }> }) {
  const { patientId } = await params;
  return <PatientSummary patientId={patientId} />;
}
