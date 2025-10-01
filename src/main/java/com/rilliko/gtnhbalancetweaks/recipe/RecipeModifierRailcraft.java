package com.rilliko.gtnhbalancetweaks.recipe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.rilliko.gtnhbalancetweaks.core.GTNHBalanceTweaksLogger;

public final class RecipeModifierRailcraft {

    private RecipeModifierRailcraft() {}

    private static final IdentityHashMap<Object, Boolean> SEEN_COLLECTIONS = new IdentityHashMap<>();

    public static void modifyRailcraftCokeOven() {
        GTNHBalanceTweaksLogger.info("=== Railcraft Coke Oven Modification Pass (hard 50% time) ===");
        try {
            // Classes
            Class<?> mgrCls = Class.forName("mods.railcraft.common.util.crafting.CokeOvenCraftingManager");
            Class<?> apiMgrCls = Class.forName("mods.railcraft.api.crafting.ICokeOvenCraftingManager");

            Class<?> innerRecipeCls = null;
            for (Class<?> c : mgrCls.getDeclaredClasses()) {
                if (c.getSimpleName()
                    .equals("CokeOvenRecipe")) {
                    innerRecipeCls = c;
                    break;
                }
            }
            if (innerRecipeCls == null) {
                GTNHBalanceTweaksLogger.info("Railcraft: inner CokeOvenRecipe not found; skipping.");
                return;
            }

            // CokeOvenCraftingManager.getInstance()
            Method getInstance = mgrCls.getDeclaredMethod("getInstance");
            Object mgr = getInstance.invoke(null);

            // getRecipes()
            Method getRecipes = apiMgrCls.getDeclaredMethod("getRecipes");
            @SuppressWarnings("unchecked")
            List<Object> recipes = (List<Object>) getRecipes.invoke(mgr);
            if (recipes == null || recipes.isEmpty()) {
                GTNHBalanceTweaksLogger.info("Railcraft: Coke Oven recipes empty; skipping.");
                return;
            }
            if (SEEN_COLLECTIONS.putIfAbsent(recipes, Boolean.TRUE) != null) {
                GTNHBalanceTweaksLogger.info("Railcraft: recipes list already visited; skipping.");
                return;
            }

            // Reflect fields
            Field fInput = innerRecipeCls.getDeclaredField("input");
            Field fMatchDamage = innerRecipeCls.getDeclaredField("matchDamage");
            Field fMatchNBT = innerRecipeCls.getDeclaredField("matchNBT");
            Field fOutput = innerRecipeCls.getDeclaredField("output");
            Field fFluid = innerRecipeCls.getDeclaredField("fluidOutput");
            Field fCook = innerRecipeCls.getDeclaredField("cookTime");
            for (Field f : new Field[] { fInput, fMatchDamage, fMatchNBT, fOutput, fFluid, fCook }) {
                f.setAccessible(true);
            }

            Constructor<?> ctor = innerRecipeCls.getDeclaredConstructor(
                ItemStack.class,
                boolean.class,
                boolean.class,
                ItemStack.class,
                FluidStack.class,
                int.class);
            ctor.setAccessible(true);

            int changed = 0, skipped = 0;

            for (int i = 0; i < recipes.size(); i++) {
                Object r = recipes.get(i);
                if (r == null) {
                    skipped++;
                    continue;
                }

                int cookTime = (Integer) fCook.get(r);
                if (cookTime <= 1) {
                    skipped++;
                    continue;
                }

                int newCook = Math.max(1, cookTime / 2);
                if (newCook == cookTime) {
                    skipped++;
                    continue;
                }

                ItemStack input = (ItemStack) fInput.get(r);
                boolean matchDamage = (Boolean) fMatchDamage.get(r);
                boolean matchNBT = (Boolean) fMatchNBT.get(r);
                ItemStack output = (ItemStack) fOutput.get(r);
                FluidStack fluidOut = (FluidStack) fFluid.get(r);

                Object newRecipe = ctor.newInstance(
                    copyOrNull(input),
                    matchDamage,
                    matchNBT,
                    copyOrNull(output),
                    copyOrNull(fluidOut),
                    newCook);

                recipes.set(i, newRecipe);
                changed++;
            }

            GTNHBalanceTweaksLogger.info(
                String.format(
                    "PROCESSED (Railcraft): cokeOvenRecipes - %d recipes halved, %d skipped",
                    changed,
                    skipped));
        } catch (Throwable t) {
            GTNHBalanceTweaksLogger.info(
                "Railcraft pass failed: " + t.getClass()
                    .getSimpleName() + ": " + t.getMessage());
        } finally {
            GTNHBalanceTweaksLogger.info("=== Railcraft Coke Oven Modification Pass Finished ===");
        }
    }

    private static ItemStack copyOrNull(ItemStack s) {
        return s == null ? null : s.copy();
    }

    private static FluidStack copyOrNull(FluidStack f) {
        return f == null ? null : f.copy();
    }
}
