import { useEffect, useState } from 'react';
import { tr } from '../quitStore';
import type { AppLanguage } from '../types';

export function SosScreen({ lang }: { lang: AppLanguage }) {
  const [seconds, setSeconds] = useState(120);
  const [running, setRunning] = useState(false);

  useEffect(() => {
    if (!running || seconds <= 0) return;
    const id = window.setTimeout(() => {
      setSeconds((prev) => {
        const next = prev - 1;
        if (next <= 0) {
          setRunning(false);
          return 0;
        }
        return next;
      });
    }, 1000);
    return () => window.clearTimeout(id);
  }, [running, seconds]);

  const mins = Math.floor(seconds / 60);
  const secs = (seconds % 60).toString().padStart(2, '0');

  const tips = [
    tr(lang, 'Take 5 slow breaths.', '5 aahista saans lein.'),
    tr(lang, 'Drink a glass of water.', 'Aik glass pani piyen.'),
    tr(
      lang,
      'Move away from your smoking trigger.',
      'Smoking wali jagah ya trigger se door ho jayein.'
    ),
    tr(
      lang,
      'Message someone who supports you.',
      'Kisi supportive dost ya ghar walay ko message karein.'
    ),
  ];

  const handleToggle = () => {
    if (seconds === 0) {
      setSeconds(120);
      setRunning(true);
    } else {
      setRunning((prev) => !prev);
    }
  };

  return (
    <div className="flex flex-col items-center gap-4 p-[22px]">
      <div className="text-[48px] leading-none" role="img" aria-label="Lungs">
        🫁
      </div>
      <h2 className="text-[25px] font-bold text-[#F2F7F3]">
        {tr(lang, 'Craving SOS', 'Craving ka muqabla')}
      </h2>
      <p className="text-center text-[14px] text-[#9FB7A9]">
        {tr(
          lang,
          'The urge will pass. Give yourself two minutes.',
          'Yeh talab guzar jayegi. Khud ko do minute dein.'
        )}
      </p>

      <div className="flex h-[210px] w-[210px] flex-col items-center justify-center rounded-full bg-[#12231B]">
        <div className="text-[48px] font-bold leading-none text-[#35D07F]">
          {mins}:{secs}
        </div>
        <div className="mt-1 text-[12px] text-[#9FB7A9]">
          {running ? 'BREATHE' : 'PAUSE'}
        </div>
      </div>

      <button
        type="button"
        onClick={handleToggle}
        className="w-full rounded-full bg-[#35D07F] py-3 text-[14px] font-semibold text-[#08130F] transition-opacity hover:opacity-95"
      >
        {running ? 'Pause timer' : seconds === 0 ? 'Start again' : 'Start 2-minute reset'}
      </button>

      <button
        type="button"
        onClick={() => {
          setSeconds(120);
          setRunning(false);
        }}
        className="w-full rounded-full border border-[#345143] py-3 text-[14px] font-semibold text-[#35D07F] transition-colors hover:bg-[#12231B]"
      >
        Reset
      </button>

      <div className="w-full rounded-[18px] bg-[#12231B] p-4">
        <div className="text-[14px] font-bold text-[#35D07F]">
          {tr(lang, 'Try this now', 'Abhi yeh karein')}
        </div>
        <ul className="mt-1 space-y-2 text-[13px] text-[#F2F7F3]">
          {tips.map((tip) => (
            <li key={tip} className="pt-1">
              • {tip}
            </li>
          ))}
        </ul>
      </div>

      <p className="text-center text-[11px] text-[#9FB7A9]">
        {tr(
          lang,
          'If withdrawal feels overwhelming, a healthcare professional can help you choose evidence-based support.',
          'Agar withdrawal mushkil ho, healthcare professional se evidence-based madad lein.'
        )}
      </p>
    </div>
  );
}
