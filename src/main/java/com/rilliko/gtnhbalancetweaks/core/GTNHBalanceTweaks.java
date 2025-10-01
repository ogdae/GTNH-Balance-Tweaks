package com.rilliko.gtnhbalancetweaks.core;

import com.rilliko.gtnhbalancetweaks.integration.NEIHideHandler;
import com.rilliko.gtnhbalancetweaks.recipe.PrimitiveBlastTweaker;
import com.rilliko.gtnhbalancetweaks.recipe.RecipeModifier;
import com.rilliko.gtnhbalancetweaks.recipe.RecipeModifierGTPP;
import com.rilliko.gtnhbalancetweaks.recipe.RecipeModifierRailcraft;
import com.rilliko.gtnhbalancetweaks.registry.MetaTileRegistry;
import com.rilliko.gtnhbalancetweaks.registry.RecipeHandler;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
    modid = GTNHBalanceTweaks.MODID,
    name = "GTNH Balance Tweaks",
    version = GTNHBalanceTweaks.VERSION,
    dependencies = "required-after:gregtech;" + "required-after:appliedenergistics2;"
        + "after:miscutils;"
        + "after:GTplusplus;"
        + "after:Railcraft")
public class GTNHBalanceTweaks {

    public static final String MODID = "gtnhbalancetweaks";
    public static final String VERSION = "3.1.0";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Load config
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        GTNHBalanceTweaksLogger.info(
            "Config initialized. DefaultRecipeTimeMultiplier=" + ConfigHandler.defaultTimeMultiplier
                + ", DefaultEuPerTickMultiplier="
                + ConfigHandler.defaultEuMultiplier
                + ", PrimitiveBlastTimeMultiplier="
                + ConfigHandler.primitiveBlastTimeMultiplier);

        // Register our custom hatches
        try {
            GTNHBalanceTweaksLogger.info("[BTX] preInit ENTER - registering ME+ hatches");
            MetaTileRegistry.registerAll();
            GTNHBalanceTweaksLogger.info("[BTX] MetaTileRegistry.registerAll() completed");
        } catch (Throwable t) {
            GTNHBalanceTweaksLogger.error("[BTX] MetaTile registration failed", t);
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // === Core GT recipe modifications ===
        try {
            GTNHBalanceTweaksLogger.info("Starting GT recipe modifications...");
            RecipeModifier.modifyAllRecipes();
            GTNHBalanceTweaksLogger.info("GT recipe modifications finished!");
        } catch (Throwable t) {
            GTNHBalanceTweaksLogger.error("GT recipe pass failed", t);
        }

        // === GT++ / MiscUtils recipe modifications ===
        try {
            if (Loader.isModLoaded("miscutils") || Loader.isModLoaded("GTplusplus")) {
                GTNHBalanceTweaksLogger.info("Starting GT++ recipe modifications...");
                RecipeModifierGTPP.modifyAllRecipes();
                GTNHBalanceTweaksLogger.info("GT++ recipe modifications finished!");
            } else {
                GTNHBalanceTweaksLogger.info("GT++ not detected; skipping recipe modifications.");
            }
        } catch (Throwable t) {
            GTNHBalanceTweaksLogger.error("GT++ recipe pass failed", t);
        }

        // === Railcraft Coke Oven tweaks ===
        try {
            if (Loader.isModLoaded("Railcraft")) {
                GTNHBalanceTweaksLogger.info("Starting Railcraft Coke Oven modifications (hard 50% time)...");
                RecipeModifierRailcraft.modifyRailcraftCokeOven();
                GTNHBalanceTweaksLogger.info("Railcraft recipe modifications finished!");
            } else {
                GTNHBalanceTweaksLogger.info("Railcraft not detected; skipping recipe modifications.");
            }
        } catch (Throwable t) {
            GTNHBalanceTweaksLogger.error("Railcraft pass failed", t);
        }

        // === Primitive Blast Furnace tweaks ===
        try {
            GTNHBalanceTweaksLogger.info("Running Primitive Blast Furnace tweaks...");
            PrimitiveBlastTweaker.tweak();
            GTNHBalanceTweaksLogger.info("Primitive Blast Furnace tweaks finished!");
        } catch (Throwable t) {
            GTNHBalanceTweaksLogger.error("Primitive Blast Furnace pass failed", t);
        }
    }

    @EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        // === Register assembler recipes for our custom hatches ===
        try {
            GTNHBalanceTweaksLogger.info("[BTX] loadComplete ENTER - registering custom recipes");
            RecipeHandler.registerAll();
            GTNHBalanceTweaksLogger.info("[BTX] RecipeHandler.registerAll() completed");
        } catch (Throwable t) {
            GTNHBalanceTweaksLogger.error("[BTX] Custom recipe registration failed", t);
        }
    }

    @EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        NEIHideHandler.hideOldMEHatches();
    }
}
