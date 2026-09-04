"use client";

import { useAuth } from "@/features/auth/auth-context";
import { usePathname, useRouter } from "next/navigation";
import { useEffect, type ReactNode } from "react";

const ADMIN = ["SYSTEM_ADMIN", "HOSPITAL_ADMIN"];
const ROUTE_ROLES: Record<string, string[]> = {
  "/patients": ["SYSTEM_ADMIN", "HOSPITAL_ADMIN", "DOCTOR", "NURSE", "RECEPTIONIST"],
  "/clinical": ["SYSTEM_ADMIN", "HOSPITAL_ADMIN", "DOCTOR", "NURSE"],
  "/queue": ["SYSTEM_ADMIN", "HOSPITAL_ADMIN", "NURSE", "RECEPTIONIST"],
  "/pharmacy": ["SYSTEM_ADMIN", "HOSPITAL_ADMIN", "DOCTOR", "PHARMACIST"],
  "/laboratory": ["SYSTEM_ADMIN", "HOSPITAL_ADMIN", "DOCTOR", "NURSE", "LAB_TECHNICIAN"],
  "/inventory": ["SYSTEM_ADMIN", "HOSPITAL_ADMIN", "PHARMACIST"],
  "/analytics": ADMIN,
  "/facilities": ADMIN,
  "/settings": ADMIN,
};

function requiredRolesForPath(pathname: string) {
  const entry = Object.entries(ROUTE_ROLES).find(([prefix]) => pathname === prefix || pathname.startsWith(`${prefix}/`));
  return entry?.[1] ?? null;
}

export function ProtectedPortal({ children }: { children: ReactNode }) {
  const { user, loading, hasRole } = useAuth();
  const router = useRouter();
  const pathname = usePathname();

  useEffect(() => {
    if (loading) return;

    if (!user) {
      router.replace(`/login?next=${encodeURIComponent(pathname)}`);
      return;
    }

    const requiredRoles = requiredRolesForPath(pathname);
    if (requiredRoles && !hasRole(...requiredRoles)) {
      router.replace("/dashboard");
    }
  }, [loading, user, pathname, router, hasRole]);

  if (loading || !user) {
    return (
      <div className="grid min-h-screen place-items-center bg-[#f6f8fb] dark:bg-[#0b1120]">
        <div className="rounded-xl bg-white px-6 py-4 text-sm font-medium text-slate-600 shadow-sm dark:bg-slate-900 dark:text-slate-300">
          Checking your secure session...
        </div>
      </div>
    );
  }

  const requiredRoles = requiredRolesForPath(pathname);
  if (requiredRoles && !hasRole(...requiredRoles)) return null;

  return <>{children}</>;
}
