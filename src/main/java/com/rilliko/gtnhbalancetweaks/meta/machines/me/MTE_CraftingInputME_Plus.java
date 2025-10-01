package com.rilliko.gtnhbalancetweaks.meta.machines.me;

import com.rilliko.gtnhbalancetweaks.util.TextUtil;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;

public class MTE_CraftingInputME_Plus extends MTEHatchCraftingInputME {

    public MTE_CraftingInputME_Plus(int aID, String aName, String aNameRegional, boolean supportFluids) {
        super(aID, aName, aNameRegional, supportFluids);
    }

    public MTE_CraftingInputME_Plus(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures,
        boolean supportFluids) {
        super(aName, aTier, aDescription, aTextures, supportFluids);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTE_CraftingInputME_Plus(
            this.mName,
            this.mTier,
            this.mDescriptionArray,
            this.mTextures,
            this.supportsFluids());
    }

    @Override
    public String[] getDescription() {
        return TextUtil.patchDescriptionWithTier(
            super.getDescription(),
            4,
            TextUtil.authorCreditLine("Rilliko", "GTNH Balance Tweaks"));
    }
}
