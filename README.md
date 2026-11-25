# Smoking Quit System — By Jawad

A bilingual (English & Roman Urdu) smoking-cessation companion web application migrated from the original Android Jetpack Compose starter project.

## Features
- **First-run setup**: Configure cigarettes per day, pack price (PKR), pack size, quit approach (`Both`, `Gradual reduction`, or `Quit immediately`), and language (`Both`, `English`, or `Roman Urdu`).
- **Dashboard (`Home`)**: Live quit-journey duration counter, estimated cigarettes avoided, estimated money saved (PKR), daily encouragement card, current quit plan target, and a quick health fact.
- **Progress (`Progress`)**: Cumulative time smoke-free, estimated cigarettes avoided, money saved, and general educational health recovery milestones.
- **Craving SOS (`SOS`)**: 2-minute countdown breathing/reset timer and practical distraction steps.
- **Motivation (`Quotes`)**: Bilingual motivational quote cards.
- **Preferences (`Settings`)**: Switch language, toggle browser notifications for the 5 daily reminders (8 AM, 11 AM, 2 PM, 5 PM, 8 PM), switch active quit approach, and reset setup. Data is stored locally in `localStorage`.

## Development
```bash
npm install
npm run dev
```
