/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaSpreader;

import java.util.List;

public class LensTripwire extends Lens {

	@Override
	public boolean allowBurstShooting(ItemStack stack, IManaSpreader spreader, boolean redstone) {
		IManaBurst burst = spreader.runBurstSimulation();
		return burst.hasTripped();
	}

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		Entity entity = burst.entity();
		if (burst.isFake()) {
			if (entity.world.isRemote) {
				return;
			}

			AxisAlignedBB axis = new AxisAlignedBB(entity.getPosX(), entity.getPosY(), entity.getPosZ(), entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).grow(0.25);
			List<LivingEntity> entities = entity.world.getEntitiesWithinAABB(LivingEntity.class, axis);
			if (!entities.isEmpty()) {
				burst.setTripped(true);
			}
		}

	}

}
