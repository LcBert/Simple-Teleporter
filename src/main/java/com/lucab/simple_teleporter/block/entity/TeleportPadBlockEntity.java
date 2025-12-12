package com.lucab.simple_teleporter.block.entity;

import com.lucab.simple_teleporter.SimpleTeleporterConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class TeleportPadBlockEntity extends BlockEntity {
    private GlobalPos destination;
    private int cooldown = 0;

    public TeleportPadBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.TELEPORT_PAD.get(), pos, blockState);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }

        if (cooldown > 0) {
            cooldown--;
        }

        if (destination == null) {
            return;
        }

        // Check for entities on top
        // The block collision height is 6 pixels (0.375 blocks)
        // We check for entities in a box just above the collision shape
        // We shrink the box horizontally to ensure the entity is really on the pad
        double padHeight = 6.0 / 16.0;
        AABB box = new AABB(pos.getX(), pos.getY() + padHeight, pos.getZ(),
                pos.getX() + 1, pos.getY(), pos.getZ() + 1);
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, box);

        for (Entity entity : entities) {
            if (cooldown > 0)
                continue;

            if (entity instanceof ServerPlayer player) {
                if (player.isShiftKeyDown()) {
                    teleportEntity(player);
                    this.cooldown = 10;
                }
            } else {
                // Non-player entities teleport immediately
                teleportEntity(entity);
                this.cooldown = 10;
            }
        }
    }

    private void teleportEntity(Entity entity) {
        if (entity.level().isClientSide)
            return;
        ServerLevel targetLevel = entity.getServer().getLevel(destination.dimension());

        if (targetLevel != null) {
            if (entity instanceof ServerPlayer player) {
                int cost;
                if (player.level().dimension() == destination.dimension()) {
                    cost = SimpleTeleporterConfig.SAME_DIMENSION_COST.get();
                } else {
                    cost = SimpleTeleporterConfig.CROSS_DIMENSION_COST.get();
                }

                if (!player.isCreative() && player.experienceLevel < cost) {
                    player.displayClientMessage(net.minecraft.network.chat.Component
                            .literal("Not enough XP! Need " + cost + " levels.").withColor(0xFF0000), true);
                    player.level().playSound(null, player.blockPosition(), SoundEvents.VILLAGER_NO, SoundSource.BLOCKS,
                            1.0F, 1.0F);
                    return;
                }

                if (!player.isCreative()) {
                    player.giveExperienceLevels(-cost);
                }
            }

            // Play sound at source
            entity.level().playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS,
                    1.0F, 1.0F);

            if (entity instanceof ServerPlayer player) {
                player.teleportTo(targetLevel, destination.pos().getX() + 0.5, destination.pos().getY() + 1,
                        destination.pos().getZ() + 0.5, player.getYRot(), player.getXRot());
            } else {
                entity.teleportTo(targetLevel, destination.pos().getX() + 0.5, destination.pos().getY() + 1,
                        destination.pos().getZ() + 0.5, Set.of(), entity.getYRot(), entity.getXRot());
            }

            // Play sound at destination
            targetLevel.playSound(null, destination.pos(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0F,
                    1.0F);

            // Set cooldown on destination pad if it exists
            BlockEntity destBlockEntity = targetLevel.getBlockEntity(destination.pos());
            if (destBlockEntity instanceof TeleportPadBlockEntity destPad) {
                destPad.setCooldown(10);
            }
        }
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void setDestination(GlobalPos destination) {
        this.destination = destination;
        setChanged();
    }

    public GlobalPos getDestination() {
        return destination;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (destination != null) {
            GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, destination).resultOrPartial()
                    .ifPresent(destTag -> tag.put("destination", destTag));
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("destination")) {
            GlobalPos.CODEC.parse(NbtOps.INSTANCE, tag.get("destination")).resultOrPartial()
                    .ifPresent(dest -> this.destination = dest);
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
