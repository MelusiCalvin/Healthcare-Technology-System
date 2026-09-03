import type { Metadata } from "next";
import { CheckCircle2, ShieldCheck } from "lucide-react";
import { Brand } from "@/components/layout/brand";
import { LoginForm } from "@/features/auth/login-form";

export const metadata: Metadata = { title: "Sign in" };

export default function LoginPage() {
  return (
    <main className="grid min-h-screen bg-white lg:grid-cols-2">
      <section className="hidden bg-primary-700 p-10 text-white lg:flex lg:flex-col lg:justify-between xl:p-14">
        <div><Brand /><div className="mt-24 max-w-lg"><p className="text-sm font-bold uppercase tracking-[0.16em] text-primary-200">Ubuntu Health</p><h1 className="mt-4 text-5xl font-extrabold tracking-tight">Better-connected care for every South African.</h1><p className="mt-6 max-w-md text-lg leading-8 text-primary-100">One secure workspace for clinicians, care teams and the people they serve.</p></div></div>
        <div className="space-y-3 text-sm text-primary-100"><p className="flex items-center gap-2"><CheckCircle2 className="h-5 w-5 text-primary-200" /> Role-aware access for every care setting</p><p className="flex items-center gap-2"><CheckCircle2 className="h-5 w-5 text-primary-200" /> Designed for public and private healthcare</p><p className="flex items-center gap-2"><CheckCircle2 className="h-5 w-5 text-primary-200" /> Privacy and audit controls built in</p></div>
      </section>
      <section className="flex items-center justify-center px-5 py-10 sm:px-8"><div className="w-full max-w-md"><div className="mb-10 lg:hidden"><Brand /></div><div className="mb-8"><span className="grid h-11 w-11 place-items-center rounded-xl bg-primary-50 text-primary-600"><ShieldCheck className="h-6 w-6" /></span><h2 className="mt-5 text-3xl font-extrabold tracking-tight text-slate-950">Welcome back</h2><p className="mt-2 text-sm leading-6 text-slate-500">Sign in with your Ubuntu Health account to continue to your authorised workspace.</p></div><LoginForm /><p className="mt-8 text-center text-xs leading-5 text-slate-400">By continuing, you confirm that you are authorised to access this healthcare system. All activity is logged and monitored.</p></div></section>
    </main>
  );
}
