import { Ban, PiggyBank } from 'lucide-react';
import { formatDuration, getCigsSaved, getDays, getMoneySaved, tr } from '../quitStore';
import type { AppLanguage, QuitPrefs } from '../types';
import { MilestoneCard, StatCard } from './SharedUi';

export function HomeScreen({
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
  const money = getMoneySaved(prefs, now);
  const title = tr(lang, 'Your smoke-free journey', 'Aapka smoke-free safar');

  const mode = prefs.activeMode;
  const base = prefs.cigsDay;
  const week = Math.floor(day / 7);
  const target = mode === 'Gradual reduction' ? Math.max(0, Math.trunc(base * (1.0 - week * 0.2))) : 0;

  return (
    <div className="flex flex-col gap-3.5 px-[18px] pb-5">
      <div className="flex w-full flex-col items-center rounded-[24px] bg-[#12231B] p-5 text-center">
        <div className="text-[13px] text-[#9FB7A9]">{title}</div>
        <div className="text-[64px] font-extrabold leading-tight text-[#35D07F]">{day}</div>
        <div className="text-[14px] font-bold tracking-[1.2px] text-[#F2F7F3]">
          {tr(lang, 'DAYS ON QUIT JOURNEY', 'QUIT JOURNEY KE DIN')}
        </div>
        <div className="mt-2 text-[13px] text-[#9FB7A9]">{formatDuration(prefs, now)}</div>
        <div className="mt-3 text-[13px] text-[#35D07F]">
          {tr(
            lang,
            'One moment at a time. Keep going.',
            'Bas aik lamha aik waqt mein. Aagay barhtay rahain.'
          )}
        </div>
      </div>

      <div className="grid grid-cols-2 gap-[10px]">
        <StatCard
          value={`${cigs}`}
          label={tr(lang, 'Cigarettes avoided', 'Cigarettes se bachay')}
          icon={<Ban className="h-5 w-5" />}
        />
        <StatCard
          value={`Rs ${money}`}
          label={tr(lang, 'Estimated saved', 'Andazan bachay')}
          icon={<PiggyBank className="h-5 w-5" />}
        />
      </div>

      <div className="rounded-[20px] bg-[#173626] p-[17px]">
        <div className="text-[11px] font-bold text-[#35D07F]">
          {tr(lang, "TODAY'S REMINDER", 'AAJ KI YAAD DEHANI')}
        </div>
        <p className="mt-2 text-[16px] font-medium leading-snug text-[#F2F7F3]">
          {tr(
            lang,
            "A craving is a wave. You don't have to act on it. Pause, breathe, and let it pass.",
            'Craving aik lehar ki tarah hai. Is par amal zaroori nahi. Rukain, saans lein aur guzarne dein.'
          )}
        </p>
      </div>

      <h2 className="text-[18px] font-bold text-[#F2F7F3]">
        {tr(lang, 'Your next small win', 'Aapki agli choti jeet')}
      </h2>

      <div className="rounded-[18px] bg-[#12231B] p-4">
        <div className="text-[11px] font-bold text-[#35D07F]">
          {tr(lang, 'YOUR CURRENT APPROACH', 'AAPKA MAUJOODA TAREEQA')}
        </div>
        <div className="mt-1.5 text-[17px] font-bold text-[#F2F7F3]">
          {mode === 'Gradual reduction'
            ? tr(lang, 'Gradual reduction', 'Aahista aahista kami')
            : tr(lang, 'Quit immediately', 'Foran quit')}
        </div>
        <p className="mt-1.5 text-[12px] leading-relaxed text-[#9FB7A9]">
          {mode === 'Gradual reduction'
            ? tr(
                lang,
                `Suggested target: about ${target} cigarettes/day (from ${base}). Adjust with a healthcare professional if needed.`,
                `Tajweez shuda target: taqreeban ${target} cigarettes rozana (pehle ${base}). Zaroorat par healthcare professional se mashwara karein.`
              )
            : tr(
                lang,
                'Focus on staying smoke-free, one craving at a time.',
                'Har craving ka aik aik kar ke muqabla karein aur smoking se door rahain.'
              )}
        </p>
      </div>

      <MilestoneCard lang={lang} />
    </div>
  );
}
