/* SPDX-License-Identifier: MPL-2.0
 * Modern blocking base pose adapted from Fuzs / SwordBlockingMechanics 21.1.1.
 * https://github.com/Fuzss/swordblockingmechanics
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package local.legacypvp.mixin;

import local.legacypvp.LegacyPvp;
import local.legacypvp.BlockPose;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Fallback for sword-use implementations without SwordBlockingMechanics. */
@Mixin(value=HeldItemRenderer.class,priority=1100)
public abstract class HeldItemMixin {
    private static final boolean legacy$hasSbm=FabricLoader.getInstance().isModLoaded("swordblockingmechanics");
    @Shadow public abstract void renderItem(LivingEntity entity,ItemStack stack,ModelTransformationMode mode,boolean left,MatrixStack matrices,VertexConsumerProvider consumers,int light);
    @Shadow protected abstract void applyEquipOffset(MatrixStack matrices,Arm arm,float equip);
    @Shadow protected abstract void applySwingOffset(MatrixStack matrices,Arm arm,float swing);

    @Inject(method="renderFirstPersonItem",at=@At("HEAD"),cancellable=true)
    private void legacy$blockHit(AbstractClientPlayerEntity player,float delta,float pitch,Hand hand,float swing,ItemStack stack,float equip,MatrixStack m,VertexConsumerProvider consumers,int light,CallbackInfo ci) {
        if(legacy$hasSbm || LegacyPvp.config==null || !LegacyPvp.config.blockHit || !(stack.getItem() instanceof SwordItem)
            || !player.isUsingItem() || player.getActiveHand()!=hand || player.getItemUseTimeLeft()<=0)return;
        Arm arm=hand==Hand.MAIN_HAND?player.getMainArm():player.getMainArm().getOpposite();
        int direction=arm==Arm.RIGHT?1:-1;
        m.push();
        try {
            applyEquipOffset(m,arm,equip);applySwingOffset(m,arm,swing);
            // Modern baked-model equivalent of the legacy block base pose, as used by SBM.
            m.translate(direction*-.14142136f,.08f,.14142136f);
            m.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-102.25f));
            m.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(direction*13.365f));
            m.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(direction*78.05f));
            // OldAnimations 2.3.1 block-hit size and local offset.
            BlockPose.apply(m,arm);
            renderItem(player,stack,arm==Arm.RIGHT?ModelTransformationMode.FIRST_PERSON_RIGHT_HAND:ModelTransformationMode.FIRST_PERSON_LEFT_HAND,arm==Arm.LEFT,m,consumers,light);
        } finally {m.pop();}
        ci.cancel();
    }
}
