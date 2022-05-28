/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.bow;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;

import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import java.util.function.Consumer;

public class ItemCrystalBow extends ItemLivingwoodBow {

	private final int ARROW_COST = 200;

	public ItemCrystalBow(Properties builder) {
		super(builder);
	}

	@Override
	public float chargeVelocityMultiplier() {
		return 2F;
	}

	@Override
	boolean canFire(ItemStack stack, PlayerEntity player) {
		boolean infinity = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
		return player.abilities.isCreativeMode || ManaItemHandler.instance().requestManaExactForTool(stack, player, ARROW_COST / (infinity ? 2 : 1), false);
	}

	@Override
	void onFire(ItemStack stack, LivingEntity living, boolean infinity, AbstractArrowEntity arrow) {
		arrow.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		boolean infinity = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
		return ToolCommons.damageItemIfPossible(stack, amount, entity, ARROW_COST / (infinity ? 2 : 1));
	}
}
