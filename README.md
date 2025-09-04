# GTNH Balance Tweaks

A lightweight addon mod for [GregTech: New Horizons](https://github.com/GTNewHorizons/GT-New-Horizons-Modpack) that adjusts the **time** and **power usage** of GregTech machines.  
Currently built and tested with **GTNH 2.8.0-beta-3**.

---

## ✨ Features
- Halves recipe time and EU/t for most GregTech machines.  
- Special handling for the **Primitive Blast Furnace**: recipe time reduced by 4x (EU/t unchanged).  
- Configurable multipliers so you can **make the game easier or harder** depending on your preference.  
- Safe defaults for balanced play

---

## ⚙️ Configuration
A config file is generated at: /config/gtnhbalancetweaks.cfg

You can adjust these settings:
- `RecipeTimeMultiplier` → default `0.5`  
- `EuPerTickMultiplier` → default `0.5`  
- `PrimitiveBlastTimeMultiplier` → default `0.25`  

Examples:
- Set to `0.1` → recipes 10x faster, 10x cheaper  
- Set to `2.0` → recipes 2x slower, 2x more expensive

---

## 🏭 Machines Covered
All recipes for these machines are currently modified:

- **Basic processing**: Macerator, Compressor, Extractor, Ore Washer, Thermal Centrifuge  
- **Furnaces & smelters**: Arc Furnace, Plasma Arc Furnace, Blast Furnace, Alloy Smelter, Vacuum Freezer  
- **Forming & cutting**: Cutter, Wiremill, Bender, Lathe, Extruder, Hammer, Slicer, Forming Press, Laser Engraver  
- **Chemical & fluids**: Chemical Reactor, Multiblock Chemical Reactor, Mixer, Autoclave, Chemical Bath, Distillery, Fluid Heater, Fluid Solidifier, Fluid Extractor, Fluid Canner, Fermenter, Polarizer, Brewing  
- **Assembly & misc**: Assembler, Circuit Assembler, Canner  

Special case: **Primitive Blast Furnace** handled separately with its own multiplier.  

---

## 📌 Compatibility
- Built for **GregTech: New Horizons 2.8.0-beta-3**   
- Should work fine on servers and clients alike.  

---

## 🛠 Issues & Support
- Open an [issue on GitHub](../../issues)  
- Or contact me directly on Discord: **rilliko**  

---

## 🔮 Planned Features
- Support for all **Assembly Line** recipes  
- Support for late-game multiblocks (Fusion, Eye of Harmony, Plasma Forge, etc.)  
- Support for modifying **individual groups of machines** (e.g. only furnaces, only chemical machines, etc.)
- Increased fuel efficiency for single block generators, and increased power output modifiers for multiblock generators
- Deeper configuration and tweakability

---

## 📦 Download
Grab the latest release from the [Releases page](../../releases).  

---

## 🙏 Credits
- Huge thanks to the [GTNH team](https://github.com/GTNewHorizons) for their incredible work maintaining and expanding the pack.  
- This mod was created using the **[ExampleMod template](https://github.com/GTNewHorizons/ExampleMod1.7.10)** as a starting point.

---


