/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemBlackHoleTalisman;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class BlackHoleTalismanExtractRecipe extends SpecialRecipe {
	public static final SpecialRecipeSerializer<BlackHoleTalismanExtractRecipe> SERIALIZER = new SpecialRecipeSerializer<>(BlackHoleTalismanExtractRecipe::new);

	public BlackHoleTalismanExtractRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		boolean foundTalisman = false;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() == ModItems.blackHoleTalisman && !foundTalisman) {
					foundTalisman = true;
				} else {
					return false;
				}
			}
		}

		return foundTalisman;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		ItemStack talisman = ItemStack.EMPTY;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				talisman = stack;
			}
		}

		int count = ItemBlackHoleTalisman.getBlockCount(talisman);
		if (count > 0) {
			Block block = ItemBlackHoleTalisman.getBlock(talisman);
			if (block != null) {
				return new ItemStack(block, Math.min(64, count));
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height > 0;
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getRemainingItems(@Nonnull CraftingInventory inv) {
		return RecipeUtils.getRemainingItemsSub(inv, s -> {
			if (s.getItem() == ModItems.blackHoleTalisman) {
				int count = ItemBlackHoleTalisman.getBlockCount(s);
				if (count == 0) {
					return ItemStack.EMPTY;
				}

				int extract = Math.min(64, count);
				ItemStack copy = s.copy();
				copy.setCount(1);
				ItemBlackHoleTalisman.remove(copy, extract);
				ItemNBTHelper.setBoolean(copy, ItemBlackHoleTalisman.TAG_ACTIVE, false);

				return copy;
			}
			return null;
		});
	}
}
