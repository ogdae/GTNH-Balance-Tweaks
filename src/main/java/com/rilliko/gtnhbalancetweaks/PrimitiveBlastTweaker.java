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
            @SuppressWarnings("unchecked")
            Collection<Object> recipes = (Collection<Object>) getAllRecipes.invoke(recipeMap);

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

                    // PBF recipes always have eut == 0, only time matters
                    if (duration > 0) {
                        int newDuration = (int) Math
                            .max(1, Math.round(duration * ConfigHandler.primitiveBlastTimeMultiplier));

                        durationField.setInt(recipe, newDuration);
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
