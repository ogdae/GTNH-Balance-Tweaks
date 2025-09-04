package com.rilliko.gtnhbalancetweaks;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@Mod(
    modid = GTNHBalanceTweaks.MODID,
    name = "GTNH Balance Tweaks",
    version = GTNHBalanceTweaks.VERSION,
    dependencies = "required-after:gregtech")
public class GTNHBalanceTweaks {

    public static final String MODID = "gtnhbalancetweaks";
    public static final String VERSION = "2.0.2";

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        GTNHBalanceTweaksLogger.info("Starting recipe modifications...");
        RecipeModifier.modifyAllRecipes();
        GTNHBalanceTweaksLogger.info("Recipe modifications finished!");
    }

    @EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        GTNHBalanceTweaksLogger.info("Running special Primitive Blast Furnace tweaks...");
        // Call into your dedicated PBF tweaker class
        com.rilliko.gtnhbalancetweaks.PrimitiveBlastTweaker.tweak();
        GTNHBalanceTweaksLogger.info("Primitive Blast Furnace tweaks finished!");
    }
}
