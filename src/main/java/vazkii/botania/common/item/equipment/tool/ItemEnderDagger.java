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
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;

import javax.annotation.Nonnull;

import java.util.function.Consumer;

public class ItemEnderDagger extends ItemManasteelSword {

	public ItemEnderDagger(Properties props) {
		super(BotaniaAPI.instance().getManasteelItemTier(), 3, -1.25F, props);
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, @Nonnull LivingEntity attacker) {
		if (!target.world.isRemote
				&& target instanceof EndermanEntity
				&& attacker instanceof PlayerEntity) {
			target.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) attacker), 20);
		}

		stack.damageItem(1, attacker, e -> e.sendBreakAnimation(Hand.MAIN_HAND));
		return true;
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return amount;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity player, int slot, boolean selected) {}

	@Override
	public boolean usesMana(ItemStack stack) {
		return false;
	}

}
