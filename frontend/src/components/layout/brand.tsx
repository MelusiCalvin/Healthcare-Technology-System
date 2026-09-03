import { Cross } from "lucide-react";
import Link from "next/link";

export function Brand({ compact = false }: { compact?: boolean }) {
  return (
    <Link href="/dashboard" className="group flex items-center gap-3 rounded-xl focus-visible:ring-2 focus-visible:ring-primary-500">
      <span className="grid h-10 w-10 shrink-0 place-items-center rounded-xl bg-primary-600 text-white shadow-sm shadow-primary-600/30 transition-transform group-hover:scale-105">
        <Cross className="h-5 w-5" strokeWidth={2.8} aria-hidden="true" />
      </span>
      {!compact ? (
        <span className="min-w-0">
          <span className="block text-base font-extrabold tracking-tight text-slate-950 dark:text-white">Ubuntu Health</span>
          <span className="block truncate text-[11px] font-medium text-slate-500 dark:text-slate-400">Connected care, together</span>
        </span>
      ) : null}
    </Link>
  );
}
