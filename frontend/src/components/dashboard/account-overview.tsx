"use client";

import { useAuth } from "@/features/auth/auth-context";

export function AccountOverview() {
  const { user } = useAuth();
  const fullName = [user?.firstName, user?.lastName].filter(Boolean).join(" ");
  const displayName = fullName || user?.username || "";
  const roles = user?.roles.map((role) => role.replaceAll("_", " ")).join(", ") ?? "";

  return (
    <div className="space-y-8">
      <section>
        <p className="text-sm font-semibold text-primary-600 dark:text-primary-400">Welcome back, {displayName}</p>
        <h1 className="mt-1 text-2xl font-extrabold tracking-tight text-slate-950 dark:text-white sm:text-3xl">Overview</h1>
        <p className="mt-2 text-sm text-slate-500 dark:text-slate-400">Your signed-in account and facility overview.</p>
      </section>

      <section className="max-w-2xl border-t border-slate-200 pt-6 dark:border-slate-800" aria-labelledby="account-heading">
        <h2 id="account-heading" className="text-lg font-bold text-slate-950 dark:text-white">Your account</h2>
        <dl className="mt-4 divide-y divide-slate-200 dark:divide-slate-800">
          <div className="flex flex-wrap justify-between gap-x-6 gap-y-1 py-3">
            <dt className="text-sm text-slate-500 dark:text-slate-400">Name</dt>
            <dd className="text-right text-sm font-semibold text-slate-900 dark:text-slate-100">{displayName}</dd>
          </div>
          <div className="flex flex-wrap justify-between gap-x-6 gap-y-1 py-3">
            <dt className="text-sm text-slate-500 dark:text-slate-400">Username</dt>
            <dd className="text-right text-sm font-semibold text-slate-900 dark:text-slate-100">{user?.username}</dd>
          </div>
          {user?.email ? (
            <div className="flex flex-wrap justify-between gap-x-6 gap-y-1 py-3">
              <dt className="text-sm text-slate-500 dark:text-slate-400">Email</dt>
              <dd className="break-all text-right text-sm font-semibold text-slate-900 dark:text-slate-100">{user.email}</dd>
            </div>
          ) : null}
          <div className="flex flex-wrap justify-between gap-x-6 gap-y-1 py-3">
            <dt className="text-sm text-slate-500 dark:text-slate-400">Roles</dt>
            <dd className="text-right text-sm font-semibold capitalize text-slate-900 dark:text-slate-100">{roles}</dd>
          </div>
        </dl>
      </section>

      <section className="max-w-2xl border-t border-slate-200 pt-6 dark:border-slate-800" aria-labelledby="facility-heading">
        <h2 id="facility-heading" className="text-lg font-bold text-slate-950 dark:text-white">Facility activity</h2>
        <p className="mt-2 text-sm text-slate-500 dark:text-slate-400">Operational dashboard data is not currently available.</p>
      </section>
    </div>
  );
}
