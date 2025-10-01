# Changelog

All notable changes to **GTNH Balance Tweaks** will be documented here.
This project follows simple semantic-style versioning: *MAJOR.MINOR.PATCH*.

---

## [3.1.0] - 2025-10-01
## Added
- EV-tier variants of the ME Crafting Input hatches:
  - New Crafting Input Buffer (Fluids) and Proxy (Slave) now available at EV tier.
  - Assembler recipes use EV components (Hull, Circuits, Sensors, Emitters, Field Generators) and EV Superconductor Base.
  - Function identically to the original UIV hatches but unlock automation integration much earlier.
- NEI integration tweaks:
  - Original UIV Crafting Input Buffer, Bus, and Proxy hatches are now hidden from NEI.
  - Recipes remain craftable for backwards compatibility.

## Changed
- Descriptions of the new EV hatches updated to display the correct EV tier.
- Cleaned up RecipeHandler and MetaTileRegistry:
  - Removed debug logging and redundant comments.
  - Simplified helper methods for clarity.
- Refactored project structure for better separation of core, registry, recipe, and machine classes.


## [2.2.0] - 2025-09-04
### Added
- Config file generated at `config/gtnhbalancetweaks.cfg`:
  - `RecipeTimeMultiplier` (default **0.5**)
  - `EuPerTickMultiplier` (default **0.5**)
  - `PrimitiveBlastTimeMultiplier` (default **0.25**)
- Support for additional machines:
  - Forming Press
  - Laser Engraver
  - Electromagnetic Separator
  - Fluid Canner
  - Fermenter
  - Pyrolyse Oven
  - Cracking Unit
  - Circuit Assembler
  - Canner
  - Slicer

### Changed
- Recipe modifications now run during **FMLLoadComplete** → ensures all late-added recipes (Minetweaker/GTNH scripts) are caught.
- Reflection updated (`getDeclaredField`) → fixes skipped recipes with private/protected fields.
- Expanded and clarified logging of skipped/failed recipes.

---

## [2.1.0] - 2025-09-01
### Added
- Dedicated **Primitive Blast Furnace handler**:
  - Reduces recipe time by 4x by default (EU/t unchanged).

### Changed
- Internal config system introduced, but not yet public.

---

## [2.0.2] - 2025-08-20
### Initial Public Release
- Halves recipe time and EU/t for a large set of GregTech machines.
- Primitive Blast Furnace temporarily excluded (handled manually in later versions).
