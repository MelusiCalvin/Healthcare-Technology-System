"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { AlertCircle, ArrowRight, LockKeyhole, UserRound } from "lucide-react";
import { useRouter, useSearchParams } from "next/navigation";
import { useId } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Button } from "@/components/ui/button";
import { authApi } from "@/features/auth/auth-api";
import { useAuth } from "@/features/auth/auth-context";

const loginSchema = z.object({
  username: z.string().trim().min(1, "Enter your username."),
  password: z.string().min(1, "Enter your password."),
});

type LoginValues = z.infer<typeof loginSchema>;

export function LoginForm() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const { signIn } = useAuth();
  const formId = useId();
  const { register, handleSubmit, formState: { errors, isSubmitting }, setError } = useForm<LoginValues>({ resolver: zodResolver(loginSchema) });

  const onSubmit = async (values: LoginValues) => {
    try {
      const user = await authApi.login(values);
      signIn(user);
      const requestedPath = searchParams.get("next");
      const destination = requestedPath?.startsWith("/") && !requestedPath.startsWith("//") ? requestedPath : "/dashboard";
      router.replace(destination);
    } catch (error) {
      setError("root", { message: error instanceof Error ? error.message : "Unable to sign in. Please try again." });
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} noValidate className="space-y-5">
      {errors.root?.message ? <div role="alert" className="flex gap-3 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800"><AlertCircle className="mt-0.5 h-5 w-5 shrink-0" /><p>{errors.root.message}</p></div> : null}
      <label htmlFor={`${formId}-username`} className="block text-sm font-bold text-slate-700">
        Username
        <span className="relative mt-1.5 block"><UserRound className="pointer-events-none absolute left-3.5 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" /><input id={`${formId}-username`} autoComplete="username" className="h-11 w-full rounded-xl border border-slate-200 bg-white pl-10 pr-3.5 text-sm text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-primary-500 focus:ring-2 focus:ring-primary-500/15" placeholder="Enter your username" {...register("username")} /></span>
        {errors.username?.message ? <span className="mt-1.5 block text-xs font-medium text-red-600">{errors.username.message}</span> : null}
      </label>
      <label htmlFor={`${formId}-password`} className="block text-sm font-bold text-slate-700">
        Password
        <span className="relative mt-1.5 block"><LockKeyhole className="pointer-events-none absolute left-3.5 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" /><input id={`${formId}-password`} type="password" autoComplete="current-password" className="h-11 w-full rounded-xl border border-slate-200 bg-white pl-10 pr-3.5 text-sm text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-primary-500 focus:ring-2 focus:ring-primary-500/15" placeholder="Enter your password" {...register("password")} /></span>
        {errors.password?.message ? <span className="mt-1.5 block text-xs font-medium text-red-600">{errors.password.message}</span> : null}
      </label>
      <div className="flex items-center justify-between gap-3"><label className="inline-flex items-center gap-2 text-sm text-slate-600"><input type="checkbox" className="h-4 w-4 rounded border-slate-300 text-primary-600 focus:ring-primary-500" /> Remember this device</label><button type="button" className="text-sm font-bold text-primary-600 hover:text-primary-700">Need help?</button></div>
      <Button type="submit" className="w-full" size="lg" loading={isSubmitting}>Sign in securely <ArrowRight className="h-4 w-4" /></Button>
    </form>
  );
}
