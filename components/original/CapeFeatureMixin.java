package local.legacypvp.mixin;

import local.legacypvp.SaoCape;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.util.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Leave skin and elytra consumers untouched; vanilla owns cape movement/visibility. */
@Mixin(CapeFeatureRenderer.class)
public abstract class CapeFeatureMixin {
    @Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getSkinTextures()Lnet/minecraft/client/util/SkinTextures;"))
    private SkinTextures legacyPvp$saoCape(AbstractClientPlayerEntity player) {
        return SaoCape.textures(player);
    }
}
