"use client";

import {
  Activity,
  BarChart3,
  Bell,
  CalendarDays,
  ChevronDown,
  ClipboardList,
  FileText,
  Hospital,
  LayoutDashboard,
  Package,
  Pill,
  Settings,
  Stethoscope,
  UsersRound,
  X,
  type LucideIcon,
} from "lucide-react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { cn } from "@/lib/utils";
import { Brand } from "@/components/layout/brand";

interface NavigationItem {
  href: string;
  label: string;
  icon: LucideIcon;
}

const primaryNavigation: NavigationItem[] = [
  { href: "/dashboard", label: "Overview", icon: LayoutDashboard },
  { href: "/patients", label: "Patients", icon: UsersRound },
  { href: "/appointments", label: "Appointments", icon: CalendarDays },
  { href: "/clinical", label: "Clinical workspace", icon: Stethoscope },
  { href: "/queue", label: "Queue management", icon: Activity },
];

const operationsNavigation: NavigationItem[] = [
  { href: "/pharmacy", label: "Pharmacy", icon: Pill },
  { href: "/laboratory", label: "Laboratory", icon: ClipboardList },
  { href: "/documents", label: "Documents", icon: FileText },
  { href: "/inventory", label: "Inventory", icon: Package },
];

const administrationNavigation: NavigationItem[] = [
  { href: "/analytics", label: "Analytics", icon: BarChart3 },
  { href: "/notifications", label: "Notifications", icon: Bell },
  { href: "/facilities", label: "Facilities", icon: Hospital },
  { href: "/settings", label: "Settings", icon: Settings },
];

interface SidebarProps {
  mobileOpen: boolean;
  onClose: () => void;
}

function NavigationGroup({ label, items }: { label?: string; items: NavigationItem[] }) {
  const pathname = usePathname();

  return (
    <div className="space-y-1">
      {label ? <p className="px-3 pb-2 pt-5 text-[11px] font-bold uppercase tracking-[0.12em] text-slate-400">{label}</p> : null}
      {items.map((item) => {
        const active = pathname === item.href || (item.href !== "/dashboard" && pathname.startsWith(`${item.href}/`));
        const Icon = item.icon;
        return (
          <Link
            key={item.href}
            href={item.href}
            className={cn(
              "group flex h-10 items-center gap-3 rounded-xl px-3 text-sm font-medium transition-colors",
              active
                ? "bg-primary-50 text-primary-700 dark:bg-primary-950/60 dark:text-primary-300"
                : "text-slate-600 hover:bg-slate-100 hover:text-slate-950 dark:text-slate-400 dark:hover:bg-slate-800 dark:hover:text-slate-100",
            )}
          >
            <Icon className={cn("h-[18px] w-[18px]", active ? "text-primary-600 dark:text-primary-400" : "text-slate-400 group-hover:text-current")} aria-hidden="true" />
            {item.label}
          </Link>
        );
      })}
    </div>
  );
}

export function Sidebar({ mobileOpen, onClose }: SidebarProps) {
  return (
    <>
      <div
        className={cn(
          "fixed inset-0 z-40 bg-slate-950/40 backdrop-blur-[1px] transition-opacity lg:hidden",
          mobileOpen ? "opacity-100" : "pointer-events-none opacity-0",
        )}
        onClick={onClose}
        aria-hidden="true"
      />
      <aside
        className={cn(
          "fixed inset-y-0 left-0 z-50 flex w-[280px] flex-col border-r border-slate-200 bg-white px-4 py-5 transition-transform duration-200 dark:border-slate-800 dark:bg-slate-950 lg:translate-x-0",
          mobileOpen ? "translate-x-0" : "-translate-x-full",
        )}
        aria-label="Primary navigation"
      >
        <div className="flex items-center justify-between px-1">
          <Brand />
          <button
            type="button"
            onClick={onClose}
            className="grid h-10 w-10 place-items-center rounded-lg text-slate-500 hover:bg-slate-100 dark:hover:bg-slate-800 lg:hidden"
            aria-label="Close navigation"
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        <nav className="mt-7 min-h-0 flex-1 overflow-y-auto pr-1">
          <NavigationGroup items={primaryNavigation} />
          <NavigationGroup label="Operations" items={operationsNavigation} />
          <NavigationGroup label="Administration" items={administrationNavigation} />
        </nav>

        <div className="mt-4 border-t border-slate-100 pt-4 dark:border-slate-800">
          <button
            type="button"
            className="flex w-full items-center gap-3 rounded-xl px-2 py-2 text-left hover:bg-slate-100 dark:hover:bg-slate-800"
            aria-label="Open account menu"
          >
            <span className="grid h-9 w-9 place-items-center rounded-full bg-primary-100 text-xs font-bold text-primary-700 dark:bg-primary-900 dark:text-primary-200">NM</span>
            <span className="min-w-0 flex-1">
              <span className="block truncate text-sm font-semibold text-slate-800 dark:text-slate-100">Nomsa Mokoena</span>
              <span className="block truncate text-xs text-slate-500 dark:text-slate-400">Reception · Sandton</span>
            </span>
            <ChevronDown className="h-4 w-4 text-slate-400" aria-hidden="true" />
          </button>
        </div>
      </aside>
    </>
  );
}
