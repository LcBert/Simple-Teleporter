package com.lucab.simple_teleporter.component;

import com.lucab.simple_teleporter.SimpleTeleporter;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DataComponentRegistry {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister
            .create(Registries.DATA_COMPONENT_TYPE, SimpleTeleporter.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GlobalPos>> BIND_POS = DATA_COMPONENT_TYPES
            .register("bind_pos", () -> DataComponentType.<GlobalPos>builder().persistent(GlobalPos.CODEC).build());
}
