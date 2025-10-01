package com.rilliko.gtnhbalancetweaks.registry;

import com.rilliko.gtnhbalancetweaks.meta.machines.me.MTE_CraftingInputME_Plus;
import com.rilliko.gtnhbalancetweaks.meta.machines.me.MTE_CraftingInputME_Plus_Slave;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class MetaTileRegistry {

    public static final int ID_ME_INPUT_BUFFER_PLUS_FLUID = 2900;
    public static final int ID_ME_INPUT_SLAVE_PLUS = 2902;

    public static MetaTileEntity ME_BUS_PLUS_FLUID;
    public static MetaTileEntity ME_BUS_PLUS_SLAVE;

    private static void register(int id, MetaTileEntity newTile) {
        IMetaTileEntity existing = GregTechAPI.METATILEENTITIES[id];
        if (existing != null) return;
        GregTechAPI.METATILEENTITIES[id] = newTile;
    }

    public static void registerAll() {
        ME_BUS_PLUS_FLUID = new MTE_CraftingInputME_Plus(
            ID_ME_INPUT_BUFFER_PLUS_FLUID,
            "hatch.crafting.input.me.plus.fluids",
            "Pattern Processor Matrix",
            true);
        register(ID_ME_INPUT_BUFFER_PLUS_FLUID, ME_BUS_PLUS_FLUID);

        ME_BUS_PLUS_SLAVE = new MTE_CraftingInputME_Plus_Slave(
            ID_ME_INPUT_SLAVE_PLUS,
            "hatch.crafting.input.me.plus.slave",
            "Pattern Matrix Proxy");
        register(ID_ME_INPUT_SLAVE_PLUS, ME_BUS_PLUS_SLAVE);
    }
}
