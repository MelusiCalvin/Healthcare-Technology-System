import type { Metadata } from "next";
import { Suspense } from "react";
import { UserPlus } from "lucide-react";
import { Brand } from "@/components/layout/brand";
import { SignupForm } from "@/features/auth/signup-form";

export const metadata: Metadata = { title: "Create account" };

export default function SignupPage() {
  return (
    <main className="grid min-h-screen bg-white lg:grid-cols-2">
      <section className="hidden bg-primary-700 p-10 text-white lg:flex lg:flex-col lg:justify-between xl:p-14">
        <Brand />
        <div className="max-w-lg pb-16">
          <p className="text-sm font-bold uppercase tracking-[0.16em] text-primary-200">Ubuntu Health</p>
          <h1 className="mt-4 text-5xl font-extrabold tracking-tight">Your care journey starts here.</h1>
          <p className="mt-6 max-w-md text-lg leading-8 text-primary-100">Create a secure account to access your authorised healthcare workspace.</p>
        </div>
      </section>
      <section className="flex items-center justify-center px-5 py-10 sm:px-8">
        <div className="w-full max-w-md">
          <div className="mb-8 lg:hidden"><Brand /></div>
          <div className="mb-8">
            <span className="grid h-11 w-11 place-items-center rounded-xl bg-primary-50 text-primary-600"><UserPlus className="h-6 w-6" /></span>
            <h2 className="mt-5 text-3xl font-extrabold tracking-tight text-slate-950">Create your account</h2>
            <p className="mt-2 text-sm leading-6 text-slate-500">Register securely to continue to Ubuntu Health.</p>
          </div>
          <Suspense fallback={<div>Loading registration...</div>}><SignupForm /></Suspense>
          <p className="mt-8 text-center text-sm text-slate-500">Already have an account? <a href="/login" className="font-bold text-primary-600 hover:text-primary-700">Sign in</a></p>
        </div>
      </section>
    </main>
  );
}