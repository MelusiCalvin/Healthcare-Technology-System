import type { Metadata } from "next";
import { CalendarPlus, Clock3, MapPin } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader } from "@/components/ui/card";

export const metadata: Metadata = { title: "Appointments" };

const schedule = [
  { time: "08:30", duration: "30 min", patient: "Tumi Dlamini", type: "Chronic care review", location: "Consulting Room 2", status: "Checked in" },
  { time: "09:15", duration: "30 min", patient: "Sibusiso Nkosi", type: "Follow-up consultation", location: "Consulting Room 1", status: "In consultation" },
  { time: "10:00", duration: "45 min", patient: "Lerato Molefe", type: "Annual wellness check", location: "Consulting Room 3", status: "Confirmed" },
  { time: "10:45", duration: "30 min", patient: "Amanda Jacobs", type: "Prescription review", location: "Consulting Room 2", status: "Confirmed" },
];

export default function AppointmentsPage() {
  return (
    <div className="space-y-6">
      <section className="flex flex-col justify-between gap-4 sm:flex-row sm:items-end"><div><p className="text-sm font-semibold text-primary-600 dark:text-primary-400">Scheduling</p><h1 className="mt-1 text-2xl font-extrabold tracking-tight text-slate-950 dark:text-white sm:text-3xl">Appointments</h1><p className="mt-2 text-sm text-slate-500 dark:text-slate-400">Thursday, 3 September · Sandton Community Health Centre</p></div><Button><CalendarPlus className="h-4 w-4" /> New appointment</Button></section>
      <Card><CardHeader><div><h2 className="text-base font-bold text-slate-900 dark:text-white">Today&apos;s appointments</h2><p className="mt-1 text-sm text-slate-500 dark:text-slate-400">14 scheduled · 2 completed · 1 awaiting check-in</p></div></CardHeader><CardContent className="pt-4"><div className="space-y-3">{schedule.map((appointment) => <article key={`${appointment.time}-${appointment.patient}`} className="flex flex-col gap-4 rounded-2xl border border-slate-100 p-4 transition hover:border-primary-200 hover:bg-primary-50/30 sm:flex-row sm:items-center dark:border-slate-800 dark:hover:border-primary-900 dark:hover:bg-primary-950/10"><div className="flex min-w-[88px] items-center gap-2 text-sm font-extrabold text-slate-800 dark:text-slate-100"><Clock3 className="h-4 w-4 text-primary-500" /> {appointment.time}</div><div className="min-w-0 flex-1"><p className="font-bold text-slate-800 dark:text-slate-100">{appointment.patient}</p><p className="mt-0.5 text-sm text-slate-500 dark:text-slate-400">{appointment.type} · {appointment.duration}</p></div><p className="flex items-center gap-1.5 text-sm text-slate-500 dark:text-slate-400"><MapPin className="h-4 w-4" /> {appointment.location}</p><Badge variant={appointment.status === "In consultation" ? "warning" : appointment.status === "Checked in" ? "success" : "info"}>{appointment.status}</Badge></article>)}</div></CardContent></Card>
    </div>
  );
}
