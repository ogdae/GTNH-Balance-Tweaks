package com.rilliko.gtnhbalancetweaks;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = GTNHBalanceTweaks.MODID, name = "GTNH Balance Tweaks", version = GTNHBalanceTweaks.VERSION, dependencies =
// Ensure GregTech first, then optional mods are already registered by the time we touch them
"required-after:gregtech;" + "after:miscutils;" + // GT++ (GTNH builds)
    "after:GTplusplus;" + // GT++ (alt id seen in some jars)
    "after:Railcraft" // Railcraft (Coke Oven)
)
public class GTNHBalanceTweaks {

    public static final String MODID = "gtnhbalancetweaks";
    public static final String VERSION = "3.0.0";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Load or create config
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        GTNHBalanceTweaksLogger.info(
            "Config initialized. DefaultRecipeTimeMultiplier=" + ConfigHandler.defaultTimeMultiplier
                + ", DefaultEuPerTickMultiplier="
                + ConfigHandler.defaultEuMultiplier
                + ", PrimitiveBlastTimeMultiplier="
                + ConfigHandler.primitiveBlastTimeMultiplier);
    }

    @EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        // === Standard GT recipe tweaks ===
        try {
            GTNHBalanceTweaksLogger.info("Starting recipe modifications...");
            RecipeModifier.modifyAllRecipes();
            GTNHBalanceTweaksLogger.info("Recipe modifications finished!");
        } catch (Throwable t) {
            GTNHBalanceTweaksLogger.info(
                "GT recipe pass failed: " + t.getClass()
                    .getSimpleName() + ": " + t.getMessage());
        }

        // === GT++ recipe tweaks === (only if GT++ present)
        try {
            boolean gtppPresent = Loader.isModLoaded("miscutils") || Loader.isModLoaded("GTplusplus");
            if (gtppPresent) {
                GTNHBalanceTweaksLogger.info("Starting GTPP recipe modifications...");
                RecipeModifierGTPP.modifyAllRecipes();
                GTNHBalanceTweaksLogger.info("GTPP recipe modifications finished!");
            } else {
                GTNHBalanceTweaksLogger.info("GT++ not detected; skipping GTPP recipe modifications.");
            }
        } catch (Throwable t) {
            GTNHBalanceTweaksLogger.info(
                "GTPP recipe pass failed: " + t.getClass()
                    .getSimpleName() + ": " + t.getMessage());
        }

        // === Railcraft Coke Oven: hard 50% time on all recipes (if Railcraft present) ===
        try {
            if (Loader.isModLoaded("Railcraft")) {
                GTNHBalanceTweaksLogger.info("Starting Railcraft Coke Oven modifications (hard 50% time)...");
                RecipeModifierRailcraft.modifyRailcraftCokeOven();
                GTNHBalanceTweaksLogger.info("Railcraft recipe modifications finished!");
            } else {
                GTNHBalanceTweaksLogger.info("Railcraft not detected; skipping Railcraft recipe modifications.");
            }
        } catch (Throwable t) {
            GTNHBalanceTweaksLogger.info(
                "Railcraft pass failed: " + t.getClass()
                    .getSimpleName() + ": " + t.getMessage());
        }

        // === Primitive Blast Furnace tweaks ===
        try {
            GTNHBalanceTweaksLogger.info("Running Primitive Blast Furnace tweaks...");
            PrimitiveBlastTweaker.tweak();
            GTNHBalanceTweaksLogger.info("Primitive Blast Furnace tweaks finished!");
        } catch (Throwable t) {
            GTNHBalanceTweaksLogger.info(
                "PBF pass failed: " + t.getClass()
                    .getSimpleName() + ": " + t.getMessage());
        }
    }
}
