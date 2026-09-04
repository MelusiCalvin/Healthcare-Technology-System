"use client";

import { useAuth } from "@/features/auth/auth-context";
import { usePathname, useRouter } from "next/navigation";
import { useEffect, type ReactNode } from "react";

export function ProtectedPortal({ children }: { children: ReactNode }) {
  const { user, loading } = useAuth();
  const router = useRouter();
  const pathname = usePathname();

  useEffect(() => {
    if (!loading && !user) {
      router.replace(`/login?next=${encodeURIComponent(pathname)}`);
    }
  }, [loading, user, pathname, router]);

  if (loading || !user) {
    return (
      <div className="grid min-h-screen place-items-center bg-[#f6f8fb] dark:bg-[#0b1120]">
        <div className="rounded-xl bg-white px-6 py-4 text-sm font-medium text-slate-600 shadow-sm dark:bg-slate-900 dark:text-slate-300">
          Checking your secure session...
        </div>
      </div>
    );
  }

  return <>{children}</>;
}
