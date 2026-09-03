import type { LucideIcon } from "lucide-react";
import { ArrowDownRight, ArrowUpRight } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { cn } from "@/lib/utils";

interface StatCardProps {
  label: string;
  value: string;
  change: string;
  trend: "up" | "down" | "neutral";
  icon: LucideIcon;
  iconClassName: string;
}

export function StatCard({ label, value, change, trend, icon: Icon, iconClassName }: StatCardProps) {
  const positive = trend === "up";
  const neutral = trend === "neutral";

  return (
    <Card>
      <CardContent className="p-5 sm:p-6">
        <div className="flex items-start justify-between gap-3">
          <span className={cn("grid h-10 w-10 place-items-center rounded-xl", iconClassName)}>
            <Icon className="h-5 w-5" aria-hidden="true" />
          </span>
          <span
            className={cn(
              "inline-flex items-center gap-0.5 text-xs font-bold",
              neutral ? "text-slate-500" : positive ? "text-emerald-600 dark:text-emerald-400" : "text-red-600 dark:text-red-400",
            )}
          >
            {!neutral ? positive ? <ArrowUpRight className="h-3.5 w-3.5" /> : <ArrowDownRight className="h-3.5 w-3.5" /> : null}
            {change}
          </span>
        </div>
        <p className="mt-5 text-2xl font-extrabold tracking-tight text-slate-950 dark:text-white">{value}</p>
        <p className="mt-1 text-sm font-medium text-slate-500 dark:text-slate-400">{label}</p>
      </CardContent>
    </Card>
  );
}
