"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { AlertCircle, CheckCircle2, ChevronLeft, Save } from "lucide-react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useId, useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { useCreatePatient } from "@/features/patients/patient-hooks";
import { cn } from "@/lib/utils";

const registrationSchema = z.object({
  firstName: z.string().trim().min(1, "First name is required.").max(100, "First name must be 100 characters or fewer."),
  lastName: z.string().trim().min(1, "Last name is required.").max(100, "Last name must be 100 characters or fewer."),
  dateOfBirth: z.string().min(1, "Date of birth is required.").refine((value) => new Date(value) < new Date(), "Date of birth must be in the past."),
  sex: z.enum(["FEMALE", "MALE", "INTERSEX", "UNSPECIFIED"], { message: "Select the recorded sex." }),
  phoneNumber: z.string().trim().max(30, "Phone number must be 30 characters or fewer.").optional(),
  email: z.string().trim().email("Enter a valid email address.").max(254, "Email must be 254 characters or fewer.").or(z.literal("")),
});

type RegistrationValues = z.infer<typeof registrationSchema>;

const fieldClassName = "mt-1.5 h-11 w-full rounded-xl border bg-white px-3.5 text-sm text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-primary-500 focus:ring-2 focus:ring-primary-500/15 dark:bg-slate-950 dark:text-white";

function FieldError({ message }: { message?: string }) {
  return message ? <p className="mt-1.5 text-xs font-medium text-red-600 dark:text-red-400">{message}</p> : null;
}

export function PatientRegistrationForm() {
  const router = useRouter();
  const formId = useId();
  const [success, setSuccess] = useState(false);
  const createPatient = useCreatePatient();
  const { register, handleSubmit, formState: { errors } } = useForm<RegistrationValues>({
    resolver: zodResolver(registrationSchema),
    defaultValues: { sex: "UNSPECIFIED", phoneNumber: "", email: "" },
  });

  const onSubmit = async (values: RegistrationValues) => {
    setSuccess(false);
    const patient = await createPatient.mutateAsync({
      ...values,
      phoneNumber: values.phoneNumber || undefined,
      email: values.email || undefined,
    });
    setSuccess(true);
    window.setTimeout(() => router.push(`/patients/${patient.id}`), 800);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} noValidate className="space-y-6">
      {createPatient.isError ? (
        <div role="alert" className="flex gap-3 rounded-xl border border-red-200 bg-red-50 px-4 py-3.5 text-sm text-red-800 dark:border-red-900 dark:bg-red-950/50 dark:text-red-200">
          <AlertCircle className="mt-0.5 h-5 w-5 shrink-0" />
          <div><p className="font-bold">We couldn&apos;t register this patient.</p><p className="mt-1">{createPatient.error.message}</p></div>
        </div>
      ) : null}
      {success ? (
        <div role="status" className="flex gap-3 rounded-xl border border-emerald-200 bg-emerald-50 px-4 py-3.5 text-sm text-emerald-800 dark:border-emerald-900 dark:bg-emerald-950/50 dark:text-emerald-200">
          <CheckCircle2 className="mt-0.5 h-5 w-5 shrink-0" />
          <div><p className="font-bold">Patient registered successfully.</p><p className="mt-1">Opening the patient record…</p></div>
        </div>
      ) : null}

      <Card>
        <CardHeader>
          <div>
            <h2 className="text-base font-bold text-slate-900 dark:text-white">Personal details</h2>
            <p className="mt-1 text-sm text-slate-500 dark:text-slate-400">Enter the patient&apos;s legal details as verified at reception.</p>
          </div>
          <span className="text-xs font-semibold text-slate-400">Required fields marked *</span>
        </CardHeader>
        <CardContent className="grid gap-5 pt-4 md:grid-cols-2">
          <label htmlFor={`${formId}-firstName`} className="block text-sm font-bold text-slate-700 dark:text-slate-200">
            First name <span className="text-red-600">*</span>
            <input id={`${formId}-firstName`} autoComplete="given-name" className={cn(fieldClassName, errors.firstName ? "border-red-500" : "border-slate-200 dark:border-slate-700")} {...register("firstName")} />
            <FieldError message={errors.firstName?.message} />
          </label>
          <label htmlFor={`${formId}-lastName`} className="block text-sm font-bold text-slate-700 dark:text-slate-200">
            Last name <span className="text-red-600">*</span>
            <input id={`${formId}-lastName`} autoComplete="family-name" className={cn(fieldClassName, errors.lastName ? "border-red-500" : "border-slate-200 dark:border-slate-700")} {...register("lastName")} />
            <FieldError message={errors.lastName?.message} />
          </label>
          <label htmlFor={`${formId}-dateOfBirth`} className="block text-sm font-bold text-slate-700 dark:text-slate-200">
            Date of birth <span className="text-red-600">*</span>
            <input id={`${formId}-dateOfBirth`} type="date" autoComplete="bday" className={cn(fieldClassName, errors.dateOfBirth ? "border-red-500" : "border-slate-200 dark:border-slate-700")} {...register("dateOfBirth")} />
            <FieldError message={errors.dateOfBirth?.message} />
          </label>
          <label htmlFor={`${formId}-sex`} className="block text-sm font-bold text-slate-700 dark:text-slate-200">
            Recorded sex <span className="text-red-600">*</span>
            <select id={`${formId}-sex`} className={cn(fieldClassName, errors.sex ? "border-red-500" : "border-slate-200 dark:border-slate-700")} {...register("sex")}>
              <option value="UNSPECIFIED">Unspecified</option>
              <option value="FEMALE">Female</option>
              <option value="MALE">Male</option>
              <option value="INTERSEX">Intersex</option>
            </select>
            <FieldError message={errors.sex?.message} />
          </label>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <div>
            <h2 className="text-base font-bold text-slate-900 dark:text-white">Contact details</h2>
            <p className="mt-1 text-sm text-slate-500 dark:text-slate-400">Use a contact method approved by the patient.</p>
          </div>
        </CardHeader>
        <CardContent className="grid gap-5 pt-4 md:grid-cols-2">
          <label htmlFor={`${formId}-phoneNumber`} className="block text-sm font-bold text-slate-700 dark:text-slate-200">
            Mobile number
            <input id={`${formId}-phoneNumber`} type="tel" autoComplete="tel" placeholder="e.g. 082 123 4567" className={cn(fieldClassName, errors.phoneNumber ? "border-red-500" : "border-slate-200 dark:border-slate-700")} {...register("phoneNumber")} />
            <FieldError message={errors.phoneNumber?.message} />
          </label>
          <label htmlFor={`${formId}-email`} className="block text-sm font-bold text-slate-700 dark:text-slate-200">
            Email address
            <input id={`${formId}-email`} type="email" autoComplete="email" placeholder="patient@example.com" className={cn(fieldClassName, errors.email ? "border-red-500" : "border-slate-200 dark:border-slate-700")} {...register("email")} />
            <FieldError message={errors.email?.message} />
          </label>
        </CardContent>
      </Card>

      <div className="flex flex-col-reverse justify-between gap-3 border-t border-slate-200 pt-5 sm:flex-row sm:items-center dark:border-slate-800">
        <Link href="/patients" className="inline-flex h-10 items-center justify-center gap-2 rounded-xl px-4 text-sm font-semibold text-slate-600 hover:bg-white hover:text-slate-950 dark:text-slate-300 dark:hover:bg-slate-900 dark:hover:text-white"><ChevronLeft className="h-4 w-4" /> Back to patients</Link>
        <Button type="submit" loading={createPatient.isPending}><Save className="h-4 w-4" /> Register patient</Button>
      </div>
    </form>
  );
}
