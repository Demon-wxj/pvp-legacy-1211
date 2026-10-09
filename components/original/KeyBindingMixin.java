package local.legacypvp.mixin;

import local.legacypvp.LegacyPvp;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin {
    @Inject(method="isPressed",at=@At("RETURN"),cancellable=true)
    private void legacy$sprintIntent(CallbackInfoReturnable<Boolean> cir) {
        if(LegacyPvp.config!=null && LegacyPvp.config.toggleSprint && (Object)this==MinecraftClient.getInstance().options.sprintKey && LegacyPvp.playing())
            cir.setReturnValue(true);
    }
}
