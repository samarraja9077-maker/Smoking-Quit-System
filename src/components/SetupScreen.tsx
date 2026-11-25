import { useState } from 'react';
import type { AppLanguage, QuitMethod } from '../types';
import { ChoiceRow, Field } from './SharedUi';

interface SetupScreenProps {
  initialLang: AppLanguage;
  onDone: (
    daily: number,
    price: number,
    pack: number,
    method: QuitMethod,
    lang: AppLanguage
  ) => void;
}

const METHODS: QuitMethod[] = ['Both', 'Gradual reduction', 'Quit immediately'];
const LANGUAGES: AppLanguage[] = ['Both', 'English', 'Roman Urdu'];

export function SetupScreen({ initialLang, onDone }: SetupScreenProps) {
  const [daily, setDaily] = useState('10');
  const [price, setPrice] = useState('500');
  const [pack, setPack] = useState('20');
  const [method, setMethod] = useState<QuitMethod>('Both');
  const [lang, setLang] = useState<AppLanguage>(initialLang);

  const handleSubmit = () => {
    const parsedDaily = Number.parseInt(daily, 10);
    const safeDaily = Number.isFinite(parsedDaily) ? Math.min(100, Math.max(1, parsedDaily)) : 10;

    const parsedPrice = Number.parseFloat(price);
    const safePrice = Number.isFinite(parsedPrice) ? Math.max(0, parsedPrice) : 500;

    const parsedPack = Number.parseInt(pack, 10);
    const safePack = Number.isFinite(parsedPack) ? Math.min(100, Math.max(1, parsedPack)) : 20;

    onDone(safeDaily, safePrice, safePack, method, lang);
  };

  return (
    <div className="flex flex-1 flex-col gap-3.5 overflow-y-auto p-[22px]">
      <div className="pt-3">
        <div className="text-[42px] leading-none" role="img" aria-label="No smoking">
          🚭
        </div>
        <div className="mt-2 text-[13px] font-bold tracking-[1px] text-[#35D07F]">
          Smoking Quit System
        </div>
        <div className="text-[12px] font-medium text-[#9FB7A9]">By Jawad</div>
        <h1 className="mt-1 text-[28px] font-bold leading-tight text-[#F2F7F3]">
          Your fresh start begins here
        </h1>
        <p className="mt-1 text-[14px] text-[#9FB7A9]">
          Aapka naya safar yahan se shuru hota hai. No judgement, just progress.
        </p>
      </div>

      <Field
        label="Cigarettes per day / Roz kitni cigarettes?"
        value={daily}
        onChange={setDaily}
      />
      <Field
        label="Price per pack (PKR) / Packet ki qeemat"
        value={price}
        onChange={setPrice}
      />
      <Field
        label="Cigarettes in one pack / Packet mein cigarettes"
        value={pack}
        onChange={setPack}
      />

      <div>
        <div className="mb-1.5 text-[14px] font-semibold text-[#F2F7F3]">
          Choose your quit approach / Quit karne ka tareeqa
        </div>
        {METHODS.map((v) => (
          <ChoiceRow key={v} label={v} selected={method === v} onClick={() => setMethod(v)} />
        ))}
      </div>

      <div>
        <div className="mb-1.5 text-[14px] font-semibold text-[#F2F7F3]">App language</div>
        {LANGUAGES.map((v) => (
          <ChoiceRow key={v} label={v} selected={lang === v} onClick={() => setLang(v)} />
        ))}
      </div>

      <div className="pt-1 pb-4">
        <button
          type="button"
          onClick={handleSubmit}
          className="h-[54px] w-full rounded-[16px] bg-[#35D07F] font-bold text-[#08130F] transition-opacity hover:opacity-95 active:opacity-90"
        >
          Start my smoke-free journey  →
        </button>
        <p className="mt-2 text-center text-[11px] text-[#9FB7A9]">
          You can change these details anytime in Settings.
        </p>
      </div>
    </div>
  );
}
