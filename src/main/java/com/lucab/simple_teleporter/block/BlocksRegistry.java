package com.lucab.simple_teleporter.block;

import com.lucab.simple_teleporter.SimpleTeleporter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlocksRegistry {
    public static final DeferredRegister.Blocks BLOCKS_REGISTRY = DeferredRegister.createBlocks(SimpleTeleporter.MODID);

    public static final DeferredBlock<Block> TELEPORT_PAD = BLOCKS_REGISTRY.register("teleport_pad",
            () -> new TeleportPadBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0f)));
}
