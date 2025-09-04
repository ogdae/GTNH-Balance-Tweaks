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
Tweaks are controlled through config values in `gradle.properties` or through the mod’s internal config system.
Examples:
- `timeMultiplier = 0.5` → recipes take half as long.
- `euMultiplier = 0.5` → machines consume half the power.
- `fuelEfficiencyMultiplier = 4.0` → fuels last 4× longer.

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
