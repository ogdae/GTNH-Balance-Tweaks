package com.rilliko.gtnhbalancetweaks;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

public class PrimitiveBlastTweaker {

    public static void tweak() {
        try {
            Class<?> recipeMapsClass = Class.forName("gregtech.api.recipe.RecipeMaps");
            Field primitiveBlastField = recipeMapsClass.getField("primitiveBlastRecipes");
            Object recipeMap = primitiveBlastField.get(null);

            // Call getAllRecipes to fetch what backend compiled
            Method getAllRecipes = recipeMap.getClass()
                .getMethod("getAllRecipes");
            Collection<?> recipes = (Collection<?>) getAllRecipes.invoke(recipeMap);

            int modified = 0;
            int skipped = 0;

            for (Object recipe : recipes) {
                if (recipe == null) continue;

                try {
                    Field durationField = recipe.getClass()
                        .getField("mDuration");
                    int duration = durationField.getInt(recipe);

                    // Primitive Blast Furnace always has eut == 0
                    if (duration > 0) {
                        durationField.setInt(recipe, Math.max(1, duration / 4));
                        modified++;
                    } else {
                        skipped++;
                    }
                } catch (Exception inner) {
                    skipped++;
                }
            }

            GTNHBalanceTweaksLogger
                .info(String.format("Primitive Blast Furnace: %d recipes modified, %d skipped", modified, skipped));
        } catch (Exception e) {
            GTNHBalanceTweaksLogger.error("Failed to tweak Primitive Blast Furnace", e);
        }
    }
}
