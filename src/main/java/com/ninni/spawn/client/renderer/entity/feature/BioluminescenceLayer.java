package com.ninni.spawn.client.renderer.entity.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ninni.spawn.client.model.AnglerFishModel;
import com.ninni.spawn.entity.AnglerFish;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Predicate;

import static com.ninni.spawn.Spawn.MOD_ID;

@Environment(EnvType.CLIENT)
public class BioluminescenceLayer<T extends AnglerFish> extends EyesLayer<T, AnglerFishModel<T>> {
    private final Predicate<T> predicate;
    private static final RenderType BIOLUMINESCENCE = RenderType.entityTranslucentEmissive(ResourceLocation
            .fromNamespaceAndPath(MOD_ID, "textures/entity/angler_fish/angler_fish_bioluminescence.png"));

    public BioluminescenceLayer(RenderLayerParent<T, AnglerFishModel<T>> renderLayerParent, Predicate<T> predicate) {
        super(renderLayerParent);
        this.predicate = predicate;
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            int light,
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch,
            float partialTick) {
        if (!this.predicate.test(entity))
            return;

        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(this.renderType());

        int color = ((int) (1.0F * 255) << 24) | // alpha
                ((int) (1.0F * 255) << 16) | // red
                ((int) (1.0F * 255) << 8) | // green
                ((int) (1.0F * 255)); // blue

        ((Model) this.getParentModel()).renderToBuffer(
                poseStack,
                vertexConsumer,
                light,
                OverlayTexture.NO_OVERLAY,
                color);
    }

    @Override
    public RenderType renderType() {
        return BIOLUMINESCENCE;
    }
}
