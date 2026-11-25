export type AppLanguage = 'Both' | 'English' | 'Roman Urdu';
export type QuitMethod = 'Both' | 'Gradual reduction' | 'Quit immediately';
export type ActiveMode = 'Quit immediately' | 'Gradual reduction';
export type TabKey = 'Home' | 'Progress' | 'SOS' | 'Quotes' | 'Settings';

export interface QuitPrefs {
  configured: boolean;
  quitStart: number;
  cigsDay: number;
  packPrice: number;
  packSize: number;
  method: QuitMethod;
  activeMode: ActiveMode;
  language: AppLanguage;
  reminders: boolean;
}
