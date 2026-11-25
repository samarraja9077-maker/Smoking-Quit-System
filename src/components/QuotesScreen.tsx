import { tr } from '../quitStore';
import type { AppLanguage } from '../types';

const ENGLISH_QUOTES = [
  'You are stronger than a temporary craving.',
  "Don't count the days. Make the days count.",
  'Every cigarette you skip is a choice for your future.',
  'A slip is not the end. Learn, reset, continue.',
  "You deserve a life that isn't controlled by nicotine.",
  'Small wins become a powerful new habit.',
];

const ROMAN_QUOTES = [
  'Aap aik aarzi craving se zyada mazboot hain.',
  'Sirf din na ginein, har din ko qeemti banayein.',
  'Har chori hui cigarette aapke mustaqbil ke haq mein faisla hai.',
  'Aik slip ka matlab haar nahi. Seekhein, dobara shuru karein.',
  'Aap aisi zindagi deserve karte hain jo nicotine ke qaboo mein na ho.',
  'Choti jeetain mil kar bari kamyabi banti hain.',
];

export function QuotesScreen({ lang }: { lang: AppLanguage }) {
  return (
    <div className="flex flex-col gap-3 p-[18px]">
      <h2 className="text-[22px] font-bold text-[#F2F7F3]">
        {tr(lang, 'A little motivation', 'Thora sa hosla')}
      </h2>

      {ENGLISH_QUOTES.map((eng, idx) => (
        <div
          key={eng}
          className="flex items-start rounded-[18px] bg-[#12231B] p-[18px]"
        >
          <span className="text-[34px] font-bold leading-none text-[#35D07F]">“</span>
          <p className="ml-2 pt-[3px] text-[16px] font-medium leading-snug text-[#F2F7F3]">
            {tr(lang, eng, ROMAN_QUOTES[idx])}
          </p>
        </div>
      ))}
    </div>
  );
}
