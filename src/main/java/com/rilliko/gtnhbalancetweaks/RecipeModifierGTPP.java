package com.rilliko.gtnhbalancetweaks;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class RecipeModifierGTPP {

    private RecipeModifierGTPP() {}

    // Optional extra safety: even though we now use a whitelist, keep a tiny blacklist
    // to catch any odd map names that sneak in later (fuels/generators).
    private static final Set<String> BLACKLIST_EXACT = new HashSet<String>();
    private static final Set<String> BLACKLIST_PARTIAL = new HashSet<String>();

    static {
        // Common GT fuel/generator names (defensive only)
        BLACKLIST_EXACT.add("dieselFuels");
        BLACKLIST_EXACT.add("gasTurbineFuels");
        BLACKLIST_EXACT.add("hotFuels");
        BLACKLIST_EXACT.add("plasmaFuels");
        BLACKLIST_EXACT.add("magicFuels");
        BLACKLIST_EXACT.add("denseLiquidFuels");
        BLACKLIST_EXACT.add("largeBoilerFakeFuels");

        // Partial matches to skip power/fuel related maps if encountered
        final String[] partials = new String[] { "fuel", "generator", "turbine", "boiler", "engine", "plasma", "diesel",
            "rocket", "solar", "rtg", "reactor", "fission", "thorium", "naquadah", "nuclear" };
        for (String s : partials) BLACKLIST_PARTIAL.add(s.toLowerCase(Locale.ROOT));
    }

    public static void modifyAllRecipes() {
        try {
            // Correct GT++ map holder for GTNH
            final Class<?> mapHolder = Class.forName("gtPlusPlus.api.recipe.GTPPRecipeMaps");

            int totalModified = 0;
            int processedMaps = 0;

            GTNHBalanceTweaksLogger.info("=== GT++ Recipe Modification Pass ===");

            for (Field field : mapHolder.getDeclaredFields()) {
                final int mods = field.getModifiers();
                if (!Modifier.isPublic(mods) || !Modifier.isStatic(mods)) continue;

                final String mapFieldName = field.getName();

                // Use the new whitelist (safe GT++ maps only)
                if (!RecipeMapWhitelist.shouldProcessGTPP(mapFieldName)) {
                    continue;
                }

                try {
                    final Object recipeMap = field.get(null);
                    if (recipeMap == null) continue;

                    // Defensive blacklist (should be redundant with whitelist, but cheap)
                    if (isBlacklistedMap(recipeMap, mapFieldName)) {
                        GTNHBalanceTweaksLogger
                            .info("SKIP (GT++ blacklist): " + displayMapName(recipeMap, mapFieldName));
                        continue;
                    }

                    final Collection<?> recipes = tryGetAllRecipesFromRecipeMap(recipeMap);
                    if (recipes == null) continue;

                    final String display = displayMapName(recipeMap, mapFieldName);
                    final int modified = processRecipeCollection(recipes, display);
                    if (modified > 0) {
                        totalModified += modified;
                        processedMaps++;
                    }
                } catch (Throwable t) {
                    GTNHBalanceTweaksLogger.warn("Failed to process GT++ map: " + mapFieldName, t);
                }
            }

            GTNHBalanceTweaksLogger.info("=== GT++ Recipe Modification Pass Finished ===");
            GTNHBalanceTweaksLogger
                .info(String.format("GT++: %d maps processed, %d recipes modified", processedMaps, totalModified));

        } catch (ClassNotFoundException e) {
            // GT++ not present
            GTNHBalanceTweaksLogger
                .info("GT++ not detected (missing gtPlusPlus.api.recipe.GTPPRecipeMaps). Skipping GTPP pass.");
        } catch (Throwable t) {
            GTNHBalanceTweaksLogger.error("Failed to process GT++ recipes", t);
        }
    }

    // ----- helpers -----

    private static boolean isBlacklistedMap(Object recipeMap, String fieldName) {
        final String fieldLc = fieldName.toLowerCase(Locale.ROOT);
        if (BLACKLIST_EXACT.contains(fieldName) || BLACKLIST_EXACT.contains(fieldLc)) return true;
        for (String sub : BLACKLIST_PARTIAL) if (fieldLc.contains(sub)) return true;

        // Also check runtime display name, if available
        try {
            final Method getName = recipeMap.getClass()
                .getMethod("getName");
            final Object nameObj = getName.invoke(recipeMap);
            if (nameObj instanceof String) {
                final String name = ((String) nameObj);
                final String nameLc = name.toLowerCase(Locale.ROOT);
                if (BLACKLIST_EXACT.contains(name) || BLACKLIST_EXACT.contains(nameLc)) return true;
                for (String sub : BLACKLIST_PARTIAL) if (nameLc.contains(sub)) return true;
            }
        } catch (Throwable ignored) {}

        return false;
    }

    private static String displayMapName(Object recipeMap, String fallback) {
        try {
            final Method getName = recipeMap.getClass()
                .getMethod("getName");
            final Object nameObj = getName.invoke(recipeMap);
            if (nameObj instanceof String) return (String) nameObj;
        } catch (Throwable ignored) {}
        return fallback;
    }

    @SuppressWarnings("unchecked")
    private static Collection<?> tryGetAllRecipesFromRecipeMap(Object recipeMap) {
        try {
            // Preferred path in GTNH GT5u: map.getBackend().getAllRecipes()
            final Method getBackend = recipeMap.getClass()
                .getMethod("getBackend");
            final Object backend = getBackend.invoke(recipeMap);
            if (backend != null) {
                // Common method names
                for (String m : new String[] { "getAllRecipes", "getRecipes", "getRecipeList" }) {
                    try {
                        final Method getter = backend.getClass()
                            .getMethod(m);
                        final Object val = getter.invoke(backend);
                        if (val instanceof Collection) return (Collection<?>) val;
                    } catch (NoSuchMethodException ignored) {}
                }
                // Rare field names as last resort
                for (String f : new String[] { "mRecipeList", "allRecipes", "recipes" }) {
                    try {
                        final Field rf = backend.getClass()
                            .getDeclaredField(f);
                        rf.setAccessible(true);
                        final Object val = rf.get(backend);
                        if (val instanceof Collection) return (Collection<?>) val;
                    } catch (NoSuchFieldException ignored) {}
                }
            }
        } catch (NoSuchMethodException e) {
            // Some maps expose getAllRecipes() directly
            try {
                final Method getAll = recipeMap.getClass()
                    .getMethod("getAllRecipes");
                final Object val = getAll.invoke(recipeMap);
                if (val instanceof Collection) return (Collection<?>) val;
            } catch (Throwable ignored) {}
        } catch (Throwable ignored) {}

        return null;
    }

    private static int processRecipeCollection(Collection<?> recipes, String mapName) {
        if (recipes == null || recipes.isEmpty()) {
            GTNHBalanceTweaksLogger.info(String.format("PROCESSED (GT++): %s - 0 recipes modified (empty)", mapName));
            return 0;
        }

        int modified = 0;

        // Per-map multipliers; falls back to global defaults if not explicitly configured
        final double timeMult = ConfigHandler.getTimeMultiplier(mapName);
        final double euMult = ConfigHandler.getEuMultiplier(mapName);

        for (Object recipe : recipes) {
            if (recipe == null) continue;

            try {
                // GT_Recipe-compatible fields
                final Field durationField = recipe.getClass()
                    .getDeclaredField("mDuration");
                final Field eutField = recipe.getClass()
                    .getDeclaredField("mEUt");
                durationField.setAccessible(true);
                eutField.setAccessible(true);

                final int duration = durationField.getInt(recipe);
                final int eut = eutField.getInt(recipe);

                // Skip non-duration/value entries (e.g., pure fuel tables, placeholders)
                if (duration <= 0 || eut <= 0) continue;

                final int newDuration = Math.max(1, (int) Math.round(duration * timeMult));
                final int newEut = Math.max(1, (int) Math.round(eut * euMult));

                if (newDuration != duration || newEut != eut) {
                    durationField.setInt(recipe, newDuration);
                    eutField.setInt(recipe, newEut);
                    modified++;
                }

            } catch (NoSuchFieldException nsfe) {
                // Not a standard GT_Recipe-like entry for this map; ignore
            } catch (Throwable t) {
                GTNHBalanceTweaksLogger.warn(
                    "Failed to modify GT++ recipe in " + mapName
                        + " (class="
                        + recipe.getClass()
                            .getName()
                        + ")",
                    t);
            }
        }

        GTNHBalanceTweaksLogger.info(String.format("PROCESSED (GT++): %s - %d recipes modified", mapName, modified));
        return modified;
    }
}
