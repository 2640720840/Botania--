/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class ItemThunderSword extends ItemManasteelSword {

	private static final String TAG_LIGHTNING_SEED = "lightningSeed";

	public ItemThunderSword(Properties props) {
		super(BotaniaAPI.instance().getTerrasteelItemTier(), 3, -1.5F, props);
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity entity, @Nonnull LivingEntity attacker) {
		if (!(entity instanceof PlayerEntity) && entity != null) {
			double range = 8;
			List<LivingEntity> alreadyTargetedEntities = new ArrayList<>();
			int dmg = 5;
			long lightningSeed = ItemNBTHelper.getLong(stack, TAG_LIGHTNING_SEED, 0);

			Predicate<Entity> selector = e -> e instanceof LivingEntity && e instanceof IMob && !(e instanceof PlayerEntity) && !alreadyTargetedEntities.contains(e);

			Random rand = new Random(lightningSeed);
			LivingEntity lightningSource = entity;
			int hops = entity.world.isThundering() ? 10 : 4;
			for (int i = 0; i < hops; i++) {
				List<Entity> entities = entity.world.getEntitiesInAABBexcluding(lightningSource, new AxisAlignedBB(lightningSource.getPosX() - range, lightningSource.getPosY() - range, lightningSource.getPosZ() - range, lightningSource.getPosX() + range, lightningSource.getPosY() + range, lightningSource.getPosZ() + range), selector::test);
				if (entities.isEmpty()) {
					break;
				}

				LivingEntity target = (LivingEntity) entities.get(rand.nextInt(entities.size()));
				if (attacker instanceof PlayerEntity) {
					target.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) attacker), dmg);
				} else {
					target.attackEntityFrom(DamageSource.causeMobDamage(attacker), dmg);
				}

				Botania.proxy.lightningFX(Vector3.fromEntityCenter(lightningSource), Vector3.fromEntityCenter(target), 1, 0x0179C4, 0xAADFFF);

				alreadyTargetedEntities.add(target);
				lightningSource = target;
				dmg--;
			}

			if (!entity.world.isRemote) {
				ItemNBTHelper.setLong(stack, TAG_LIGHTNING_SEED, entity.world.rand.nextLong());
			}
		}

		return super.hitEntity(stack, entity, attacker);
	}

}
