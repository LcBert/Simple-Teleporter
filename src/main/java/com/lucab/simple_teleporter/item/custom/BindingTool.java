package com.lucab.simple_teleporter.item.custom;

import com.lucab.simple_teleporter.component.DataComponentRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;

public class BindingTool extends Item {

    public BindingTool() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() == null || !context.getPlayer().isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        if (context.getLevel().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockPos position = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        GlobalPos globalPos = GlobalPos.of(context.getLevel().dimension(), position);
        stack.set(DataComponentRegistry.BIND_POS, globalPos);

        context.getPlayer().displayClientMessage(
                Component.literal("Position saved: " + position.toShortString()).withColor(0x00FF00),
                true);

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
            TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("Shift + Right Click on a block to bind position").withColor(0xAAAAAA));
        tooltipComponents
                .add(Component.literal("Right Click a teleporter to set teleport position").withColor(0xAAAAAA));
        if (Screen.hasShiftDown()) {
            GlobalPos globalPos = stack.get(DataComponentRegistry.BIND_POS);
            if (globalPos != null) {
                tooltipComponents
                        .add(Component.literal("  Bound to: " + globalPos.pos().toShortString())
                                .withColor(0x00FF00));
                tooltipComponents
                        .add(Component.literal("  Dimension: " + globalPos.dimension().location())
                                .withColor(0x00FF00));
            } else {
                tooltipComponents.add(Component.literal("    No position saved").withColor(0xFF0000));
            }
        } else {
            tooltipComponents.add(Component.literal("Hold Shift to see saved position").withColor(0x777777));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
