package com.ninni.spawn.block;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.ninni.spawn.Spawn;
import com.ninni.spawn.SpawnProperties;
import com.ninni.spawn.block.entity.AnthillBlockEntity;
import com.ninni.spawn.entity.Ant;
import com.ninni.spawn.registry.SpawnBlockEntityTypes;
import com.ninni.spawn.registry.SpawnBlocks;
import com.ninni.spawn.registry.SpawnSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AnthillBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty RESOURCE_LEVEL = SpawnProperties.RESOURCE_LEVEL;

    public static final MapCodec<AnthillBlock> CODEC = simpleCodec(AnthillBlock::new);

    public AnthillBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(
                ((this.stateDefinition.any())).setValue(FACING, Direction.NORTH).setValue(RESOURCE_LEVEL, 0));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState,
            @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);

        if (!level.isClientSide && blockEntity instanceof AnthillBlockEntity anthillBlockEntity) {

            Holder<Enchantment> silkTouch = level.registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(Enchantments.SILK_TOUCH);

            if (EnchantmentHelper.getItemEnchantmentLevel(silkTouch, itemStack) == 0) {
                anthillBlockEntity.angerAnts(player, blockState, AnthillBlockEntity.AntState.EMERGENCY);
                level.updateNeighbourForOutputSignal(blockPos, this);
                this.angerNearbyAnts(level, blockPos);
            }
        }
    }

    private void angerNearbyAnts(Level world, BlockPos pos) {
        List<Ant> antList = world.getEntitiesOfClass(Ant.class, new AABB(pos).inflate(8.0, 6.0, 8.0));
        if (!antList.isEmpty()) {
            List<Player> playerList = world.getEntitiesOfClass(Player.class, new AABB(pos).inflate(8.0, 6.0, 8.0));
            for (Ant ant : antList) {
                if (ant.getTarget() != null || ant.isTame())
                    continue;
                ant.setTarget(playerList.get(world.random.nextInt(playerList.size())));
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AnthillBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
            BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null
                : AnthillBlock.createTickerHelper(blockEntityType, SpawnBlockEntityTypes.ANTHILL,
                        AnthillBlockEntity::serverTick);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()
                && player.isCreative()
                && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {

            BlockEntity be = level.getBlockEntity(pos);

            if (be instanceof AnthillBlockEntity anthill && !anthill.hasNoAnts()) {
                ItemStack stack = new ItemStack(this);

                CompoundTag tag = anthill.saveWithoutMetadata(level.registryAccess());

                tag.remove("x");
                tag.remove("y");
                tag.remove("z");
                tag.remove("id");

                stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(tag));

                ItemEntity itemEntity = new ItemEntity(
                        level,
                        pos.getX(),
                        pos.getY(),
                        pos.getZ(),
                        stack);

                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return blockState.getValue(RESOURCE_LEVEL) == 3;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource randomSource) {
        int range = 1;
        List<BlockPos> list = Lists.newArrayList();
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                BlockPos blockPos = new BlockPos(pos.getX() + x, pos.getY() - range, pos.getZ() + z);
                BlockState belowState = world.getBlockState(blockPos);
                if (belowState.is(BlockTags.OVERWORLD_NATURAL_LOGS)
                        || belowState.is(BlockTags.DIRT) && !belowState.is(SpawnBlocks.ANT_MOUND)) {
                    list.add(blockPos);
                }
            }
        }
        if (!list.isEmpty()) {
            BlockPos blockPos = list.get(randomSource.nextInt(list.size()));
            BlockState placeState = null;
            if (world.getBlockState(blockPos).is(BlockTags.OVERWORLD_NATURAL_LOGS)) {
                placeState = SpawnBlocks.ROTTEN_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS,
                        world.getBlockState(blockPos).getValue(RotatedPillarBlock.AXIS));
            } else if (world.getBlockState(blockPos).is(BlockTags.DIRT)
                    && !world.getBlockState(blockPos).is(SpawnBlocks.ANT_MOUND)) {
                placeState = SpawnBlocks.ANT_MOUND.defaultBlockState();
            }
            world.setBlock(blockPos, placeState, 2);
            if (world.getBlockEntity(blockPos) instanceof BrushableBlockEntity brushableBlockEntity) {
                ResourceKey<LootTable> lootTableKey = ResourceKey.create(
                        Registries.LOOT_TABLE,
                        ResourceLocation.fromNamespaceAndPath(Spawn.MOD_ID, "archaeology/anthill"));

                brushableBlockEntity.setLootTable(
                        lootTableKey,
                        blockPos.asLong());
            }
            world.playSound(null, pos, SpawnSoundEvents.ANTHILL_RESOURCE, SoundSource.BLOCKS);
            world.setBlock(pos, state.setValue(RESOURCE_LEVEL, 0), 2);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity;
        Entity entity = builder.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if ((entity instanceof PrimedTnt || entity instanceof Creeper || entity instanceof WitherSkull
                || entity instanceof WitherBoss || entity instanceof MinecartTNT)
                && (blockEntity = builder
                        .getOptionalParameter(LootContextParams.BLOCK_ENTITY)) instanceof AnthillBlockEntity) {
            AnthillBlockEntity blockEntity1 = (AnthillBlockEntity) blockEntity;
            blockEntity1.angerAnts(null, state, AnthillBlockEntity.AntState.EMERGENCY);
        }
        return super.getDrops(state, builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, RESOURCE_LEVEL);
    }
}
