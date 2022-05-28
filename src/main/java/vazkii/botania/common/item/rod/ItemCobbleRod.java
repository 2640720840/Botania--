/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;

import javax.annotation.Nonnull;

public class ItemCobbleRod extends Item implements IManaUsingItem, IBlockProvider {

	static final int COST = 150;

	public ItemCobbleRod(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		return ItemDirtRod.place(ctx, Blocks.COBBLESTONE, COST, 0.3F, 0.3F, 0.3F);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public boolean provideBlock(PlayerEntity player, ItemStack requestor, ItemStack stack, Block block, boolean doit) {
		if (block == Blocks.COBBLESTONE) {
			return (doit && ManaItemHandler.instance().requestManaExactForTool(requestor, player, COST, true)) ||
					(!doit && ManaItemHandler.instance().requestManaExactForTool(requestor, player, COST, false));
		}
		return false;
	}

	@Override
	public int getBlockCount(PlayerEntity player, ItemStack requestor, ItemStack stack, Block block) {
		if (block == Blocks.COBBLESTONE) {
			return ManaItemHandler.instance().getInvocationCountForTool(requestor, player, COST);
		}
		return 0;
	}

}
