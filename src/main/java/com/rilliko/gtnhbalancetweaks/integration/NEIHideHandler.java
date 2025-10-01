package com.rilliko.gtnhbalancetweaks.integration;

import net.minecraft.item.ItemStack;

import com.rilliko.gtnhbalancetweaks.core.GTNHBalanceTweaksLogger;

import codechicken.nei.api.API;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;

public final class NEIHideHandler {

    private NEIHideHandler() {}

    public static void hideOldMEHatches() {
        try {
            hideById(2714, "ME Crafting Input Buffer (UIV)");
            hideById(2716, "ME Crafting Proxy Hatch (UIV)");
            hideById(2715, "ME Crafting Input Bus (LUV)");
        } catch (Throwable t) {
            GTNHBalanceTweaksLogger.error("[BTX] Exception while hiding old ME hatches from NEI", t);
        }
    }

    private static void hideById(int id, String name) {
        IMetaTileEntity mte = GregTechAPI.METATILEENTITIES[id];
        if (mte != null) {
            ItemStack stack = mte.getStackForm(1);
            if (stack != null) {
                API.hideItem(stack);
                GTNHBalanceTweaksLogger.info("[BTX] Hid " + name + " from NEI");
            } else {
                GTNHBalanceTweaksLogger.warn("[BTX] Could not get ItemStack for " + name);
            }
        } else {
            GTNHBalanceTweaksLogger.warn("[BTX] No MetaTileEntity registered at ID " + id + " (" + name + ")");
        }
    }
}
