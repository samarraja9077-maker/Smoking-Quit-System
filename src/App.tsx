import { useEffect, useState, type ReactNode } from 'react';
import { Heart, Home, Leaf, Quote, Settings, TrendingUp } from 'lucide-react';
import {
  clearBrowserReminders,
  loadPrefs,
  pageTitle,
  savePrefs,
  scheduleBrowserReminders,
  tr,
} from './quitStore';
import type { AppLanguage, QuitMethod, QuitPrefs, TabKey } from './types';
import { SetupScreen } from './components/SetupScreen';
import { HomeScreen } from './components/HomeScreen';
import { ProgressScreen } from './components/ProgressScreen';
import { SosScreen } from './components/SosScreen';
import { QuotesScreen } from './components/QuotesScreen';
import { SettingsScreen } from './components/SettingsScreen';

const NAV_ITEMS: Array<{ key: TabKey; label: string; icon: ReactNode }> = [
  { key: 'Home', label: 'Home', icon: <Home className="h-5 w-5" /> },
  { key: 'Progress', label: 'Progress', icon: <TrendingUp className="h-5 w-5" /> },
  { key: 'SOS', label: 'SOS', icon: <Heart className="h-5 w-5" /> },
  { key: 'Quotes', label: 'Quotes', icon: <Quote className="h-5 w-5" /> },
  { key: 'Settings', label: 'Settings', icon: <Settings className="h-5 w-5" /> },
];

export function App() {
  const [prefs, setPrefs] = useState<QuitPrefs>(() => loadPrefs());
  const [page, setPage] = useState<TabKey>('Home');
  const [now, setNow] = useState<number>(() => Date.now());

  useEffect(() => {
    let intervalId: number | undefined;
    const msUntilNextMinute = 60_000 - (Date.now() % 60_000) + 50;
    const timeoutId = window.setTimeout(() => {
      setNow(Date.now());
      intervalId = window.setInterval(() => setNow(Date.now()), 60_000);
    }, msUntilNextMinute);
    return () => {
      window.clearTimeout(timeoutId);
      if (intervalId !== undefined) window.clearInterval(intervalId);
    };
  }, []);

  useEffect(() => {
    if (prefs.configured && prefs.reminders) {
      scheduleBrowserReminders(true, prefs.language);
    } else {
      clearBrowserReminders();
    }
    return () => clearBrowserReminders();
  }, [prefs.configured, prefs.reminders, prefs.language]);

  const updatePrefs = (next: QuitPrefs) => {
    setPrefs(next);
    savePrefs(next);
  };

  const handleSetupDone = async (
    daily: number,
    price: number,
    pack: number,
    method: QuitMethod,
    lang: AppLanguage
  ) => {
    if (typeof window !== 'undefined' && 'Notification' in window && Notification.permission === 'default') {
      try {
        await Notification.requestPermission();
      } catch {
        // Ignore notification permission prompt rejection
      }
    }

    const startTime = Date.now();
    setNow(startTime);
    updatePrefs({
      configured: true,
      quitStart: startTime,
      cigsDay: daily,
      packPrice: price,
      packSize: pack,
      method,
      activeMode: method === 'Gradual reduction' ? 'Gradual reduction' : 'Quit immediately',
      language: lang,
      reminders: true,
    });
    setPage('Home');
  };

  return (
    <div className="min-h-screen w-full bg-[#08130F] text-[#F2F7F3]">
      <div className="mx-auto flex min-h-screen w-full max-w-md flex-col bg-[#08130F]">
        <div className="flex items-center justify-between border-b border-[#1B3B2A] bg-[#12231B] px-4 py-2 text-xs">
          <span className="text-[#9FB7A9]">Native Android APK ready</span>
          <a
            href="/download/apk"
            download="SmokingQuitSystem-debug.apk"
            className="rounded-lg bg-[#35D07F] px-3 py-1 font-bold text-[#08130F]"
          >
            Download APK
          </a>
        </div>
        {!prefs.configured ? (
          <SetupScreen initialLang={prefs.language} onDone={handleSetupDone} />
        ) : (
          <>
            <header className="flex items-center justify-between px-5 py-[18px]">
              <div>
                <div className="text-[12px] font-bold tracking-[1.4px] text-[#35D07F]">
                  SMOKING QUIT SYSTEM
                </div>
                <h1 className="text-[25px] font-bold leading-tight text-[#F2F7F3]">
                  {pageTitle(page, prefs.language)}
                </h1>
                <div className="text-[11px] font-medium text-[#9FB7A9]">By Jawad</div>
              </div>
              <div className="flex h-[42px] w-[42px] items-center justify-center rounded-full bg-[#12231B] text-[#35D07F]">
                <Leaf className="h-5 w-5" />
              </div>
            </header>

            <main className="flex-1 overflow-y-auto">
              {page === 'Home' && <HomeScreen prefs={prefs} lang={prefs.language} now={now} />}
              {page === 'Progress' && <ProgressScreen prefs={prefs} lang={prefs.language} now={now} />}
              {page === 'SOS' && <SosScreen lang={prefs.language} />}
              {page === 'Quotes' && <QuotesScreen lang={prefs.language} />}
              {page === 'Settings' && (
                <SettingsScreen
                  prefs={prefs}
                  onUpdatePrefs={updatePrefs}
                  onResetSetup={() => updatePrefs({ ...prefs, configured: false })}
                />
              )}
            </main>

            <nav className="sticky bottom-0 grid grid-cols-5 bg-[#12231B] px-1 py-2">
              {NAV_ITEMS.map(({ key, label, icon }) => {
                const selected = page === key;
                return (
                  <button
                    key={key}
                    type="button"
                    onClick={() => setPage(key)}
                    className="flex flex-col items-center gap-1 py-1 text-center"
                  >
                    <span
                      className={`flex h-8 w-14 items-center justify-center rounded-full transition-colors ${
                        selected ? 'bg-[#244B36] text-[#35D07F]' : 'text-[#9FB7A9]'
                      }`}
                    >
                      {icon}
                    </span>
                    <span
                      className={`text-[10px] leading-tight ${
                        selected ? 'font-semibold text-[#35D07F]' : 'text-[#9FB7A9]'
                      }`}
                    >
                      {tr(prefs.language, label, label)}
                    </span>
                  </button>
                );
              })}
            </nav>
          </>
        )}
      </div>
    </div>
  );
}

export default App;
