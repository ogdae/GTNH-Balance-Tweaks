# GTNH Balance Tweaks

A lightweight addon for [GregTech: New Horizons](https://github.com/GTNewHorizons/GT-New-Horizons-Modpack) that adjusts core gameplay balance to provide a smoother, easier progression experience.

## ✨ Features
- **Global recipe scaling**
  - Reduce recipe times by configurable multipliers.
  - Adjust EU/t (energy usage) across all machines.

- **Custom machine tweaks** *(planned/experimental)*
  - Parallelism, overclocking curve adjustments, idle EU draw reductions.

- **Fuel balancing** *(planned/experimental)*
  - Increase runtime of fuels globally, or improve turbine efficiency.

## ⚙️ Configuration
A config covering global or targeted machine groups is a work in progess:
- This would allow specifying the amount of time and eu/t reduction to be applied.

## 📦 Installation
1. Place the built `.jar` into your `mods/` folder in GTNH.
2. Ensure you are running the same **GregTech: New Horizons version** as the mod was built against.
3. Launch the game — tweaks apply automatically.

## 🛠️ Development
### Requirements
- JDK 17+
- Gradle (use the provided `gradlew` wrapper)

### Build
```bash
./gradlew build
