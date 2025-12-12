package com.lucab.simple_teleporter.block.entity;

import com.lucab.simple_teleporter.SimpleTeleporter;
import com.lucab.simple_teleporter.block.BlocksRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister
            .create(Registries.BLOCK_ENTITY_TYPE, SimpleTeleporter.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TeleportPadBlockEntity>> TELEPORT_PAD = BLOCK_ENTITY_TYPES
            .register("teleport_pad",
                    () -> BlockEntityType.Builder.of(TeleportPadBlockEntity::new, BlocksRegistry.TELEPORT_PAD.get())
                            .build(null));
}
