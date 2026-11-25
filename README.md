# Smoking Quit System — By Jawad

Native Android application built with **Kotlin**, **Jetpack Compose**, **Material 3**, and **Room Database**, with automated Debug & Release APK builds via GitHub Actions. (The original Vite + React web reference in `src/` is preserved in the repository.)

## Native Android Features
- **First-run setup (`SetupScreen`)**: Configure cigarettes per day, pack price (PKR), pack size, quit approach (`Both`, `Gradual reduction`, or `Quit immediately`), and language (`Both`, `English`, or `Roman Urdu`).
- **Room Database Persistence (`QuitDatabase`)**: Stores your quit profile (`quit_profile`) and completed Craving SOS logs (`craving_logs`) locally on the device.
- **Dashboard (`HomeScreen`)**: Live quit-journey days and `d h m` duration timer, estimated cigarettes avoided, estimated money saved (`Rs`), daily reminder card, active quit approach & weekly gradual-reduction target, and health fact card.
- **Progress (`ProgressScreen`)**: Cumulative time smoke-free, cigarettes avoided, PKR saved, SOS cravings overcome, and educational health recovery milestones.
- **Craving SOS (`SosScreen`)**: Interactive 2-minute countdown timer (`BREATHE` / `PAUSE`), reset controls, practical coping tips, and automatic logging of completed SOS resets.
- **Motivation (`QuotesScreen`)**: Bilingual motivational quotes in English and Roman Urdu.
- **Preferences (`SettingsScreen`) & Daily Notifications (`ReminderScheduler`)**: Switch language or active approach, toggle 5 daily local reminders (8 AM, 11 AM, 2 PM, 5 PM, 8 PM) backed by `AlarmManager` and `BootReceiver`, or reset app setup.

## Build & Download APK

### Option 1: Download from GitHub Actions
1. Open the repository on GitHub and click the **Actions** tab.
2. Select the latest **Build Native Android APK** workflow run.
3. Under **Artifacts**, download **Smoking-Quit-System-Debug-APK** (ready to install on Android devices) or **Smoking-Quit-System-Release-APK**.

### Option 2: Build Locally in Android Studio / Gradle
- Open the repository root in Android Studio (JDK 17, Android SDK 35).
- Or run via CLI:
  ```bash
  ./gradlew testDebugUnitTest assembleDebug assembleRelease
  ```
- Optional Release signing via environment variables (never commit credentials):
  `ANDROID_KEYSTORE_PATH`, `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, `ANDROID_KEY_PASSWORD`.
