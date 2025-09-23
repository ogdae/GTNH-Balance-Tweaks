# GTNH Balance Tweaks

A lightweight addon mod for GregTech: New Horizons that adjusts the time and power usage of GregTech machines.  
Currently built and tested on **GTNH 2.8.0 RC1**. Presumed to work on future versions, but not guaranteed.  

⚠️ **Disclaimer**: This mod is experimental and untested. It may break long-term worlds. Use at your own risk.  
If you run into issues, please open a ticket on GitHub or contact me directly on Discord (`rilliko`).  

---

## ✨ Features
- Halves recipe time and EU/t for most GregTech machines by default.  
- Primitive Blast Furnace recipes run 4× faster.  
- Configurable multipliers so you can tune recipes easier *or* harder depending on your preference.    
- Works on both client and server.  

---

## ⚙️ Configuration
A config file is generated at:  
`/config/gtnhbalancetweaks.cfg`  

You can adjust multipliers on a **per-machine basis**, or globally if you want everything treated the same:  

- `RecipeTimeMultiplier` → default `0.5`  
- `EuPerTickMultiplier` → default `0.5`  
- `PrimitiveBlastTimeMultiplier` → default `0.25`  

Examples:  
- `0.1` → recipes are 10× faster and 10× cheaper.  
- `2.0` → recipes are 2× slower and 2× more expensive.  
- Per-machine overrides let you make, for example, **cutting machines cheap**, but **furnaces expensive**.  

---

## 🏭 Machines Covered
This mod tweaks nearly all GregTech singleblock and multiblock machines, including:  

- **Basic processing**: Macerator, Compressor, Extractor, Ore Washer, Thermal Centrifuge  
- **Furnaces & smelters**: Arc Furnace, Plasma Arc Furnace, Blast Furnace, Alloy Smelter, Vacuum Freezer  
- **Forming & cutting**: Cutter, Wiremill, Bender, Lathe, Extruder, Hammer, Slicer, Forming Press, Laser Engraver  
- **Chemical & fluids**: Chemical Reactor, Multiblock Chemical Reactor, Mixer, Autoclave, Chemical Bath, Distillery, Fluid Heater, Fluid Solidifier, Fluid Extractor, Fluid Canner, Fermenter, Polarizer, Brewing  
- **Assembly & misc**: Assembler, Circuit Assembler, Canner  
- **Special case**: Primitive Blast Furnace (handled separately with its own multiplier)  

GT++ maps (~6,300 recipes) and Railcraft Coke Oven recipes are also supported.  

---

## 📌 Compatibility
- Designed for **GregTech: New Horizons 2.8.0 RC1**  
- Expected to work with future versions, but not guaranteed  

---

## 🔮 Planned Features
- Support for endgame multiblocks (Assembly Line, Fusion, Eye of Harmony, etc.)  
- More granular configuration (toggle entire groups like "furnaces" or "chemicals")  
- Generator tweaks (fuel efficiency and multiblock output scaling)  

---

## 📦 Download
Get the latest release from the [Releases page](../../releases).  

---

## 🙏 Credits
Huge thanks to the GTNH team for their incredible work maintaining and expanding the pack.  
This mod started life from the ExampleMod template.  

---

## ⚠️ Additional Disclaimer
This code was created with major assistance from ChatGPT. While every effort has been made to test functionality, unexpected behavior may occur. Don't expect fantastic work if you take a look at the code yourself, I have no idea what I'm doing, but the mod works well enough for me and my friends. Again, use at your own risk.
