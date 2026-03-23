package com.ninni.spawn;

import com.google.common.reflect.Reflection;
import com.ninni.spawn.entity.*;
import com.ninni.spawn.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

//TODO Advancements
public class Spawn implements ModInitializer {
	public static final String MOD_ID = "spawn";

	// Static initializer to register features BEFORE data pack loading
	@Override
	public void onInitialize() {
		Reflection.initialize(SpawnFeatures.class);

		Reflection.initialize(
				SpawnSoundEvents.class,
				SpawnEntityType.class,
				SpawnBlockEntityTypes.class,
				SpawnCreativeModeTab.class,
				SpawnItems.class,
				SpawnBlocks.class,
				SpawnCriteriaTriggers.class,
				SpawnParticles.class,
				SpawnPointsOfInterests.class,
				SpawnDecoratedPotPatterns.class);

		// Register entity attributes
		FabricDefaultAttributeRegistry.register(SpawnEntityType.ANGLER_FISH, AnglerFish.createAttributes());
		FabricDefaultAttributeRegistry.register(SpawnEntityType.TUNA, Tuna.createAttributes());
		FabricDefaultAttributeRegistry.register(SpawnEntityType.TUNA_EGG, TunaEgg.createAttributes());
		FabricDefaultAttributeRegistry.register(SpawnEntityType.SEAHORSE, Seahorse.createAttributes());
		FabricDefaultAttributeRegistry.register(SpawnEntityType.SNAIL, Snail.createAttributes());
		FabricDefaultAttributeRegistry.register(SpawnEntityType.HAMSTER, Hamster.createAttributes());
		FabricDefaultAttributeRegistry.register(SpawnEntityType.ANT, Ant.createAttributes());

		SpawnVanillaIntegration.serverInit();
	}
}
