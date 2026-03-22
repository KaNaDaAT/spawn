package com.ninni.spawn.mixin;

import com.ninni.spawn.registry.SpawnDecoratedPotPatterns;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DecoratedPotPatterns.class)
public abstract class DecoratedPotPatternsMixin {

    @Inject(method = "getPattern", at = @At("HEAD"), cancellable = true)
    private static void S$getPattern(Item item, CallbackInfoReturnable<DecoratedPotPattern> cir) {
        ResourceLocation id = SpawnDecoratedPotPatterns.S$ITEM_TO_POT_TEXTURE.get(item);

        if (id != null) {
            cir.setReturnValue(BuiltInRegistries.DECORATED_POT_PATTERN.get(id));
        }
    }
}
