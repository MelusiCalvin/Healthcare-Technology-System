"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { AlertCircle, ArrowRight, LockKeyhole, Mail, UserRound } from "lucide-react";
import { useRouter, useSearchParams } from "next/navigation";
import { useId } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Button } from "@/components/ui/button";
import { authApi } from "@/features/auth/auth-api";
import { useAuth } from "@/features/auth/auth-context";

const signupSchema = z.object({
  firstName: z.string().trim().min(1, "Enter your first name.").max(120),
  lastName: z.string().trim().min(1, "Enter your last name.").max(120),
  username: z.string().trim().min(3, "Use at least 3 characters.").max(120),
  email: z.string().trim().email("Enter a valid email address."),
  sex: z.string().min(1, "Select an option."),
  password: z.string().min(8, "Use at least 8 characters.").max(72),
  confirmPassword: z.string().min(1, "Confirm your password."),
}).refine((values) => values.password === values.confirmPassword, { path: ["confirmPassword"], message: "Passwords do not match." });

type SignupValues = z.infer<typeof signupSchema>;

export function SignupForm() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const { signIn } = useAuth();
  const formId = useId();
  const { register, handleSubmit, formState: { errors, isSubmitting }, setError } = useForm<SignupValues>({ resolver: zodResolver(signupSchema) });
  const fieldClass = "mt-1.5 h-11 w-full rounded-xl border border-slate-200 bg-white px-3.5 text-sm text-slate-900 outline-none transition focus:border-primary-500 focus:ring-2 focus:ring-primary-500/15";

  const onSubmit = async ({ confirmPassword, ...values }: SignupValues) => {
    void confirmPassword;
    try {
      const session = await authApi.register(values);
      signIn(session);
      const requestedPath = searchParams.get("next");
      const destination = requestedPath?.startsWith("/") && !requestedPath.startsWith("//") ? requestedPath : "/dashboard";
      router.replace(destination);
    } catch (error) {
      setError("root", { message: error instanceof Error ? error.message : "Unable to create your account. Please try again." });
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} noValidate className="space-y-4">
      {errors.root?.message ? <div role="alert" className="flex gap-3 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800"><AlertCircle className="mt-0.5 h-5 w-5 shrink-0" /><p>{errors.root.message}</p></div> : null}
      <div className="grid gap-4 sm:grid-cols-2">
        <label htmlFor={`${formId}-firstName`} className="text-sm font-bold text-slate-700">First name<input id={`${formId}-firstName`} autoComplete="given-name" className={fieldClass} {...register("firstName")} />{errors.firstName?.message ? <span className="mt-1 block text-xs text-red-600">{errors.firstName.message}</span> : null}</label>
        <label htmlFor={`${formId}-lastName`} className="text-sm font-bold text-slate-700">Last name<input id={`${formId}-lastName`} autoComplete="family-name" className={fieldClass} {...register("lastName")} />{errors.lastName?.message ? <span className="mt-1 block text-xs text-red-600">{errors.lastName.message}</span> : null}</label>
      </div>
      <label htmlFor={`${formId}-username`} className="block text-sm font-bold text-slate-700">Username<span className="relative mt-1.5 block"><UserRound className="pointer-events-none absolute left-3.5 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" /><input id={`${formId}-username`} autoComplete="username" className={`${fieldClass} pl-10`} {...register("username")} /></span>{errors.username?.message ? <span className="mt-1 block text-xs text-red-600">{errors.username.message}</span> : null}</label>
      <label htmlFor={`${formId}-email`} className="block text-sm font-bold text-slate-700">Email<span className="relative mt-1.5 block"><Mail className="pointer-events-none absolute left-3.5 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" /><input id={`${formId}-email`} type="email" autoComplete="email" className={`${fieldClass} pl-10`} {...register("email")} /></span>{errors.email?.message ? <span className="mt-1 block text-xs text-red-600">{errors.email.message}</span> : null}</label>
      <label htmlFor={`${formId}-sex`} className="block text-sm font-bold text-slate-700">Sex<select id={`${formId}-sex`} className={fieldClass} {...register("sex")}><option value="">Select an option</option><option value="FEMALE">Female</option><option value="MALE">Male</option><option value="OTHER">Other</option></select>{errors.sex?.message ? <span className="mt-1 block text-xs text-red-600">{errors.sex.message}</span> : null}</label>
      <div className="grid gap-4 sm:grid-cols-2"><label htmlFor={`${formId}-password`} className="text-sm font-bold text-slate-700">Password<span className="relative mt-1.5 block"><LockKeyhole className="pointer-events-none absolute left-3.5 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" /><input id={`${formId}-password`} type="password" autoComplete="new-password" className={`${fieldClass} pl-10`} {...register("password")} /></span>{errors.password?.message ? <span className="mt-1 block text-xs text-red-600">{errors.password.message}</span> : null}</label><label htmlFor={`${formId}-confirmPassword`} className="text-sm font-bold text-slate-700">Confirm password<input id={`${formId}-confirmPassword`} type="password" autoComplete="new-password" className={fieldClass} {...register("confirmPassword")} />{errors.confirmPassword?.message ? <span className="mt-1 block text-xs text-red-600">{errors.confirmPassword.message}</span> : null}</label></div>
      <Button type="submit" className="w-full" size="lg" loading={isSubmitting}>Create account <ArrowRight className="h-4 w-4" /></Button>
    </form>
  );
}