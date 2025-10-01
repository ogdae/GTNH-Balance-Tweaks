package com.rilliko.gtnhbalancetweaks.recipe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import com.rilliko.gtnhbalancetweaks.core.ConfigHandler;
import com.rilliko.gtnhbalancetweaks.core.GTNHBalanceTweaksLogger;

public class RecipeModifier {

    public static void modifyAllRecipes() {
        GTNHBalanceTweaksLogger.info("=== Starting Recipe Modification Pass ===");

        try {
            // Reflect into GT RecipeMaps
            Class<?> recipeMapsClass = Class.forName("gregtech.api.recipe.RecipeMaps");

            int totalModified = 0;
            int processedMaps = 0;

            // Get all fields from RecipeMaps
            Field[] allFields = recipeMapsClass.getDeclaredFields();

            for (Field field : allFields) {
                // Only process public static fields
                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())
                    || !java.lang.reflect.Modifier.isPublic(field.getModifiers())) {
                    continue;
                }

                String fieldName = field.getName();

                try {
                    Object recipeMap = field.get(null);
                    if (recipeMap == null) continue;

                    try {
                        Method getAllRecipesMethod = recipeMap.getClass()
                            .getMethod("getAllRecipes");
                        @SuppressWarnings("unchecked")
                        Collection<Object> recipes = (Collection<Object>) getAllRecipesMethod.invoke(recipeMap);

                        // use whitelist helper to decide if we touch this map
                        if (RecipeMapWhitelist.shouldProcessGT(fieldName)) {
                            int modified = processRecipeMap(recipes, fieldName);
                            if (modified > 0) {
                                totalModified += modified;
                                processedMaps++;
                            }
                        }

                    } catch (NoSuchMethodException e) {
                        // Not a recipe map, skip
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

        double timeMult = ConfigHandler.getTimeMultiplier(mapName);
        double euMult = ConfigHandler.getEuMultiplier(mapName);

        for (Object recipe : recipes) {
            if (recipe == null) continue;

            try {
                Field durationField = recipe.getClass()
                    .getDeclaredField("mDuration");
                durationField.setAccessible(true);
                int duration = durationField.getInt(recipe);

                Field eutField = recipe.getClass()
                    .getDeclaredField("mEUt");
                eutField.setAccessible(true);
                int eut = eutField.getInt(recipe);

                if (duration > 0 && eut > 0) {
                    int newDuration = Math.max(1, (int) Math.round(duration * timeMult));
                    int newEut = Math.max(1, (int) Math.round(eut * euMult));

                    durationField.setInt(recipe, newDuration);
                    eutField.setInt(recipe, newEut);
                    modified++;
                }
            } catch (Exception e) {
                GTNHBalanceTweaksLogger.warn(
                    "Failed to modify recipe in " + mapName
                        + " (class="
                        + recipe.getClass()
                            .getName()
                        + ")",
                    e);
            }
        }

        GTNHBalanceTweaksLogger.info(String.format("PROCESSED: %s - %d recipes modified", mapName, modified));
        return modified;
    }
}
