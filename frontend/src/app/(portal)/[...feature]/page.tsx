import { Blocks, ChevronLeft } from "lucide-react";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { EmptyState } from "@/components/ui/empty-state";

export default async function FeaturePlaceholderPage({ params }: { params: Promise<{ feature: string[] }> }) {
  const { feature } = await params;
  const featureName = feature
    .join(" ")
    .split("-")
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join(" ");

  return (
    <div className="mx-auto max-w-3xl py-10"><Card><EmptyState icon={Blocks} title={`${featureName} is being prepared`} description="This workspace has a reserved route and navigation entry. Its secure workflow will be implemented as its backend module becomes available." action={<Link href="/dashboard"><Button variant="outline"><ChevronLeft className="h-4 w-4" /> Back to overview</Button></Link>} /></Card></div>
  );
}
