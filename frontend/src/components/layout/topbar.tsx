"use client";

import { Bell, Menu, Moon, Search, Sun } from "lucide-react";
import { useTheme } from "@/providers/theme-provider";

interface TopbarProps {
  onOpenNavigation: () => void;
}

export function Topbar({ onOpenNavigation }: TopbarProps) {
  const { theme, toggleTheme } = useTheme();

  return (
    <header className="sticky top-0 z-30 flex h-[72px] items-center gap-3 border-b border-slate-200/80 bg-[#f6f8fb]/90 px-4 backdrop-blur dark:border-slate-800 dark:bg-[#0b1120]/90 sm:px-6 lg:px-8">
      <button
        type="button"
        onClick={onOpenNavigation}
        className="grid h-10 w-10 place-items-center rounded-xl text-slate-600 hover:bg-white dark:text-slate-300 dark:hover:bg-slate-900 lg:hidden"
        aria-label="Open navigation"
      >
        <Menu className="h-5 w-5" />
      </button>

      <label className="relative hidden max-w-xl flex-1 sm:block">
        <Search className="pointer-events-none absolute left-3.5 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" aria-hidden="true" />
        <input
          type="search"
          placeholder="Search patients, appointments or records..."
          className="h-10 w-full rounded-xl border border-slate-200 bg-white pl-10 pr-16 text-sm text-slate-900 outline-none transition placeholder:text-slate-400 focus:border-primary-500 focus:ring-2 focus:ring-primary-500/15 dark:border-slate-800 dark:bg-slate-900 dark:text-slate-100"
        />
        <kbd className="pointer-events-none absolute right-3 top-1/2 hidden -translate-y-1/2 rounded border border-slate-200 bg-slate-50 px-1.5 py-0.5 text-[10px] font-semibold text-slate-400 lg:inline dark:border-slate-700 dark:bg-slate-800">⌘ K</kbd>
      </label>
      <div className="ml-auto flex items-center gap-1">
        <button
          type="button"
          onClick={toggleTheme}
          className="grid h-10 w-10 place-items-center rounded-xl text-slate-500 transition hover:bg-white hover:text-slate-900 dark:text-slate-400 dark:hover:bg-slate-900 dark:hover:text-white"
          aria-label={theme === "light" ? "Switch to dark mode" : "Switch to light mode"}
        >
          {theme === "light" ? <Moon className="h-[18px] w-[18px]" /> : <Sun className="h-[18px] w-[18px]" />}
        </button>
        <button
          type="button"
          className="relative grid h-10 w-10 place-items-center rounded-xl text-slate-500 transition hover:bg-white hover:text-slate-900 dark:text-slate-400 dark:hover:bg-slate-900 dark:hover:text-white"
          aria-label="Notifications"
        >
          <Bell className="h-[18px] w-[18px]" />
          <span className="absolute right-2.5 top-2.5 h-2 w-2 rounded-full bg-red-500 ring-2 ring-[#f6f8fb] dark:ring-[#0b1120]" />
        </button>
      </div>
    </header>
  );
}
