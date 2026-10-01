import type { Metadata } from "next";
import { AccountOverview } from "@/components/dashboard/account-overview";

export const metadata: Metadata = { title: "Overview" };

export default function DashboardPage() {
  return <AccountOverview />;
}
