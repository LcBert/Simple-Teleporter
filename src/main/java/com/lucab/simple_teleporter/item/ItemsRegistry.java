package com.lucab.simple_teleporter.item;

import com.lucab.simple_teleporter.SimpleTeleporter;
import com.lucab.simple_teleporter.block.BlocksRegistry;
import com.lucab.simple_teleporter.item.custom.BindingTool;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemsRegistry {
    public static final DeferredRegister.Items ITEMS_REGISTRY = DeferredRegister.createItems(SimpleTeleporter.MODID);

    public static final DeferredItem<Item> BINDING_TOOL = ITEMS_REGISTRY
            .register("binding_tool", BindingTool::new);

    public static final DeferredItem<Item> TELEPORT_PAD = ITEMS_REGISTRY.register("teleport_pad",
            () -> new BlockItem(BlocksRegistry.TELEPORT_PAD.get(), new Item.Properties()));
}
