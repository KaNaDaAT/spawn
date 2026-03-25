package com.ninni.spawn.registry;

import com.ninni.spawn.Spawn;
import com.ninni.spawn.advancements.SpawnCriterionTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class SpawnCriteriaTriggers {

        public static final SpawnCriterionTrigger INTERACT_WITH_ANGLER_FISH = Registry.register(
                        BuiltInRegistries.TRIGGER_TYPES,
                        ResourceLocation.fromNamespaceAndPath(Spawn.MOD_ID, "interact_with_angler_fish"),
                        new SpawnCriterionTrigger());

        public static final SpawnCriterionTrigger HATCH_ANT = Registry.register(
                        BuiltInRegistries.TRIGGER_TYPES,
                        ResourceLocation.fromNamespaceAndPath(Spawn.MOD_ID, "hatch_ant"),
                        new SpawnCriterionTrigger());

        public static final SpawnCriterionTrigger OPEN_HAMSTER_INVENTORY = Registry.register(
                        BuiltInRegistries.TRIGGER_TYPES,
                        ResourceLocation.fromNamespaceAndPath(Spawn.MOD_ID, "open_hamster_inventory"),
                        new SpawnCriterionTrigger());

        public static final SpawnCriterionTrigger GOT_STUCK_IN_MUCUS = Registry.register(
                        BuiltInRegistries.TRIGGER_TYPES,
                        ResourceLocation.fromNamespaceAndPath(Spawn.MOD_ID, "got_stuck_in_mucus"),
                        new SpawnCriterionTrigger());

        public static final SpawnCriterionTrigger WENT_THROUGH_GHOSTLY_MUCUS = Registry.register(
                        BuiltInRegistries.TRIGGER_TYPES,
                        ResourceLocation.fromNamespaceAndPath(Spawn.MOD_ID, "went_through_ghostly_mucus"),
                        new SpawnCriterionTrigger());
}