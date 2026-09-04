import type { ReactNode } from "react";
import { AppShell } from "@/components/layout/app-shell";
import { ProtectedPortal } from "@/features/auth/protected-portal";

export default function PortalLayout({ children }: { children: ReactNode }) {
  return (
    <ProtectedPortal>
      <AppShell>{children}</AppShell>
    </ProtectedPortal>
  );
}
