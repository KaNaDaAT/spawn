package com.ninni.spawn.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

import static com.ninni.spawn.Spawn.MOD_ID;

public class SpawnDecoratedPotPatterns {

    public static final Map<Item, ResourceLocation> S$ITEM_TO_POT_TEXTURE = new HashMap<>();

    static {
        S$ITEM_TO_POT_TEXTURE.put(SpawnItems.CROWN_POTTERY_SHERD, crown());
        S$ITEM_TO_POT_TEXTURE.put(SpawnItems.SPADE_POTTERY_SHERD, spade());
    }

    // 👇 Your original string keys preserved
    public static ResourceLocation crown() {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "crown_pottery_pattern");
    }

    public static ResourceLocation spade() {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "spade_pottery_pattern");
    }
}