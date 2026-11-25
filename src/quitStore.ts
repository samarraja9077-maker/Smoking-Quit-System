import type { AppLanguage, QuitPrefs, TabKey } from './types';

const STORAGE_KEY = 'quit_system_prefs';

export const DEFAULT_PREFS: QuitPrefs = {
  configured: false,
  quitStart: Date.now(),
  cigsDay: 10,
  packPrice: 500,
  packSize: 20,
  method: 'Both',
  activeMode: 'Quit immediately',
  language: 'Both',
  reminders: true,
};

function sanitizePrefs(raw: string | null | undefined): QuitPrefs | null {
  if (!raw) return null;
  try {
    const parsed = JSON.parse(raw) as Partial<QuitPrefs>;
    return {
      configured: Boolean(parsed.configured),
      quitStart: typeof parsed.quitStart === 'number' ? parsed.quitStart : Date.now(),
      cigsDay:
        typeof parsed.cigsDay === 'number'
          ? Math.min(100, Math.max(1, Math.trunc(parsed.cigsDay)))
          : 10,
      packPrice: typeof parsed.packPrice === 'number' ? Math.max(0, parsed.packPrice) : 500,
      packSize:
        typeof parsed.packSize === 'number'
          ? Math.min(100, Math.max(1, Math.trunc(parsed.packSize)))
          : 20,
      method:
        parsed.method === 'Gradual reduction' ||
        parsed.method === 'Quit immediately' ||
        parsed.method === 'Both'
          ? parsed.method
          : 'Both',
      activeMode:
        parsed.activeMode === 'Gradual reduction' ? 'Gradual reduction' : 'Quit immediately',
      language:
        parsed.language === 'English' ||
        parsed.language === 'Roman Urdu' ||
        parsed.language === 'Both'
          ? parsed.language
          : 'Both',
      reminders: parsed.reminders !== undefined ? Boolean(parsed.reminders) : true,
    };
  } catch {
    return null;
  }
}

export function loadPrefs(): QuitPrefs {
  try {
    const parsed = sanitizePrefs(localStorage.getItem(STORAGE_KEY));
    if (parsed) return parsed;
  } catch {
    // Ignore localStorage read errors
  }
  return { ...DEFAULT_PREFS, quitStart: Date.now() };
}

export function savePrefs(prefs: QuitPrefs): void {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(prefs));
  } catch {
    // Ignore localStorage quota errors
  }
}

export function tr(lang: AppLanguage, english: string, roman: string): string {
  if (lang === 'English') return english;
  if (lang === 'Roman Urdu') return roman;
  return `${english} / ${roman}`;
}

export function pageTitle(page: TabKey, lang: AppLanguage): string {
  const romanMap: Record<TabKey, string> = {
    Home: 'Aapki progress',
    Progress: 'Taraqqi',
    SOS: 'Craving SOS',
    Quotes: 'Honsla afzai',
    Settings: 'Settings',
  };
  return tr(lang, page, romanMap[page]);
}

export function elapsedMs(prefs: QuitPrefs, now: number): number {
  return Math.max(0, now - prefs.quitStart);
}

export function getDays(prefs: QuitPrefs, now: number): number {
  return Math.floor(elapsedMs(prefs, now) / 86_400_000);
}

export function getCigsSaved(prefs: QuitPrefs, now: number): number {
  const totalDays = Math.floor(elapsedMs(prefs, now) / 86_400_000);
  const base = prefs.cigsDay;
  if (prefs.activeMode !== 'Gradual reduction') {
    return totalDays * base;
  }
  let sum = 0;
  for (let day = 0; day < totalDays; day++) {
    const reduction = Math.min(1.0, Math.max(0.0, Math.floor(day / 7) * 0.2));
    sum += Math.trunc(base * reduction);
  }
  return sum;
}

export function getMoneySaved(prefs: QuitPrefs, now: number): number {
  const pack = Math.max(1, prefs.packSize);
  return Math.trunc(getCigsSaved(prefs, now) * (prefs.packPrice / pack));
}

export function formatDuration(prefs: QuitPrefs, now: number): string {
  const ms = elapsedMs(prefs, now);
  const d = Math.floor(ms / 86_400_000);
  const h = Math.floor(ms / 3_600_000) % 24;
  const m = Math.floor(ms / 60_000) % 60;
  return `${d}d  ${h}h  ${m}m`;
}

const REMINDER_HOURS = [8, 11, 14, 17, 20];
const ROMAN_MESSAGES = [
  'Aaj ka din smoke-free rakhein. Aap kar sakte hain! 💚',
  'Craving temporary hai. Gehri saans lein aur 5 minute rukain.',
  'Har cigarette se door rehna aapki jeet hai.',
  'Apni wajah yaad karein. Aapki sehat qeemti hai.',
  'Aaj ki progress par fakhar karein. Kal se behtar aaj banayein!',
];
const ENGLISH_MESSAGES = [
  "Keep today smoke-free. You've got this! 💚",
  'A craving is temporary. Breathe deeply and wait five minutes.',
  'Every cigarette avoided is a win.',
  'Remember your reason. Your health matters.',
  "Be proud of today's progress. Keep going!",
];

let activeTimers: number[] = [];

export function clearBrowserReminders(): void {
  activeTimers.forEach((id) => window.clearTimeout(id));
  activeTimers = [];
}

export function scheduleBrowserReminders(enabled: boolean, lang: AppLanguage): void {
  clearBrowserReminders();
  if (!enabled || typeof window === 'undefined' || !('Notification' in window)) return;
  if (Notification.permission !== 'granted') return;

  const messages = lang !== 'English' ? ROMAN_MESSAGES : ENGLISH_MESSAGES;
  const now = new Date();

  REMINDER_HOURS.forEach((hour, idx) => {
    const target = new Date(now);
    target.setHours(hour, 0, 0, 0);
    if (target.getTime() <= now.getTime()) {
      target.setDate(target.getDate() + 1);
    }
    const delay = target.getTime() - now.getTime();
    const id = window.setTimeout(() => {
      if (Notification.permission === 'granted') {
        new Notification('Smoking Quit System', {
          body: messages[idx],
        });
      }
      scheduleBrowserReminders(true, lang);
    }, delay);
    activeTimers.push(id);
  });
}
