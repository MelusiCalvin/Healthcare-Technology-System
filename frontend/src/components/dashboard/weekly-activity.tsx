import { MoreHorizontal } from "lucide-react";
import { Card, CardContent, CardHeader } from "@/components/ui/card";

const activity = [48, 66, 57, 84, 73, 44, 29];
const days = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];

export function WeeklyActivity() {
  return (
    <Card className="h-full">
      <CardHeader>
        <div>
          <h2 className="text-base font-bold text-slate-900 dark:text-white">Patient activity</h2>
          <p className="mt-1 text-sm text-slate-500 dark:text-slate-400">Appointments completed this week</p>
        </div>
        <button className="grid h-8 w-8 place-items-center rounded-lg text-slate-400 hover:bg-slate-100 hover:text-slate-700 dark:hover:bg-slate-800" aria-label="More activity options">
          <MoreHorizontal className="h-5 w-5" />
        </button>
      </CardHeader>
      <CardContent className="pt-5">
        <div className="flex h-52 items-end justify-between gap-3 sm:gap-5">
          {activity.map((value, index) => (
            <div key={days[index]} className="flex h-full flex-1 flex-col items-center justify-end gap-2">
              <div className="group relative flex w-full flex-1 items-end">
                <div
                  className="w-full rounded-t-lg bg-primary-100 transition hover:bg-primary-500 dark:bg-primary-900/70 dark:hover:bg-primary-500"
                  style={{ height: `${value}%` }}
                  aria-label={`${days[index]}: ${value} completed appointments`}
                >
                  {index === 3 ? <span className="mx-auto mt-2 block h-1.5 w-1.5 rounded-full bg-primary-600 dark:bg-primary-200" /> : null}
                </div>
              </div>
              <span className="text-[11px] font-semibold text-slate-400">{days[index]}</span>
            </div>
          ))}
        </div>
      </CardContent>
    </Card>
  );
}
