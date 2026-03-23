package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.SpawnTags;
import com.ninni.spawn.entity.*;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.entity.animal.WaterAnimal;

public class SpawnEntityType {

        public static final EntityType<AnglerFish> ANGLER_FISH = register(
                        "angler_fish",
                        EntityType.Builder.of(AnglerFish::new, MobCategory.WATER_AMBIENT)
                                        .sized(0.6F, 0.6F)
                                        .clientTrackingRange(10));

        public static final EntityType<Tuna> TUNA = register(
                        "tuna",
                        EntityType.Builder.of(Tuna::new, MobCategory.WATER_CREATURE)
                                        .sized(1.2F, 0.8F));

        public static final EntityType<TunaEgg> TUNA_EGG = register(
                        "tuna_egg",
                        EntityType.Builder.of(TunaEgg::new, MobCategory.MISC)
                                        .sized(0.15f, 0.15f)
                                        .clientTrackingRange(10));

        public static final EntityType<Seahorse> SEAHORSE = register(
                        "seahorse",
                        EntityType.Builder.of(Seahorse::new, MobCategory.WATER_AMBIENT)
                                        .sized(0.3F, 0.6F)
                                        .clientTrackingRange(10));

        public static final EntityType<Snail> SNAIL = register(
                        "snail",
                        EntityType.Builder.of(Snail::new, MobCategory.CREATURE)
                                        .sized(0.8F, 0.8F)
                                        .clientTrackingRange(10));

        public static final EntityType<Hamster> HAMSTER = register(
                        "hamster",
                        EntityType.Builder.of(Hamster::new, MobCategory.CREATURE)
                                        .sized(0.6F, 0.5F)
                                        .clientTrackingRange(10));

        public static final EntityType<Ant> ANT = register(
                        "ant",
                        EntityType.Builder.of(Ant::new, MobCategory.CREATURE)
                                        .sized(0.6F, 0.5F)
                                        .clientTrackingRange(10));

        static {
                // Biome spawns
                BiomeModifications.addSpawn(
                                BiomeSelectors.tag(SpawnTags.ANGLER_FISH_SPAWNS),
                                MobCategory.WATER_AMBIENT,
                                ANGLER_FISH,
                                5, 1, 1);

                BiomeModifications.addSpawn(
                                BiomeSelectors.tag(SpawnTags.TUNA_SPAWNS),
                                MobCategory.WATER_CREATURE,
                                TUNA,
                                15, 1, 1);
                        
                BiomeModifications.addSpawn(
                                BiomeSelectors.tag(SpawnTags.SEAHORSE_SPAWNS),
                                MobCategory.WATER_AMBIENT,
                                SEAHORSE,
                                20, 1, 3);

                BiomeModifications.addSpawn(
                                BiomeSelectors.tag(SpawnTags.SNAIL_SPAWNS),
                                MobCategory.CREATURE,
                                SNAIL,
                                12, 1, 3);

                BiomeModifications.addSpawn(
                                BiomeSelectors.tag(SpawnTags.HAMSTER_SPAWNS),
                                MobCategory.CREATURE,
                                HAMSTER,
                                25, 1, 1);

                BiomeModifications.addSpawn(
                                BiomeSelectors.tag(SpawnTags.HAMSTER_FREQUENTLY_SPAWNS),
                                MobCategory.CREATURE,
                                HAMSTER,
                                100, 1, 4);

                // Spawn restrictions (moved out of builder)
                SpawnPlacements.register(
                                HAMSTER,
                                SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.WORLD_SURFACE,
                                Hamster::canSpawn);

                SpawnPlacements.register(
                                SNAIL,
                                SpawnPlacementTypes.ON_GROUND,
                                Heightmap.Types.WORLD_SURFACE,
                                Snail::canSpawn);

                SpawnPlacements.register(
                                ANGLER_FISH,
                                SpawnPlacementTypes.IN_WATER,
                                Heightmap.Types.OCEAN_FLOOR,
                                (type, level, reason, pos, random) -> level.getFluidState(pos).isSource()
                                                && level.getFluidState(pos.above()).isEmpty());

                SpawnPlacements.register(
                                TUNA,
                                SpawnPlacementTypes.IN_WATER,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                                (type, level, reason, pos, random) -> level.getFluidState(pos).isSource()
                                                && level.getFluidState(pos.above()).isEmpty());

                SpawnPlacements.register(
                                SEAHORSE,
                                SpawnPlacementTypes.IN_WATER,
                                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                                WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        }

        private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
                return Registry.register(
                                BuiltInRegistries.ENTITY_TYPE,
                                ResourceLocation.fromNamespaceAndPath(Spawn.MOD_ID, id),
                                builder.build(id));
        }
}