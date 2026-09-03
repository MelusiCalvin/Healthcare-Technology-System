import type { Metadata } from "next";
import { UserPlus } from "lucide-react";
import Link from "next/link";
import { PatientList } from "@/features/patients/patient-list";
import { Button } from "@/components/ui/button";

export const metadata: Metadata = { title: "Patients" };

export default function PatientsPage() {
  return (
    <div className="space-y-6">
      <section className="flex flex-col justify-between gap-4 sm:flex-row sm:items-end">
        <div>
          <p className="text-sm font-semibold text-primary-600 dark:text-primary-400">Patient management</p>
          <h1 className="mt-1 text-2xl font-extrabold tracking-tight text-slate-950 dark:text-white sm:text-3xl">Patient directory</h1>
          <p className="mt-2 text-sm text-slate-500 dark:text-slate-400">Search and manage authorised patient records for your facility.</p>
        </div>
        <Link href="/patients/new"><Button><UserPlus className="h-4 w-4" /> Register patient</Button></Link>
      </section>
      <PatientList />
    </div>
  );
}
