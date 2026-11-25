import type { ActiveMode, AppLanguage, QuitPrefs } from '../types';
import { ChoiceRow } from './SharedUi';

const LANGUAGES: AppLanguage[] = ['Both', 'English', 'Roman Urdu'];
const MODES: ActiveMode[] = ['Quit immediately', 'Gradual reduction'];

export function SettingsScreen({
  prefs,
  onUpdatePrefs,
  onResetSetup,
}: {
  prefs: QuitPrefs;
  onUpdatePrefs: (next: QuitPrefs) => void;
  onResetSetup: () => void;
}) {
  const handleRemindersToggle = async (checked: boolean) => {
    if (checked && typeof window !== 'undefined' && 'Notification' in window) {
      if (Notification.permission === 'default') {
        try {
          await Notification.requestPermission();
        } catch {
          // Ignore permission request failure
        }
      }
    }
    onUpdatePrefs({ ...prefs, reminders: checked });
  };

  return (
    <div className="flex flex-col gap-3 p-[18px]">
      <h2 className="text-[22px] font-bold text-[#F2F7F3]">Preferences</h2>

      <div>
        <div className="mb-1 text-[14px] text-[#9FB7A9]">Language / Zubaan</div>
        {LANGUAGES.map((l) => (
          <ChoiceRow
            key={l}
            label={l}
            selected={prefs.language === l}
            onClick={() => onUpdatePrefs({ ...prefs, language: l })}
          />
        ))}
      </div>

      <div className="flex items-center justify-between rounded-[16px] bg-[#12231B] p-3.5">
        <div className="pr-3">
          <div className="text-[14px] font-bold text-[#F2F7F3]">Daily reminders</div>
          <div className="text-[11px] text-[#9FB7A9]">
            5 reminders: 8 AM, 11 AM, 2 PM, 5 PM, 8 PM
          </div>
        </div>
        <button
          type="button"
          role="switch"
          aria-checked={prefs.reminders}
          onClick={() => handleRemindersToggle(!prefs.reminders)}
          className={`relative inline-flex h-7 w-12 shrink-0 items-center rounded-full transition-colors ${
            prefs.reminders ? 'bg-[#1B3B2A]' : 'bg-[#2A3B32]'
          }`}
        >
          <span
            className={`inline-block h-5 w-5 transform rounded-full transition-transform ${
              prefs.reminders
                ? 'translate-x-6 bg-[#35D07F]'
                : 'translate-x-1 bg-[#9FB7A9]'
            }`}
          />
        </button>
      </div>

      <div className="pt-1 text-[14px] font-bold text-[#35D07F]">
        Your quit plan / Aapka quit plan
      </div>

      <div>
        <div className="mb-1 text-[14px] font-semibold text-[#F2F7F3]">
          Choose your current approach
        </div>
        {MODES.map((mode) => (
          <ChoiceRow
            key={mode}
            label={mode}
            selected={prefs.activeMode === mode}
            onClick={() => onUpdatePrefs({ ...prefs, activeMode: mode })}
          />
        ))}
      </div>

      <div className="text-[12px] text-[#9FB7A9]">Available options: {prefs.method}</div>
      <div className="text-[14px] text-[#F2F7F3]">
        Daily cigarettes before quitting: {prefs.cigsDay}
      </div>
      <div className="text-[14px] text-[#F2F7F3]">
        Pack price: PKR {prefs.packPrice}
      </div>

      <p className="text-[12px] leading-relaxed text-[#9FB7A9]">
        Data is stored locally in this browser. Estimated savings and avoided cigarettes are based on the details you entered.
      </p>

      <button
        type="button"
        onClick={onResetSetup}
        className="w-full rounded-full border border-[#345143] py-3 text-[14px] font-semibold text-[#35D07F] transition-colors hover:bg-[#12231B]"
      >
        Reset app setup
      </button>
    </div>
  );
}
