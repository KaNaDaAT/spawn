package com.ninni.spawn.block;

import com.mojang.serialization.MapCodec;
import com.ninni.spawn.SpawnProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FallenLeavesBlock extends BushBlock {
    public static final IntegerProperty AMOUNT = SpawnProperties.LEAVES;
    private static final VoxelShape SHAPE = box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public static final MapCodec<FallenLeavesBlock> CODEC = simpleCodec(FallenLeavesBlock::new);

    public FallenLeavesBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(((this.stateDefinition.any())).setValue(AMOUNT, 1));
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        return (!blockPlaceContext.isSecondaryUseActive() && blockPlaceContext.getItemInHand().is(this.asItem())
                && blockState.getValue(AMOUNT) < 4) || super.canBeReplaced(blockState, blockPlaceContext);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos,
            CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return !blockState.getCollisionShape(blockGetter, blockPos).getFaceShape(Direction.UP).isEmpty()
                || blockState.isFaceSturdy(blockGetter, blockPos, Direction.UP);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockPos pos = blockPlaceContext.getClickedPos();
        BlockState state = blockPlaceContext.getLevel().getBlockState(pos);
        if (state.is(this)) {
            return state.setValue(AMOUNT, Math.min(4, state.getValue(AMOUNT) + 1));
        }
        return super.getStateForPlacement(blockPlaceContext);
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return (type == PathComputationType.AIR && !this.hasCollision)
                || super.isPathfindable(state, type);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AMOUNT);
    }
}
