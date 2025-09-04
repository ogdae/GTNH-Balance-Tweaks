package com.rilliko.gtnhbalancetweaks;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RecipeModifier {

    public static void modifyAllRecipes() {
        GTNHBalanceTweaksLogger.info("=== Starting Recipe Modification Pass ===");

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
            // primitiveBlastRecipes is handled separately now

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
                        Collection<?> recipes = (Collection<?>) getAllRecipesMethod.invoke(recipeMap);
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
                Field durationField = recipe.getClass()
                    .getField("mDuration");
                int duration = durationField.getInt(recipe);

                Field eutField = recipe.getClass()
                    .getField("mEUt");
                int eut = eutField.getInt(recipe);

                if (duration > 0 && eut > 0) {
                    durationField.setInt(recipe, Math.max(1, duration / 2));
                    eutField.setInt(recipe, Math.max(1, eut / 2));
                    modified++;
                } else {
                    skipped++;
                }
            } catch (Exception e) {
                GTNHBalanceTweaksLogger.warn("Failed to modify individual recipe in " + mapName, e);
                skipped++;
            }
        }

        GTNHBalanceTweaksLogger
            .info(String.format("PROCESSED: %s - %d recipes modified, %d skipped", mapName, modified, skipped));
        return modified;
    }
}
