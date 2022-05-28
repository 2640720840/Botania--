/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import vazkii.botania.common.item.ItemManaGun;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class ManaGunRemoveLensRecipe extends SpecialRecipe {
	public static final SpecialRecipeSerializer<ManaGunRemoveLensRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ManaGunRemoveLensRecipe::new);

	public ManaGunRemoveLensRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		boolean foundGun = false;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ItemManaGun && !ItemManaGun.getLens(stack).isEmpty()) {
					foundGun = true;
				} else {
					return false; // Found an invalid item, breaking the recipe
				}
			}
		}

		return foundGun;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		ItemStack gun = ItemStack.EMPTY;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ItemManaGun) {
					gun = stack;
				}
			}
		}

		ItemStack gunCopy = gun.copy();
		gunCopy.setCount(1);
		ItemManaGun.setLens(gunCopy, ItemStack.EMPTY);

		return gunCopy;
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
			if (s.getItem() == ModItems.manaGun) {
				ItemStack stack = ItemManaGun.getLens(s);
				stack.setCount(1);
				return stack;
			}
			return null;
		});
	}
}
