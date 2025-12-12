package com.lucab.simple_teleporter.event;

import com.lucab.simple_teleporter.SimpleTeleporter;
import com.lucab.simple_teleporter.component.DataComponentRegistry;
import com.lucab.simple_teleporter.item.ItemsRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = SimpleTeleporter.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) {
            return;
        }

        ItemStack heldItem = player.getMainHandItem();
        if (!isBindingItem(heldItem)) {
            heldItem = player.getOffhandItem();
            if (!isBindingItem(heldItem)) {
                return;
            }
        }

        if (heldItem.has(DataComponentRegistry.BIND_POS)) {
            GlobalPos globalPos = heldItem.get(DataComponentRegistry.BIND_POS);
            if (globalPos != null && globalPos.dimension() == player.level().dimension()) {
                renderHighlight(event.getPoseStack(), globalPos.pos(), event.getCamera().getPosition());
            }
        }
    }

    private static boolean isBindingItem(ItemStack stack) {
        return stack.getItem() == ItemsRegistry.BINDING_TOOL.get()
                || stack.getItem() == ItemsRegistry.BINDING_SHARD.get();
    }

    private static void renderHighlight(PoseStack poseStack, BlockPos pos, Vec3 cameraPos) {
        VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource()
                .getBuffer(RenderType.lines());

        poseStack.pushPose();
        poseStack.translate(pos.getX() - cameraPos.x, pos.getY() - cameraPos.y + 1, pos.getZ() - cameraPos.z);

        LevelRenderer.renderLineBox(poseStack, vertexConsumer, 0, 0, 0, 1, 1, 1, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();

        Minecraft.getInstance().renderBuffers().bufferSource().endBatch(RenderType.lines());
    }
}
