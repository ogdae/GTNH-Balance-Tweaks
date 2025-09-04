# Changelog

All notable changes to **GTNH Balance Tweaks** will be documented here.
This project follows simple semantic-style versioning: *MAJOR.MINOR.PATCH*.

---

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
