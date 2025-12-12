package com.lucab.simple_teleporter.creative_tab;

import com.lucab.simple_teleporter.SimpleTeleporter;
import com.lucab.simple_teleporter.item.ItemsRegistry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SimpleTeleporterTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, SimpleTeleporter.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SIMPLE_TELEPORTER_TAB = CREATIVE_TAB
            .register(SimpleTeleporter.MODID, () -> CreativeModeTab.builder()
                    .title(Component.literal("Simple Teleporter"))
                    .icon(() -> ItemsRegistry.BINDING_TOOL.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ItemsRegistry.BINDING_TOOL.get());
                        output.accept(ItemsRegistry.BINDING_SHARD.get());
                        output.accept(ItemsRegistry.TELEPORT_PAD.get());
                    }).build());
}
