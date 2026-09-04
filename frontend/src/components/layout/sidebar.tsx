"use client";

import {
  Activity,
  BarChart3,
  Bell,
  CalendarDays,
  ClipboardList,
  FileText,
  Hospital,
  LayoutDashboard,
  LogOut,
  Package,
  Pill,
  Settings,
  Stethoscope,
  UsersRound,
  X,
  type LucideIcon,
} from "lucide-react";
import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { cn } from "@/lib/utils";
import { Brand } from "@/components/layout/brand";
import { useAuth } from "@/features/auth/auth-context";

interface NavigationItem {
  href: string;
  label: string;
  icon: LucideIcon;
  roles: string[];
}

const ALL_STAFF = ["SYSTEM_ADMIN", "HOSPITAL_ADMIN", "DOCTOR", "NURSE", "PHARMACIST", "LAB_TECHNICIAN", "RECEPTIONIST"];
const ADMIN = ["SYSTEM_ADMIN", "HOSPITAL_ADMIN"];

const primaryNavigation: NavigationItem[] = [
  { href: "/dashboard", label: "Overview", icon: LayoutDashboard, roles: [...ALL_STAFF, "PATIENT"] },
  { href: "/patients", label: "Patients", icon: UsersRound, roles: ["SYSTEM_ADMIN", "HOSPITAL_ADMIN", "DOCTOR", "NURSE", "RECEPTIONIST"] },
  { href: "/appointments", label: "Appointments", icon: CalendarDays, roles: [...ALL_STAFF, "PATIENT"] },
  { href: "/clinical", label: "Clinical workspace", icon: Stethoscope, roles: ["SYSTEM_ADMIN", "HOSPITAL_ADMIN", "DOCTOR", "NURSE"] },
  { href: "/queue", label: "Queue management", icon: Activity, roles: ["SYSTEM_ADMIN", "HOSPITAL_ADMIN", "NURSE", "RECEPTIONIST"] },
];

const operationsNavigation: NavigationItem[] = [
  { href: "/pharmacy", label: "Pharmacy", icon: Pill, roles: ["SYSTEM_ADMIN", "HOSPITAL_ADMIN", "DOCTOR", "PHARMACIST"] },
  { href: "/laboratory", label: "Laboratory", icon: ClipboardList, roles: ["SYSTEM_ADMIN", "HOSPITAL_ADMIN", "DOCTOR", "NURSE", "LAB_TECHNICIAN"] },
  { href: "/documents", label: "Documents", icon: FileText, roles: [...ALL_STAFF] },
  { href: "/inventory", label: "Inventory", icon: Package, roles: ["SYSTEM_ADMIN", "HOSPITAL_ADMIN", "PHARMACIST"] },
];

const administrationNavigation: NavigationItem[] = [
  { href: "/analytics", label: "Analytics", icon: BarChart3, roles: ADMIN },
  { href: "/notifications", label: "Notifications", icon: Bell, roles: [...ALL_STAFF, "PATIENT"] },
  { href: "/facilities", label: "Facilities", icon: Hospital, roles: ADMIN },
  { href: "/settings", label: "Settings", icon: Settings, roles: ADMIN },
];

interface SidebarProps {
  mobileOpen: boolean;
  onClose: () => void;
}

function NavigationGroup({ label, items }: { label?: string; items: NavigationItem[] }) {
  const pathname = usePathname();
  const { hasRole } = useAuth();
  const visibleItems = items.filter((item) => hasRole(...item.roles));

  if (visibleItems.length === 0) return null;

  return (
    <div className="space-y-1">
      {label ? <p className="px-3 pb-2 pt-5 text-[11px] font-bold uppercase tracking-[0.12em] text-slate-400">{label}</p> : null}
      {visibleItems.map((item) => {
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
  const router = useRouter();
  const { user, signOut } = useAuth();

  const logout = () => {
    signOut();
    router.replace("/login");
  };

  const roleLabel = user?.roles[0]?.replaceAll("_", " ") ?? "User";
  const initials = user?.username?.slice(0, 2).toUpperCase() ?? "UH";

  return (
    <>
      <div className={cn("fixed inset-0 z-40 bg-slate-950/40 backdrop-blur-[1px] transition-opacity lg:hidden", mobileOpen ? "opacity-100" : "pointer-events-none opacity-0")} onClick={onClose} aria-hidden="true" />
      <aside className={cn("fixed inset-y-0 left-0 z-50 flex w-[280px] flex-col border-r border-slate-200 bg-white px-4 py-5 transition-transform duration-200 dark:border-slate-800 dark:bg-slate-950 lg:translate-x-0", mobileOpen ? "translate-x-0" : "-translate-x-full")} aria-label="Primary navigation">
        <div className="flex items-center justify-between px-1">
          <Brand />
          <button type="button" onClick={onClose} className="grid h-10 w-10 place-items-center rounded-lg text-slate-500 hover:bg-slate-100 dark:hover:bg-slate-800 lg:hidden" aria-label="Close navigation"><X className="h-5 w-5" /></button>
        </div>

        <nav className="mt-7 min-h-0 flex-1 overflow-y-auto pr-1">
          <NavigationGroup items={primaryNavigation} />
          <NavigationGroup label="Operations" items={operationsNavigation} />
          <NavigationGroup label="Administration" items={administrationNavigation} />
        </nav>

        <div className="mt-4 border-t border-slate-100 pt-4 dark:border-slate-800">
          <div className="flex items-center gap-3 rounded-xl px-2 py-2">
            <span className="grid h-9 w-9 shrink-0 place-items-center rounded-full bg-primary-100 text-xs font-bold text-primary-700 dark:bg-primary-900 dark:text-primary-200">{initials}</span>
            <span className="min-w-0 flex-1">
              <span className="block truncate text-sm font-semibold text-slate-800 dark:text-slate-100">{user?.username ?? "User"}</span>
              <span className="block truncate text-xs capitalize text-slate-500 dark:text-slate-400">{roleLabel.toLowerCase()}</span>
            </span>
          </div>
          <button type="button" onClick={logout} className="mt-1 flex w-full items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium text-slate-600 transition-colors hover:bg-red-50 hover:text-red-700 dark:text-slate-400 dark:hover:bg-red-950/30 dark:hover:text-red-300" aria-label="Log out">
            <LogOut className="h-[18px] w-[18px]" />
            Log out
          </button>
        </div>
      </aside>
    </>
  );
}
