import { ArrowRight, CircleAlert } from "lucide-react";
import Link from "next/link";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader } from "@/components/ui/card";

const queues = [
  { name: "General consultation", count: 18, average: "24 min", status: "Normal", variant: "success" as const },
  { name: "Pharmacy collection", count: 12, average: "31 min", status: "Watch", variant: "warning" as const },
  { name: "Laboratory", count: 7, average: "16 min", status: "Normal", variant: "success" as const },
];

export function QueueOverview() {
  return (
    <Card>
      <CardHeader>
        <div>
          <h2 className="text-base font-bold text-slate-900 dark:text-white">Live queue overview</h2>
          <p className="mt-1 text-sm text-slate-500 dark:text-slate-400">Sandton Community Health Centre</p>
        </div>
        <Link href="/queue" className="inline-flex items-center gap-1 text-sm font-bold text-primary-600 hover:text-primary-700 dark:text-primary-400">
          Manage <ArrowRight className="h-4 w-4" />
        </Link>
      </CardHeader>
      <CardContent className="space-y-3 pt-3">
        {queues.map((queue) => (
          <div key={queue.name} className="flex items-center gap-3 rounded-xl border border-slate-100 p-3.5 dark:border-slate-800">
            <span className="grid h-9 w-9 place-items-center rounded-xl bg-primary-50 text-sm font-extrabold text-primary-700 dark:bg-primary-950/60 dark:text-primary-300">{queue.count}</span>
            <div className="min-w-0 flex-1">
              <p className="truncate text-sm font-bold text-slate-800 dark:text-slate-100">{queue.name}</p>
              <p className="mt-0.5 text-xs text-slate-500 dark:text-slate-400">Average wait: {queue.average}</p>
            </div>
            <Badge variant={queue.variant}>{queue.status}</Badge>
          </div>
        ))}
        <div className="flex gap-2 rounded-xl bg-amber-50 p-3 text-xs leading-5 text-amber-800 dark:bg-amber-950/40 dark:text-amber-200">
          <CircleAlert className="mt-0.5 h-4 w-4 shrink-0" />
          Pharmacy queue is approaching its 35-minute alert threshold.
        </div>
      </CardContent>
    </Card>
  );
}
