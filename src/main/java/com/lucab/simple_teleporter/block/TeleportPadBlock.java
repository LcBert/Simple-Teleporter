package com.lucab.simple_teleporter.block;

import com.lucab.simple_teleporter.block.entity.BlockEntityRegistry;
import com.lucab.simple_teleporter.block.entity.TeleportPadBlockEntity;
import com.lucab.simple_teleporter.component.DataComponentRegistry;
import com.lucab.simple_teleporter.item.ItemsRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class TeleportPadBlock extends BaseEntityBlock {
    public static final MapCodec<TeleportPadBlock> CODEC = simpleCodec(TeleportPadBlock::new);
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 0, 0, 16, 4, 16));

    public TeleportPadBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TeleportPadBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) {
            return null;
        }
        return createTickerHelper(blockEntityType, BlockEntityRegistry.TELEPORT_PAD.get(),
                (pLevel, pPos, pState, pBlockEntity) -> pBlockEntity.tick(pLevel, pPos, pState));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        for (int i = 0; i < 3; ++i) {
            double d0 = (double) pos.getX() + random.nextDouble();
            double d1 = (double) pos.getY() + random.nextDouble();
            double d2 = (double) pos.getZ() + random.nextDouble();
            level.addParticle(ParticleTypes.PORTAL, d0, d1, d2, 0.0D, 1.0D, 0.0D);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide)
            return ItemInteractionResult.SUCCESS;

        if (stack.getItem() == ItemsRegistry.BINDING_TOOL.get()
                || stack.getItem() == ItemsRegistry.BINDING_SHARD.get()) {
            if (player.isShiftKeyDown()) {
                return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
            }
            if (stack.has(DataComponentRegistry.BIND_POS)) {
                GlobalPos globalPos = stack.get(DataComponentRegistry.BIND_POS);
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof TeleportPadBlockEntity teleportPadBlockEntity) {
                    teleportPadBlockEntity.setDestination(globalPos);

                    level.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);

                    player.displayClientMessage(Component.literal("Destination set to: "
                            + globalPos.pos().toShortString() + " in " + globalPos.dimension().location()), true);

                    if (stack.getItem() == ItemsRegistry.BINDING_SHARD.get()) {
                        stack.shrink(1);
                    }

                    return ItemInteractionResult.SUCCESS;
                }
            } else {
                player.displayClientMessage(
                        Component.literal("Item has no position saved!").withColor(0xFF0000), true);
                return ItemInteractionResult.FAIL;
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
