package com.rilliko.gtnhbalancetweaks.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;

import com.rilliko.gtnhbalancetweaks.recipe.RecipeMapWhitelist;

public class ConfigHandler {

    private static Configuration config;

    // Global defaults
    public static double defaultTimeMultiplier = 0.5;
    public static double defaultEuMultiplier = 0.5;
    public static double primitiveBlastTimeMultiplier = 0.25;

    // Per-map overrides
    private static final Map<String, Double> timeMultipliers = new HashMap<>();
    private static final Map<String, Double> euMultipliers = new HashMap<>();

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfig();
        }
    }

    private static void loadConfig() {
        try {
            // === [general] ===
            defaultTimeMultiplier = clamp(
                config
                    .get(
                        "general",
                        "DefaultRecipeTimeMultiplier",
                        0.5,
                        "Global multiplier for recipe duration.\n"
                            + "Examples: 0.5 = 50% time (2x faster), 1.0 = normal, 2.0 = 200% time (2x slower).\n"
                            + "Hierarchy: Per-machine overrides > global default.")
                    .getDouble(0.5));

            defaultEuMultiplier = clamp(
                config
                    .get(
                        "general",
                        "DefaultEuPerTickMultiplier",
                        0.5,
                        "Global multiplier for EU/t usage.\n"
                            + "Examples: 0.5 = 50% EU/t (half cost), 1.0 = normal, 2.0 = 200% EU/t (double cost).\n"
                            + "Hierarchy: Per-machine overrides > global default.")
                    .getDouble(0.5));

            // === [primitiveblastfurnace] ===
            primitiveBlastTimeMultiplier = clamp(
                config
                    .get(
                        "primitiveblastfurnace",
                        "TimeMultiplier",
                        0.25,
                        "Independent setting for Primitive Blast Furnace.\n" + "Does not use global defaults.\n"
                            + "Examples: 0.25 = 25% time (4x faster), 1.0 = normal, 2.0 = 200% time (2x slower).")
                    .getDouble(0.25));

            // === [machines] ===
            String machinesCat = "machines";
            config.addCustomCategoryComment(
                machinesCat,
                "Per-machine multipliers.\n" + "These override [general] if set.\n"
                    + "Examples: 0.5 = 50% (faster/cheaper), 1.0 = normal, 2.0 = 200% (slower/more expensive).");

            timeMultipliers.clear();
            euMultipliers.clear();

            for (String mapName : RecipeMapWhitelist.TARGET_MAPS) {
                double time = clamp(
                    config.get(machinesCat, mapName + "_TimeMultiplier", defaultTimeMultiplier, "")
                        .getDouble(defaultTimeMultiplier));

                double eu = clamp(
                    config.get(machinesCat, mapName + "_EuPerTickMultiplier", defaultEuMultiplier, "")
                        .getDouble(defaultEuMultiplier));

                timeMultipliers.put(mapName, time);
                euMultipliers.put(mapName, eu);
            }

            // === [gtpp_machines] ===
            String gtppCat = "gtpp_machines";
            config.addCustomCategoryComment(
                gtppCat,
                "GT++ multiblocks and machines.\n" + "Same rules as [machines]: overrides [general] if set.");

            for (String mapName : RecipeMapWhitelist.GTPP_TARGET_MAPS) {
                double time = clamp(
                    config.get(gtppCat, mapName + "_TimeMultiplier", defaultTimeMultiplier, "")
                        .getDouble(defaultTimeMultiplier));

                double eu = clamp(
                    config.get(gtppCat, mapName + "_EuPerTickMultiplier", defaultEuMultiplier, "")
                        .getDouble(defaultEuMultiplier));

                timeMultipliers.put(mapName, time);
                euMultipliers.put(mapName, eu);
            }

        } catch (Exception e) {
            GTNHBalanceTweaksLogger.error("Error loading config, using defaults", e);
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    private static double clamp(double val) {
        return val <= 0 ? 0.01 : val;
    }

    // === Public accessors ===
    public static double getTimeMultiplier(String mapName) {
        return timeMultipliers.getOrDefault(mapName, defaultTimeMultiplier);
    }

    public static double getEuMultiplier(String mapName) {
        return euMultipliers.getOrDefault(mapName, defaultEuMultiplier);
    }
}
