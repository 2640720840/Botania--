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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;

import javax.annotation.Nonnull;

public class TerraPickTippingRecipe extends SpecialRecipe {
	public static final SpecialRecipeSerializer<TerraPickTippingRecipe> SERIALIZER = new SpecialRecipeSerializer<>(TerraPickTippingRecipe::new);

	public TerraPickTippingRecipe(ResourceLocation id) {
		super(id);
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		boolean foundTerraPick = false;
		boolean foundElementiumPick = false;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ItemTerraPick && !ItemTerraPick.isTipped(stack)) {
					foundTerraPick = true;
				} else if (stack.getItem() == ModItems.elementiumPick) {
					foundElementiumPick = true;
				} else {
					return false; // Found an invalid item, breaking the recipe
				}
			}
		}

		return foundTerraPick && foundElementiumPick;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		ItemStack terraPick = ItemStack.EMPTY;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemTerraPick) {
				terraPick = stack;
			}
		}

		if (terraPick.isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack terraPickCopy = terraPick.copy();
		ItemTerraPick.setTipped(terraPickCopy);
		return terraPickCopy;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}
}
