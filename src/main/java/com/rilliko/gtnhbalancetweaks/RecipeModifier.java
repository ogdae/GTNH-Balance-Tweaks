package com.rilliko.gtnhbalancetweaks;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RecipeModifier {

    public static void modifyAllRecipes() {
        GTNHBalanceTweaksLogger.info("=== Starting Recipe Modification Pass ===");
        GTNHBalanceTweaksLogger.info(
            String.format(
                "Config: RecipeTimeMultiplier=%.3f, EuPerTickMultiplier=%.3f",
                ConfigHandler.recipeTimeMultiplier,
                ConfigHandler.euPerTickMultiplier));

        try {
            // Get RecipeMaps class using reflection
            Class<?> recipeMapsClass = Class.forName("gregtech.api.recipe.RecipeMaps");

            Set<String> targetMapNames = new HashSet<>();
            targetMapNames.add("maceratorRecipes");
            targetMapNames.add("centrifugeRecipes");
            targetMapNames.add("electrolyzerRecipes");
            targetMapNames.add("cutterRecipes");
            targetMapNames.add("benderRecipes");
            targetMapNames.add("wiremillRecipes");
            targetMapNames.add("compressorRecipes");
            targetMapNames.add("extractorRecipes");
            targetMapNames.add("blastFurnaceRecipes");
            targetMapNames.add("arcFurnaceRecipes");
            targetMapNames.add("assemblerRecipes");
            targetMapNames.add("mixerRecipes");
            targetMapNames.add("autoclaveRecipes");
            targetMapNames.add("distillationTowerRecipes");
            targetMapNames.add("chemicalReactorRecipes");
            targetMapNames.add("multiblockChemicalReactorRecipes");
            targetMapNames.add("fluidSolidifierRecipes");
            targetMapNames.add("fluidExtractionRecipes");
            targetMapNames.add("polarizerRecipes");
            targetMapNames.add("oreWasherRecipes");
            targetMapNames.add("thermalCentrifugeRecipes");
            targetMapNames.add("extruderRecipes");
            targetMapNames.add("hammerRecipes");
            targetMapNames.add("latheRecipes");
            targetMapNames.add("chemicalBathRecipes");
            targetMapNames.add("brewingRecipes");
            targetMapNames.add("plasmaArcFurnaceRecipes");
            targetMapNames.add("fluidHeaterRecipes");
            targetMapNames.add("distilleryRecipes");
            targetMapNames.add("vacuumFreezerRecipes");
            targetMapNames.add("alloySmelterRecipes");
            targetMapNames.add("formingPressRecipes");
            targetMapNames.add("laserEngraverRecipes");
            targetMapNames.add("electroMagneticSeparatorRecipes");
            targetMapNames.add("fluidCannerRecipes");
            targetMapNames.add("fermentingRecipes");
            targetMapNames.add("pyrolyseRecipes");
            targetMapNames.add("crackingRecipes");
            targetMapNames.add("circuitAssemblerRecipes");
            targetMapNames.add("cannerRecipes");
            targetMapNames.add("slicerRecipes");

            int totalModified = 0;
            int processedMaps = 0;

            // Get all fields from RecipeMaps class
            Field[] allFields = recipeMapsClass.getDeclaredFields();

            GTNHBalanceTweaksLogger.info("=== Analyzing All Recipe Maps ===");

            for (Field field : allFields) {
                // Only process public static fields that might be recipe maps
                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())
                    || !java.lang.reflect.Modifier.isPublic(field.getModifiers())) {
                    continue;
                }

                String fieldName = field.getName();

                try {
                    Object recipeMap = field.get(null);

                    if (recipeMap == null) {
                        GTNHBalanceTweaksLogger.info("SKIPPED (null): " + fieldName);
                        continue;
                    }

                    // Check if this looks like a recipe map by checking if it has getAllRecipes method
                    try {
                        Method getAllRecipesMethod = recipeMap.getClass()
                            .getMethod("getAllRecipes");
                        @SuppressWarnings("unchecked")
                        Collection<Object> recipes = (Collection<Object>) getAllRecipesMethod.invoke(recipeMap);
                        int recipeCount = recipes.size();

                        // Check if this is a fuel/generator map
                        boolean isFuelMap = fieldName.toLowerCase()
                            .contains("fuel")
                            || fieldName.toLowerCase()
                                .contains("generator")
                            || fieldName.toLowerCase()
                                .contains("turbine");

                        if (targetMapNames.contains(fieldName)) {
                            int modified = processRecipeMap(recipes, fieldName);
                            if (modified > 0) {
                                totalModified += modified;
                                processedMaps++;
                            }
                        } else if (isFuelMap) {
                            GTNHBalanceTweaksLogger
                                .info("SKIPPED (fuel/generator): " + fieldName + " (" + recipeCount + " recipes)");
                        } else if (recipeCount == 0) {
                            GTNHBalanceTweaksLogger.info("SKIPPED (empty): " + fieldName + " (0 recipes)");
                        } else {
                            GTNHBalanceTweaksLogger
                                .info("NOT TARGETED: " + fieldName + " (" + recipeCount + " recipes)");
                        }

                    } catch (NoSuchMethodException e) {
                        // Not a recipe map, skip silently
                    }

                } catch (Exception e) {
                    GTNHBalanceTweaksLogger.warn("Failed to analyze field " + fieldName, e);
                }
            }

            GTNHBalanceTweaksLogger.info("=== Recipe Modification Pass Finished ===");
            GTNHBalanceTweaksLogger
                .info(String.format("Total: %d maps processed, %d recipes modified", processedMaps, totalModified));

            if (totalModified == 0) {
                GTNHBalanceTweaksLogger
                    .warn("No recipes were modified! This might indicate a timing issue with mod loading order.");
            }

        } catch (Exception e) {
            GTNHBalanceTweaksLogger.error("Failed to modify recipes", e);
        }
    }

    private static int processRecipeMap(Collection<?> recipes, String mapName) {
        int modified = 0;
        int skipped = 0;

        for (Object recipe : recipes) {
            if (recipe == null) {
                skipped++;
                continue;
            }

            try {
                // Use getDeclaredField to catch non-public fields
                Field durationField = recipe.getClass()
                    .getDeclaredField("mDuration");
                durationField.setAccessible(true);
                int duration = durationField.getInt(recipe);

                Field eutField = recipe.getClass()
                    .getDeclaredField("mEUt");
                eutField.setAccessible(true);
                int eut = eutField.getInt(recipe);

                if (duration > 0 && eut > 0) {
                    // Apply config multipliers (clamped to at least 1)
                    int newDuration = (int) Math.max(1, Math.round(duration * ConfigHandler.recipeTimeMultiplier));
                    int newEut = (int) Math.max(1, Math.round(eut * ConfigHandler.euPerTickMultiplier));

                    durationField.setInt(recipe, newDuration);
                    eutField.setInt(recipe, newEut);
                    modified++;
                } else {
                    skipped++;
                }
            } catch (Exception e) {
                GTNHBalanceTweaksLogger.warn(
                    "Failed to modify recipe in " + mapName
                        + " (class="
                        + recipe.getClass()
                            .getName()
                        + ")",
                    e);
                skipped++;
            }
        }

        GTNHBalanceTweaksLogger
            .info(String.format("PROCESSED: %s - %d recipes modified, %d skipped", mapName, modified, skipped));
        return modified;
    }
}
