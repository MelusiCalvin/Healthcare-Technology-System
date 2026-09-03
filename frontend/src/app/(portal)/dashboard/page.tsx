import type { Metadata } from "next";
import { CalendarClock, ClipboardCheck, HeartPulse, UsersRound } from "lucide-react";
import { QueueOverview } from "@/components/dashboard/queue-overview";
import { StatCard } from "@/components/dashboard/stat-card";
import { TodaysSchedule } from "@/components/dashboard/todays-schedule";
import { WeeklyActivity } from "@/components/dashboard/weekly-activity";
import { Button } from "@/components/ui/button";
import Link from "next/link";

export const metadata: Metadata = { title: "Overview" };

export default function DashboardPage() {
  return (
    <div className="space-y-6 lg:space-y-8">
      <section className="flex flex-col justify-between gap-5 sm:flex-row sm:items-end">
        <div>
          <p className="text-sm font-semibold text-primary-600 dark:text-primary-400">Good morning, Nomsa</p>
          <h1 className="mt-1 text-2xl font-extrabold tracking-tight text-slate-950 dark:text-white sm:text-3xl">Care at a glance</h1>
          <p className="mt-2 text-sm text-slate-500 dark:text-slate-400">Here&apos;s what&apos;s happening across your facility today.</p>
        </div>
        <Link href="/patients/new">
          <Button>Register patient</Button>
        </Link>
      </section>

      <section className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        <StatCard label="Patients checked in" value="86" change="12.4%" trend="up" icon={UsersRound} iconClassName="bg-primary-50 text-primary-600 dark:bg-primary-950/60 dark:text-primary-400" />
        <StatCard label="Appointments today" value="124" change="8 remaining" trend="neutral" icon={CalendarClock} iconClassName="bg-violet-50 text-violet-600 dark:bg-violet-950/60 dark:text-violet-400" />
        <StatCard label="Average waiting time" value="22 min" change="4 min lower" trend="up" icon={HeartPulse} iconClassName="bg-emerald-50 text-emerald-600 dark:bg-emerald-950/60 dark:text-emerald-400" />
        <StatCard label="Consultations completed" value="68" change="5.8%" trend="up" icon={ClipboardCheck} iconClassName="bg-amber-50 text-amber-600 dark:bg-amber-950/60 dark:text-amber-400" />
      </section>

      <section className="grid gap-6 xl:grid-cols-[minmax(0,1.15fr)_minmax(420px,0.85fr)]">
        <WeeklyActivity />
        <QueueOverview />
      </section>

      <section className="grid gap-6 xl:grid-cols-[minmax(0,1.15fr)_minmax(420px,0.85fr)]">
        <TodaysSchedule />
        <section className="rounded-2xl bg-primary-700 p-6 text-white shadow-lg shadow-primary-900/10">
          <p className="text-sm font-semibold text-primary-100">Ubuntu Health Insight</p>
          <h2 className="mt-3 text-xl font-extrabold tracking-tight">Plan ahead for a smoother afternoon.</h2>
          <p className="mt-3 max-w-md text-sm leading-6 text-primary-100">Appointment demand is expected to increase between 13:00 and 15:00. Consider opening an additional triage desk before lunch.</p>
          <div className="mt-7 flex items-center gap-3">
            <Link href="/analytics"><Button variant="secondary" className="bg-white text-primary-800 hover:bg-primary-50">View demand trends</Button></Link>
            <span className="text-xs text-primary-200">Advisory only · Verify locally</span>
          </div>
        </section>
      </section>
    </div>
  );
}
