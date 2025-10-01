package com.rilliko.gtnhbalancetweaks.registry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;

public final class RecipeHandler {

    private RecipeHandler() {}

    public static void registerAll() {
        registerMEPlusRecipes();
    }

    private static void registerMEPlusRecipes() {
        FluidStack evSuperconductor = Materials.Uraniumtriplatinid.getMolten(144);
        if (evSuperconductor == null) return;

        ItemStack evCircuits = GTOreDictUnificator.get("circuitMaster", 4);
        if (evCircuits == null) return;

        int duration = 400;
        int eut = (int) GTValues.V[4];

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Hull_EV.get(1), evCircuits, ItemList.Emitter_EV.get(2), ItemList.Sensor_EV.get(2))
            .fluidInputs(evSuperconductor)
            .itemOutputs(MetaTileRegistry.ME_BUS_PLUS_FLUID.getStackForm(1))
            .duration(duration)
            .eut(eut)
            .addTo(RecipeMaps.assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_EV.get(1),
                ItemList.Field_Generator_EV.get(1),
                ItemList.Sensor_EV.get(1),
                ItemList.Emitter_EV.get(1))
            .fluidInputs(evSuperconductor)
            .itemOutputs(MetaTileRegistry.ME_BUS_PLUS_SLAVE.getStackForm(1))
            .duration(duration)
            .eut(eut)
            .addTo(RecipeMaps.assemblerRecipes);
    }
}
