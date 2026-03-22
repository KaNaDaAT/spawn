package com.ninni.spawn.block;

import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class StrippablePlankBlock extends Block {
    public BlockState strippedBlockState;

    public StrippablePlankBlock(BlockState strippedBlockState, Properties properties) {
        super(properties);
        this.strippedBlockState = strippedBlockState;
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack itemStack,
            BlockState blockState,
            Level level,
            BlockPos blockPos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit) {
        if (itemStack.getItem() instanceof AxeItem) {
            level.setBlock(blockPos, strippedBlockState, 4);

            level.playSound(
                    player,
                    blockPos,
                    SpawnSoundEvents.ROTTEN_WOOD_CRACK,
                    SoundSource.BLOCKS,
                    1.0f,
                    1.0f);
            EquipmentSlot slot = hand == InteractionHand.MAIN_HAND
                    ? EquipmentSlot.MAINHAND
                    : EquipmentSlot.OFFHAND;

            itemStack.hurtAndBreak(1, player, slot);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
