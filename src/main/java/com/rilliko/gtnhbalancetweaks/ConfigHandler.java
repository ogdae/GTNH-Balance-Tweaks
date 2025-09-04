package com.rilliko.gtnhbalancetweaks;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    private static Configuration config;

    // Defaults
    public static double recipeTimeMultiplier = 0.5; // half-time
    public static double euPerTickMultiplier = 0.5; // half EU/t
    public static double primitiveBlastTimeMultiplier = 0.25; // quarter time (4x faster)

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfig();
        }
    }

    private static void loadConfig() {
        try {
            recipeTimeMultiplier = config
                .get(
                    "general",
                    "RecipeTimeMultiplier",
                    0.5,
                    "Multiplier for recipe duration (most machines). Default 0.5 = half time.")
                .getDouble(0.5);

            euPerTickMultiplier = config
                .get(
                    "general",
                    "EuPerTickMultiplier",
                    0.5,
                    "Multiplier for EU/t consumption (most machines). Default 0.5 = half EU/t.")
                .getDouble(0.5);

            primitiveBlastTimeMultiplier = config
                .get(
                    "general",
                    "PrimitiveBlastTimeMultiplier",
                    0.25,
                    "Multiplier for Primitive Blast Furnace recipe time. Default 0.25 = quarter time (4x faster).")
                .getDouble(0.25);

            // Safety clamps (avoid invalid values)
            if (recipeTimeMultiplier <= 0) recipeTimeMultiplier = 0.01;
            if (euPerTickMultiplier <= 0) euPerTickMultiplier = 0.01;
            if (primitiveBlastTimeMultiplier <= 0) primitiveBlastTimeMultiplier = 0.01;

        } catch (Exception e) {
            GTNHBalanceTweaksLogger.error("Error loading config, using defaults", e);
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }
}
