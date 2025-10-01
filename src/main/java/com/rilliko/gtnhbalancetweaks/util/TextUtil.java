package com.rilliko.gtnhbalancetweaks.util;

import static gregtech.api.enums.GTValues.TIER_COLORS;
import static gregtech.api.enums.GTValues.VN;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;

public final class TextUtil {

    private TextUtil() {
        // Utility class; no instantiation
    }

    /**
     * Credit line format:
     * (Aqua) Modified by:
     * (Bold Light Green) <project>
     * (Aqua) from
     * (Bold Dark Green) <name>
     */
    public static String authorCreditLine(String name, String project) {
        return EnumChatFormatting.AQUA + "Modified by: "
            + EnumChatFormatting.GREEN
            + EnumChatFormatting.BOLD
            + project
            + EnumChatFormatting.AQUA
            + " from "
            + EnumChatFormatting.DARK_GREEN
            + EnumChatFormatting.BOLD
            + name
            + EnumChatFormatting.RESET;
    }

    /**
     * Replaces or appends the "Hatch Tier" line in a description array,
     * ensuring the correct voltage tier is displayed.
     *
     * @param base       The original description (from super.getDescription()).
     * @param tierIndex  The GTValues index for the tier (e.g. 5 = IV).
     * @param creditLine Optional credit line to append (null = none).
     * @return A new String[] with tier line replaced and credit added.
     */
    public static String[] patchDescriptionWithTier(String[] base, int tierIndex, String creditLine) {
        List<String> out = new ArrayList<>(base.length + 2);

        String tierLine = "Hatch Tier: " + TIER_COLORS[tierIndex] + VN[tierIndex];
        boolean replaced = false;

        for (String line : base) {
            if (!replaced && line != null && line.startsWith("Hatch Tier: ")) {
                out.add(tierLine);
                replaced = true;
            } else {
                out.add(line);
            }
        }

        if (!replaced) {
            out.add(tierLine);
        }

        if (creditLine != null) {
            out.add(creditLine);
        }

        return out.toArray(new String[0]);
    }
}
