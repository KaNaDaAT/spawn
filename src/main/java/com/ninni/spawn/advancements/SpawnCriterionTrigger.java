package com.ninni.spawn.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

import static com.ninni.spawn.Spawn.MOD_ID;

public class SpawnCriterionTrigger extends SimpleCriterionTrigger<SpawnCriterionTrigger.TriggerInstance> {
    private final ResourceLocation ID;

    public SpawnCriterionTrigger(String id) {
        ID = ResourceLocation.fromNamespaceAndPath(MOD_ID, id);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, conditions -> true);
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player)
            ).apply(instance, TriggerInstance::new)
        );
    }
}