import { ArrowRight, Clock3 } from "lucide-react";
import Link from "next/link";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { initials } from "@/lib/utils";

const appointments = [
  { time: "08:30", patient: "Tumi Dlamini", reason: "Diabetes review", status: "Checked in", color: "bg-violet-100 text-violet-700" },
  { time: "09:15", patient: "Sibusiso Nkosi", reason: "Follow-up consultation", status: "In consultation", color: "bg-amber-100 text-amber-800" },
  { time: "10:00", patient: "Lerato Molefe", reason: "Annual wellness check", status: "Confirmed", color: "bg-sky-100 text-sky-700" },
  { time: "10:45", patient: "Amanda Jacobs", reason: "Prescription review", status: "Confirmed", color: "bg-rose-100 text-rose-700" },
];

export function TodaysSchedule() {
  return (
    <Card className="h-full">
      <CardHeader>
        <div>
          <h2 className="text-base font-bold text-slate-900 dark:text-white">Today&apos;s schedule</h2>
          <p className="mt-1 text-sm text-slate-500 dark:text-slate-400">Thursday, 3 September</p>
        </div>
        <Link href="/appointments" className="inline-flex items-center gap-1 text-sm font-bold text-primary-600 hover:text-primary-700 dark:text-primary-400">
          View all <ArrowRight className="h-4 w-4" />
        </Link>
      </CardHeader>
      <CardContent className="pt-2">
        <div className="divide-y divide-slate-100 dark:divide-slate-800">
          {appointments.map((appointment) => (
            <div key={`${appointment.time}-${appointment.patient}`} className="flex gap-3 py-3.5 first:pt-2">
              <div className="w-10 pt-1 text-xs font-bold text-slate-500 dark:text-slate-400">{appointment.time}</div>
              <span className={`grid h-9 w-9 shrink-0 place-items-center rounded-full text-xs font-bold ${appointment.color}`}>{initials(appointment.patient)}</span>
              <div className="min-w-0 flex-1">
                <p className="truncate text-sm font-bold text-slate-800 dark:text-slate-100">{appointment.patient}</p>
                <p className="mt-0.5 truncate text-xs text-slate-500 dark:text-slate-400">{appointment.reason}</p>
              </div>
              <Badge variant={appointment.status === "In consultation" ? "warning" : appointment.status === "Checked in" ? "success" : "info"} className="hidden self-center sm:inline-flex">
                {appointment.status}
              </Badge>
            </div>
          ))}
        </div>
        <div className="mt-3 flex items-center gap-2 rounded-xl bg-slate-50 px-3 py-2.5 text-xs font-medium text-slate-500 dark:bg-slate-800/60 dark:text-slate-400">
          <Clock3 className="h-4 w-4 text-primary-500" />
          12 appointments still scheduled today
        </div>
      </CardContent>
    </Card>
  );
}
