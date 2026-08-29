package com.rasmus.clearsight.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.rasmus.clearsight.config.ClearSightConfig;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * A raised shield covers a third of the screen. The wrap fires for every
 * first-person item render but only acts on a shield the player is
 * actively blocking with, identified from the stack itself, so idle
 * shields and every other item render vanilla. The translate lives inside
 * the branch's own push/pop, and a wrap chains with other mods on the
 * same call instead of colliding.
 */
@Mixin(ItemInHandRenderer.class)
public class ShieldLegacyMixin {

    @WrapOperation(method = "renderArmWithItem", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V"))
    private void lowerRaisedShield(ItemInHandRenderer renderer, LivingEntity mob, ItemStack stack,
            ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector collector,
            int lightCoords, Operation<Void> original) {
        int height = ClearSightConfig.get().shieldHeight;
        if (height < 100 && stack.getItem() instanceof ShieldItem
                && mob.isUsingItem() && mob.getUseItem() == stack) {
            if (height <= 0) {
                return;
            }
            poseStack.translate(0.0F, -0.5F * (100 - height) / 100.0F, 0.0F);
        }
        original.call(renderer, mob, stack, type, poseStack, collector, lightCoords);
    }
}
