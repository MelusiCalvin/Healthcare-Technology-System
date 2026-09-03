"use client";

import { AlertCircle, ArrowUpRight, LoaderCircle, Search, UserPlus, UsersRound } from "lucide-react";
import Link from "next/link";
import { useMemo, useState } from "react";
import { EmptyState } from "@/components/ui/empty-state";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { usePatients } from "@/features/patients/patient-hooks";
import { initials } from "@/lib/utils";

function patientAge(dateOfBirth: string) {
  const birthDate = new Date(dateOfBirth);
  const today = new Date();
  let age = today.getFullYear() - birthDate.getFullYear();
  const monthDifference = today.getMonth() - birthDate.getMonth();
  if (monthDifference < 0 || (monthDifference === 0 && today.getDate() < birthDate.getDate())) {
    age--;
  }
  return age;
}

export function PatientList() {
  const { data: patients, error, isLoading, isFetching } = usePatients();
  const [search, setSearch] = useState("");

  const filteredPatients = useMemo(() => {
    const normalizedSearch = search.trim().toLowerCase();
    if (!normalizedSearch) return patients ?? [];

    return (patients ?? []).filter((patient) =>
      `${patient.firstName} ${patient.lastName} ${patient.email ?? ""} ${patient.phoneNumber ?? ""}`.toLowerCase().includes(normalizedSearch),
    );
  }, [patients, search]);

  if (isLoading) {
    return (
      <Card>
        <EmptyState icon={LoaderCircle} title="Loading patients" description="Retrieving the latest authorised patient directory." className="[&>div:first-child]:animate-spin" />
      </Card>
    );
  }

  if (error) {
    return (
      <Card>
        <EmptyState
          icon={AlertCircle}
          title="Patient directory is unavailable"
          description={error.message}
          action={<Link href="/patients/new"><Button><UserPlus className="h-4 w-4" /> Register patient</Button></Link>}
        />
      </Card>
    );
  }

  return (
    <Card className="overflow-hidden">
      <div className="flex flex-col justify-between gap-4 border-b border-slate-100 p-5 sm:flex-row sm:items-center sm:px-6 dark:border-slate-800">
        <label className="relative w-full sm:max-w-md">
          <Search className="pointer-events-none absolute left-3.5 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
          <input
            value={search}
            onChange={(event) => setSearch(event.target.value)}
            placeholder="Search by name, email or mobile number"
            className="h-10 w-full rounded-xl border border-slate-200 bg-slate-50 pl-10 pr-4 text-sm text-slate-900 outline-none transition focus:border-primary-500 focus:bg-white focus:ring-2 focus:ring-primary-500/15 dark:border-slate-700 dark:bg-slate-800 dark:text-white dark:focus:bg-slate-900"
          />
        </label>
        <p className="text-sm font-medium text-slate-500 dark:text-slate-400">{isFetching ? "Refreshing…" : `${filteredPatients.length} patient${filteredPatients.length === 1 ? "" : "s"}`}</p>
      </div>

      {filteredPatients.length === 0 ? (
        <EmptyState
          icon={UsersRound}
          title={search ? "No matching patients" : "No patients registered yet"}
          description={search ? "Try another name, email address, or mobile number." : "Register the first patient to start building the digital health record."}
          action={!search ? <Link href="/patients/new"><Button><UserPlus className="h-4 w-4" /> Register patient</Button></Link> : undefined}
        />
      ) : (
        <div className="overflow-x-auto">
          <table className="w-full min-w-[760px] text-left">
            <thead className="border-b border-slate-100 bg-slate-50/70 text-xs font-bold uppercase tracking-wide text-slate-500 dark:border-slate-800 dark:bg-slate-800/50 dark:text-slate-400">
              <tr>
                <th className="px-6 py-3.5">Patient</th>
                <th className="px-6 py-3.5">Details</th>
                <th className="px-6 py-3.5">Contact</th>
                <th className="px-6 py-3.5">Status</th>
                <th className="px-6 py-3.5"><span className="sr-only">Open patient</span></th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100 dark:divide-slate-800">
              {filteredPatients.map((patient) => (
                <tr key={patient.id} className="group transition-colors hover:bg-primary-50/35 dark:hover:bg-primary-950/20">
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-3">
                      <span className="grid h-9 w-9 place-items-center rounded-full bg-primary-100 text-xs font-extrabold text-primary-700 dark:bg-primary-900 dark:text-primary-200">{initials(`${patient.firstName} ${patient.lastName}`)}</span>
                      <div>
                        <p className="font-bold text-slate-800 dark:text-slate-100">{patient.firstName} {patient.lastName}</p>
                        <p className="mt-0.5 text-xs text-slate-500 dark:text-slate-400">Record ID · {patient.id.slice(0, 8).toUpperCase()}</p>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 text-sm text-slate-600 dark:text-slate-300">{patientAge(patient.dateOfBirth)} years · {patient.sex.replaceAll("_", " ")}</td>
                  <td className="px-6 py-4"><p className="text-sm text-slate-700 dark:text-slate-200">{patient.phoneNumber ?? "—"}</p><p className="mt-0.5 text-xs text-slate-500 dark:text-slate-400">{patient.email ?? "No email"}</p></td>
                  <td className="px-6 py-4"><span className="inline-flex rounded-full bg-emerald-50 px-2.5 py-1 text-xs font-bold text-emerald-700 dark:bg-emerald-950/50 dark:text-emerald-300">Active</span></td>
                  <td className="px-6 py-4 text-right"><Link href={`/patients/${patient.id}`} className="inline-flex h-9 w-9 items-center justify-center rounded-lg text-slate-400 hover:bg-white hover:text-primary-600 group-hover:text-primary-600 dark:hover:bg-slate-800" aria-label={`Open ${patient.firstName} ${patient.lastName}`}><ArrowUpRight className="h-4 w-4" /></Link></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </Card>
  );
}
