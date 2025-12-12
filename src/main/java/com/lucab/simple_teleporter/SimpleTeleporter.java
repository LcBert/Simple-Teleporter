package com.lucab.simple_teleporter;

import org.slf4j.Logger;

import com.lucab.simple_teleporter.block.BlocksRegistry;
import com.lucab.simple_teleporter.block.entity.BlockEntityRegistry;
import com.lucab.simple_teleporter.component.DataComponentRegistry;
import com.lucab.simple_teleporter.creative_tab.SimpleTeleporterTab;
import com.lucab.simple_teleporter.item.ItemsRegistry;
import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(SimpleTeleporter.MODID)
public class SimpleTeleporter {
    public static final String MODID = "simple_teleporter";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SimpleTeleporter(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);

        ItemsRegistry.ITEMS_REGISTRY.register(modEventBus);
        BlocksRegistry.BLOCKS_REGISTRY.register(modEventBus);
        BlockEntityRegistry.BLOCK_ENTITY_TYPES.register(modEventBus);
        DataComponentRegistry.DATA_COMPONENT_TYPES.register(modEventBus);

        SimpleTeleporterTab.CREATIVE_TAB.register(modEventBus);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
