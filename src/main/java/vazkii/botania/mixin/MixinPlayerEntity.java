/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import vazkii.botania.common.core.ModStats;
import vazkii.botania.common.core.PlayerEntityAccess;
import vazkii.botania.common.core.handler.PixieHandler;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity implements PlayerEntityAccess {
	@Shadow
	public abstract void addStat(ResourceLocation id, int amount);

	@Unique
	private LivingEntity critTarget;

	/**
	 * Registers the pixie spawn chance attribute on players
	 */
	@Inject(at = @At("RETURN"), method = "func_234570_el_")
	private static void addPixieAttribute(CallbackInfoReturnable<AttributeModifierMap.MutableAttribute> cir) {
		cir.getReturnValue().createMutableAttribute(PixieHandler.PIXIE_SPAWN_CHANCE);
	}

	/**
	 * Updates the distance by luminizer stat
	 */
	@Inject(
		at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/PlayerEntity;getRidingEntity()Lnet/minecraft/entity/Entity;"),
		method = "addMountedMovementStat", locals = LocalCapture.CAPTURE_FAILSOFT
	)
	private void trackLuminizerTravel(double dx, double dy, double dz, CallbackInfo ci, int cm, Entity mount) {
		if (mount.getType() == ModEntities.PLAYER_MOVER) {
			addStat(ModStats.LUMINIZER_ONE_CM, cm);
		}
	}

	/** Captures the entity attacked by the player, called from {@link ItemTerrasteelHelm#onCritEvent} */
	@Override
	public void botania$setCritTarget(LivingEntity entity) {
		critTarget = entity;
	}

	// Clear the entity on any return after the capture.
	@Inject(
		at = @At(value = "RETURN"), method = "attackTargetEntityWithCurrentItem",
		slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getKnockbackModifier(Lnet/minecraft/entity/LivingEntity;)I"))
	)
	private void clearTarget(CallbackInfo ci) {
		this.critTarget = null;
	}

	// Perform damage source modifications and apply the potion effects.
	@ModifyArg(
		method = "attackTargetEntityWithCurrentItem",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z")
	)
	private DamageSource onDamageTarget(DamageSource source, float amount) {
		if (this.critTarget != null) {
			((ItemTerrasteelHelm) ModItems.terrasteelHelm).onEntityAttacked(source, amount, (PlayerEntity) (Object) this, critTarget);
		}
		return source;
	}
}
