package local.legacypvp;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;

/** Cosmetic selection, consulted exclusively by the vanilla cape feature. */
public final class SaoCape {
    public static final Identifier TEXTURE = Identifier.of("legacy_pvp_assist", "textures/cape/sao_enderman.png");
    private SaoCape() {}

    public static SkinTextures textures(AbstractClientPlayerEntity player) {
        SkinTextures original = player.getSkinTextures();
        if (LegacyPvp.config == null) return original;
        boolean enabled = player == MinecraftClient.getInstance().player
                ? LegacyPvp.config.saoCape : CapeSyncClient.enabled(player.getUuid());
        if (!enabled) return original;
        return new SkinTextures(original.texture(), original.textureUrl(), TEXTURE,
                original.elytraTexture(), original.model(), original.secure());
    }
}
