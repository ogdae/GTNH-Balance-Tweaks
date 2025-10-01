package com.rilliko.gtnhbalancetweaks.recipe;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class RecipeMapWhitelist {

    private RecipeMapWhitelist() {}

    // === GT Maps ===
    public static final String[] TARGET_MAPS = { "maceratorRecipes", "centrifugeRecipes", "electrolyzerRecipes",
        "cutterRecipes", "benderRecipes", "wiremillRecipes", "compressorRecipes", "extractorRecipes",
        "blastFurnaceRecipes", "arcFurnaceRecipes", "assemblerRecipes", "mixerRecipes", "autoclaveRecipes",
        "distillationTowerRecipes", "chemicalReactorRecipes", "multiblockChemicalReactorRecipes",
        "fluidSolidifierRecipes", "fluidExtractionRecipes", "polarizerRecipes", "oreWasherRecipes",
        "thermalCentrifugeRecipes", "extruderRecipes", "hammerRecipes", "latheRecipes", "chemicalBathRecipes",
        "brewingRecipes", "plasmaArcFurnaceRecipes", "fluidHeaterRecipes", "distilleryRecipes", "vacuumFreezerRecipes",
        "alloySmelterRecipes", "formingPressRecipes", "laserEngraverRecipes", "electroMagneticSeparatorRecipes",
        "fluidCannerRecipes", "fermentingRecipes", "pyrolyseRecipes", "crackingRecipes", "circuitAssemblerRecipes",
        "cannerRecipes", "slicerRecipes", "sifterRecipes" };

    // === GT++ maps ===
    public static final String[] GTPP_TARGET_MAPS = { "cokeOvenRecipes", "alloyBlastSmelterRecipes",
        "vacuumFurnaceRecipes", "chemicalDehydratorRecipes", "millingRecipes", "coldTrapRecipes", "simpleWasherRecipes",
        "molecularTransformerRecipes", "chemicalPlantRecipes", "spargeTowerRecipes", "advancedFreezerRecipes",
        "centrifugeNonCellRecipes", "electrolyzerNonCellRecipes", "mixerNonCellRecipes",
        "chemicalDehydratorNonCellRecipes", "flotationCellRecipes", "treeGrowthSimulatorFakeRecipes",
        "multiblockRockBreakerRecipes", "cyclotronRecipes", "fishPondRecipes",
        // Special/edge-case, optional to tweak
        "quantumForceTransformerRecipes", "multiblockMassFabricatorRecipes" };

    private static final Set<String> GT_SET = new HashSet<>();
    private static final Set<String> GTPP_SET = new HashSet<>();

    static {
        Collections.addAll(GT_SET, TARGET_MAPS);
        Collections.addAll(GTPP_SET, GTPP_TARGET_MAPS);
    }

    // Base predicates
    public static boolean hasGTWhitelist(String mapName) {
        return GT_SET.contains(mapName);
    }

    public static boolean hasGTppWhitelist(String mapName) {
        return GTPP_SET.contains(mapName);
    }

    // Convenience wrappers used by modifiers
    public static boolean shouldProcessGT(String mapName) {
        return hasGTWhitelist(mapName);
    }

    public static boolean shouldProcessGTPP(String mapName) {
        return hasGTppWhitelist(mapName);
    }

    // Optional: read-only views
    public static Set<String> gtMaps() {
        return Collections.unmodifiableSet(GT_SET);
    }

    public static Set<String> gtppMaps() {
        return Collections.unmodifiableSet(GTPP_SET);
    }
}
