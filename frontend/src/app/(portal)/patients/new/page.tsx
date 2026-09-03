import type { Metadata } from "next";
import { PatientRegistrationForm } from "@/features/patients/patient-registration-form";

export const metadata: Metadata = { title: "Register patient" };

export default function NewPatientPage() {
  return (
    <div className="mx-auto max-w-4xl">
      <section className="mb-6">
        <p className="text-sm font-semibold text-primary-600 dark:text-primary-400">Patient management</p>
        <h1 className="mt-1 text-2xl font-extrabold tracking-tight text-slate-950 dark:text-white sm:text-3xl">Register a patient</h1>
        <p className="mt-2 text-sm text-slate-500 dark:text-slate-400">Verify the patient&apos;s identity before creating a clinical record. Sensitive identifiers will be added in the next patient-identity release.</p>
      </section>
      <PatientRegistrationForm />
    </div>
  );
}
