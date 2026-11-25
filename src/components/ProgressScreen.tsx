import { Ban, CheckCircle2, PiggyBank, Timer } from 'lucide-react';
import { formatDuration, getCigsSaved, getDays, getMoneySaved, tr } from '../quitStore';
import type { AppLanguage, QuitPrefs } from '../types';
import { StatCard } from './SharedUi';

const MILESTONES: Array<[string, string]> = [
  ['20 minutes', 'Heart rate and blood pressure may begin to improve.'],
  ['12 hours', 'Carbon monoxide level in blood can return toward normal.'],
  ['2–12 weeks', 'Circulation and lung function may improve.'],
  ['1–9 months', 'Coughing and shortness of breath may decrease.'],
];

export function ProgressScreen({
  prefs,
  lang,
  now,
}: {
  prefs: QuitPrefs;
  lang: AppLanguage;
  now: number;
}) {
  const day = getDays(prefs, now);
  const cigs = getCigsSaved(prefs, now);
  const saved = getMoneySaved(prefs, now);

  return (
    <div className="flex flex-col gap-[13px] p-[18px]">
      <h2 className="text-[23px] font-bold text-[#F2F7F3]">
        {tr(lang, 'Your progress', 'Aapki taraqqi')}
      </h2>

      <StatCard
        value={formatDuration(prefs, now)}
        label={tr(lang, 'Time since your quit start', 'Quit shuru kiye hue')}
        icon={<Timer className="h-5 w-5" />}
        className="w-full"
      />
      <StatCard
        value={`${cigs}`}
        label={tr(lang, 'Estimated cigarettes avoided', 'Andazan cigarettes se bachay')}
        icon={<Ban className="h-5 w-5" />}
        className="w-full"
      />
      <StatCard
        value={`PKR ${saved}`}
        label={tr(lang, 'Estimated money saved', 'Andazan paisay bachay')}
        icon={<PiggyBank className="h-5 w-5" />}
        className="w-full"
      />

      <h3 className="pt-1 text-[18px] font-bold text-[#F2F7F3]">
        {tr(lang, 'Recovery milestones', 'Sehat ke marahil')}
      </h3>

      {MILESTONES.map(([title, desc]) => (
        <div
          key={title}
          className="flex items-center rounded-[14px] bg-[#12231B] p-[15px]"
        >
          <CheckCircle2
            className={`h-6 w-6 shrink-0 ${day > 0 ? 'text-[#35D07F]' : 'text-[#9FB7A9]'}`}
          />
          <div className="ml-3">
            <div className="text-[14px] font-bold text-[#F2F7F3]">{title}</div>
            <div className="text-[12px] text-[#9FB7A9]">{desc}</div>
          </div>
        </div>
      ))}

      <p className="text-[11px] text-[#9FB7A9]">
        {tr(
          lang,
          'Milestones are general educational estimates, not a personal medical prediction.',
          'Yeh aam maloomati andazay hain, zaati medical prediction nahi.'
        )}
      </p>
    </div>
  );
}
