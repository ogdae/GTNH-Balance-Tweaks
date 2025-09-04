package com.rilliko.gtnhbalancetweaks;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
    modid = GTNHBalanceTweaks.MODID,
    name = "GTNH Balance Tweaks",
    version = GTNHBalanceTweaks.VERSION,
    dependencies = "required-after:gregtech")
public class GTNHBalanceTweaks {

    public static final String MODID = "gtnhbalancetweaks";
    public static final String VERSION = "2.2.0"; // bumped since all tweaks now run in loadComplete

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Load or create config
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        GTNHBalanceTweaksLogger.info(
            "Config initialized. " + "RecipeTimeMultiplier="
                + ConfigHandler.recipeTimeMultiplier
                + ", EuPerTickMultiplier="
                + ConfigHandler.euPerTickMultiplier
                + ", PrimitiveBlastTimeMultiplier="
                + ConfigHandler.primitiveBlastTimeMultiplier);
    }

    @EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        // Apply tweaks to all standard machine recipes
        GTNHBalanceTweaksLogger.info("Starting recipe modifications...");
        RecipeModifier.modifyAllRecipes();
        GTNHBalanceTweaksLogger.info("Recipe modifications finished!");

        // Apply Primitive Blast Furnace tweaks after everything else
        GTNHBalanceTweaksLogger.info("Running Primitive Blast Furnace tweaks...");
        PrimitiveBlastTweaker.tweak();
        GTNHBalanceTweaksLogger.info("Primitive Blast Furnace tweaks finished!");
    }
}
