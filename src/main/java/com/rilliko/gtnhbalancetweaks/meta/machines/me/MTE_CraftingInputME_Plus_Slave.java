package com.rilliko.gtnhbalancetweaks.meta.machines.me;

import com.rilliko.gtnhbalancetweaks.util.TextUtil;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputSlave;

public class MTE_CraftingInputME_Plus_Slave extends MTEHatchCraftingInputSlave {

    public MTE_CraftingInputME_Plus_Slave(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTE_CraftingInputME_Plus_Slave(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTE_CraftingInputME_Plus_Slave(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public String[] getDescription() {
        return TextUtil.patchDescriptionWithTier(
            super.getDescription(),
            4,
            TextUtil.authorCreditLine("Rilliko", "GTNH Balance Tweaks"));
    }
}
