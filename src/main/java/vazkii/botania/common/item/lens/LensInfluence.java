/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.item.ModItems;

import java.util.List;

public class LensInfluence extends Lens {

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		Entity entity = burst.entity();
		if (!burst.isFake()) {
			double range = 3.5;
			AxisAlignedBB bounds = new AxisAlignedBB(entity.getPosX() - range, entity.getPosY() - range, entity.getPosZ() - range, entity.getPosX() + range, entity.getPosY() + range, entity.getPosZ() + range);
			List<Entity> movables = entity.world.getEntitiesWithinAABB(ItemEntity.class, bounds);
			movables.addAll(entity.world.getEntitiesWithinAABB(ExperienceOrbEntity.class, bounds));
			movables.addAll(entity.world.getEntitiesWithinAABB(AbstractArrowEntity.class, bounds));
			movables.addAll(entity.world.getEntitiesWithinAABB(FallingBlockEntity.class, bounds));
			movables.addAll(entity.world.getEntitiesWithinAABB(ThrowableEntity.class, bounds, Predicates.instanceOf(IManaBurst.class)));

			for (Entity movable : movables) {
				if (movable == burst) {
					continue;
				}

				if (movable instanceof IManaBurst) {
					IManaBurst otherBurst = (IManaBurst) movable;
					ItemStack lens = otherBurst.getSourceLens();
					if (!lens.isEmpty() && lens.getItem() == ModItems.lensInfluence) {
						continue;
					}

					((IManaBurst) movable).setBurstMotion(entity.getMotion().getX(),
							entity.getMotion().getY(), entity.getMotion().getZ());
				} else {
					movable.setMotion(entity.getMotion());
				}
			}
		}
	}

}
