import type { ReactNode } from 'react';
import { tr } from '../quitStore';
import type { AppLanguage } from '../types';

export function Field({
  label,
  value,
  onChange,
}: {
  label: string;
  value: string;
  onChange: (val: string) => void;
}) {
  return (
    <div className="flex flex-col gap-1.5">
      <label className="text-[13px] font-medium text-[#F2F7F3]">{label}</label>
      <input
        type="text"
        inputMode="decimal"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="w-full rounded-[13px] border border-[#345143] bg-transparent px-3.5 py-3 text-[15px] text-[#F2F7F3] outline-none transition-colors focus:border-[#35D07F]"
      />
    </div>
  );
}

export function ChoiceRow({
  label,
  selected,
  onClick,
}: {
  label: string;
  selected: boolean;
  onClick: () => void;
}) {
  return (
    <button
      type="button"
      onClick={onClick}
      className={`my-[3px] flex w-full items-center rounded-[12px] p-[13px] text-left transition-colors ${
        selected ? 'bg-[#1B3B2A]' : 'bg-[#12231B] hover:bg-[#162C21]'
      }`}
    >
      <span
        className={`flex h-5 w-5 shrink-0 items-center justify-center rounded-full border-2 ${
          selected ? 'border-[#35D07F]' : 'border-[#9FB7A9]'
        }`}
      >
        {selected && <span className="h-2.5 w-2.5 rounded-full bg-[#35D07F]" />}
      </span>
      <span className="ml-3 text-[15px] text-[#F2F7F3]">{label}</span>
    </button>
  );
}

export function StatCard({
  value,
  label,
  icon,
  className = '',
}: {
  value: string;
  label: string;
  icon: ReactNode;
  className?: string;
}) {
  return (
    <div className={`rounded-[18px] bg-[#12231B] p-[15px] ${className}`}>
      <div className="text-[#35D07F]">{icon}</div>
      <div className="mt-2 text-[22px] font-bold text-[#F2F7F3]">{value}</div>
      <div className="text-[11px] text-[#9FB7A9]">{label}</div>
    </div>
  );
}

export function MilestoneCard({ lang }: { lang: AppLanguage }) {
  return (
    <div className="rounded-[18px] bg-[#12231B] p-4">
      <div className="text-[14px] font-bold text-[#35D07F]">
        {tr(lang, 'A helpful health fact', 'Sehat ki aik maloomat')}
      </div>
      <p className="mt-[7px] text-[13px] leading-relaxed text-[#F2F7F3]">
        {tr(
          lang,
          'Within about 20 minutes after your last cigarette, heart rate and blood pressure can begin moving toward normal. Recovery timelines vary from person to person.',
          'Aakhri cigarette ke taqreeban 20 minute baad dil ki dharkan aur blood pressure normal ki taraf aana shuru ho sakte hain. Har shakhs ki recovery mukhtalif hoti hai.'
        )}
      </p>
    </div>
  );
}
