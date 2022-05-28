/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import com.google.gson.JsonObject;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import vazkii.botania.api.mana.IManaItem;

import javax.annotation.Nonnull;

public class ManaUpgradeRecipe implements ICraftingRecipe {
	private final ShapedRecipe compose;

	public ManaUpgradeRecipe(ShapedRecipe compose) {
		this.compose = compose;
	}

	public ShapedRecipe getCompose() {
		return compose;
	}

	public static ItemStack output(ItemStack output, IInventory inv) {
		ItemStack out = output.copy();
		if (!(out.getItem() instanceof IManaItem)) {
			return out;
		}
		IManaItem outItem = (IManaItem) out.getItem();
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof IManaItem) {
					IManaItem item = (IManaItem) stack.getItem();
					outItem.addMana(out, item.getMana(stack));
				}
			}
		}
		return out;
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		return compose.matches(inv, world);
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		return output(compose.getCraftingResult(inv), inv);
	}

	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return compose.getIngredients();
	}

	@Override
	public boolean canFit(int width, int height) {
		return compose.canFit(width, height);
	}

	@Nonnull
	@Override
	public ItemStack getRecipeOutput() {
		return compose.getRecipeOutput();
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return compose.getId();
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static final IRecipeSerializer<ManaUpgradeRecipe> SERIALIZER = new Serializer();

	private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ManaUpgradeRecipe> {
		@Override
		public ManaUpgradeRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			return new ManaUpgradeRecipe(IRecipeSerializer.CRAFTING_SHAPED.read(recipeId, json));
		}

		@Override
		public ManaUpgradeRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
			return new ManaUpgradeRecipe(IRecipeSerializer.CRAFTING_SHAPED.read(recipeId, buffer));
		}

		@Override
		public void write(@Nonnull PacketBuffer buffer, ManaUpgradeRecipe recipe) {
			IRecipeSerializer.CRAFTING_SHAPED.write(buffer, recipe.compose);
		}
	};
}
