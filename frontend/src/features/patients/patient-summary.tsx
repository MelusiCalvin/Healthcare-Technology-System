"use client";

import { AlertCircle, CalendarPlus, ChevronLeft, LoaderCircle, Phone, UserRound } from "lucide-react";
import Link from "next/link";
import { usePatient } from "@/features/patients/patient-hooks";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { EmptyState } from "@/components/ui/empty-state";
import { initials } from "@/lib/utils";

export function PatientSummary({ patientId }: { patientId: string }) {
  const { data: patient, error, isLoading } = usePatient(patientId);

  if (isLoading) {
    return <Card><EmptyState icon={LoaderCircle} title="Loading patient record" description="Retrieving the authorised patient summary." /></Card>;
  }

  if (error || !patient) {
    return <Card><EmptyState icon={AlertCircle} title="Patient record unavailable" description={error?.message ?? "The requested patient record could not be found."} action={<Link href="/patients" className="inline-flex h-10 items-center gap-2 rounded-xl bg-primary-600 px-4 text-sm font-semibold text-white hover:bg-primary-700"><ChevronLeft className="h-4 w-4" /> Back to patients</Link>} /></Card>;
  }

  return (
    <div className="space-y-6">
      <Card>
        <CardContent className="flex flex-col gap-5 p-6 sm:flex-row sm:items-center sm:justify-between">
          <div className="flex min-w-0 items-center gap-4">
            <span className="grid h-16 w-16 shrink-0 place-items-center rounded-2xl bg-primary-100 text-lg font-extrabold text-primary-700 dark:bg-primary-900 dark:text-primary-200">{initials(`${patient.firstName} ${patient.lastName}`)}</span>
            <div className="min-w-0"><div className="flex flex-wrap items-center gap-2"><h2 className="truncate text-xl font-extrabold text-slate-950 dark:text-white">{patient.firstName} {patient.lastName}</h2><Badge variant="success">Active</Badge></div><p className="mt-1 text-sm text-slate-500 dark:text-slate-400">Patient record · {patient.id}</p></div>
          </div>
          <button type="button" className="inline-flex h-10 items-center justify-center gap-2 rounded-xl bg-primary-600 px-4 text-sm font-semibold text-white hover:bg-primary-700"><CalendarPlus className="h-4 w-4" /> Book appointment</button>
        </CardContent>
      </Card>
      <div className="grid gap-6 lg:grid-cols-2">
        <Card><CardHeader><div><h2 className="text-base font-bold text-slate-900 dark:text-white">Patient details</h2><p className="mt-1 text-sm text-slate-500 dark:text-slate-400">Core details recorded during registration.</p></div></CardHeader><CardContent className="grid gap-5 pt-4 sm:grid-cols-2"><Detail label="Date of birth" value={new Intl.DateTimeFormat("en-ZA", { dateStyle: "long" }).format(new Date(patient.dateOfBirth))} /><Detail label="Recorded sex" value={patient.sex.replaceAll("_", " ")} /><Detail label="Mobile number" value={patient.phoneNumber ?? "Not recorded"} /><Detail label="Email" value={patient.email ?? "Not recorded"} /></CardContent></Card>
        <Card><CardHeader><div><h2 className="text-base font-bold text-slate-900 dark:text-white">Clinical timeline</h2><p className="mt-1 text-sm text-slate-500 dark:text-slate-400">Clinical history becomes available after the first encounter.</p></div></CardHeader><CardContent><EmptyState icon={UserRound} title="No encounters yet" description="This patient has no clinical encounters in the authorised timeline." /></CardContent></Card>
      </div>
      <Link href="/patients" className="inline-flex h-10 items-center gap-2 rounded-xl px-3 text-sm font-semibold text-slate-600 hover:bg-white hover:text-slate-950 dark:text-slate-300 dark:hover:bg-slate-900 dark:hover:text-white"><ChevronLeft className="h-4 w-4" /> Back to patients</Link>
    </div>
  );
}

function Detail({ label, value }: { label: string; value: string }) {
  return <div><p className="text-xs font-bold uppercase tracking-wide text-slate-400">{label}</p><p className="mt-1.5 text-sm font-semibold capitalize text-slate-800 dark:text-slate-100">{value}</p></div>;
}
