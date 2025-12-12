package com.lucab.simple_teleporter;

import net.neoforged.neoforge.common.ModConfigSpec;

public class SimpleTeleporterConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.IntValue SAME_DIMENSION_COST;
    public static final ModConfigSpec.IntValue CROSS_DIMENSION_COST;

    static {
        BUILDER.push("Simple Teleporter Config");

        SAME_DIMENSION_COST = BUILDER.comment("Experience levels required to teleport within the same dimension")
                .defineInRange("sameDimensionCost", 1, 0, 100);

        CROSS_DIMENSION_COST = BUILDER.comment("Experience levels required to teleport across dimensions")
                .defineInRange("crossDimensionCost", 3, 0, 100);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
