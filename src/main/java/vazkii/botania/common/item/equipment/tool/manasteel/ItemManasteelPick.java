/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.manasteel;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.ISortableTool;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import javax.annotation.Nonnull;

import java.util.function.Consumer;
import java.util.regex.Pattern;

public class ItemManasteelPick extends PickaxeItem implements IManaUsingItem, ISortableTool {

	private static final Pattern TORCH_PATTERN = Pattern.compile("(?:(?:(?:[A-Z-_.:]|^)torch)|(?:(?:[a-z-_.:]|^)Torch))(?:[A-Z-_.:]|$)");

	private static final int MANA_PER_DAMAGE = 60;

	public ItemManasteelPick(Properties props) {
		this(BotaniaAPI.instance().getManasteelItemTier(), props, -2.8F);
	}

	public ItemManasteelPick(IItemTier mat, Properties props, float attackSpeed) {
		super(mat, 1, attackSpeed, props);
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		return ToolCommons.damageItemIfPossible(stack, amount, entity, getManaPerDamage());
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		PlayerEntity player = ctx.getPlayer();

		if (player != null) {
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				ItemStack stackAt = player.inventory.getStackInSlot(i);
				if (!stackAt.isEmpty() && TORCH_PATTERN.matcher(stackAt.getItem().getTranslationKey()).find()) {
					ItemStack displayStack = stackAt.copy();
					ActionResultType did = PlayerHelper.substituteUse(ctx, stackAt);
					if (did.isSuccessOrConsume() && !ctx.getWorld().isRemote) {
						ItemsRemainingRenderHandler.send(player, displayStack, TORCH_PATTERN);
					}
					return did;
				}
			}
		}

		return ActionResultType.PASS;
	}

	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity player, int slot, boolean selected) {
		if (!world.isRemote && player instanceof PlayerEntity && stack.getDamage() > 0 && ManaItemHandler.instance().requestManaExactForTool(stack, (PlayerEntity) player, MANA_PER_DAMAGE * 2, true)) {
			stack.setDamage(stack.getDamage() - 1);
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public int getSortingPriority(ItemStack stack, BlockState state) {
		return ToolCommons.getToolPriority(stack);
	}
}
