/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;

import vazkii.botania.api.recipe.StateIngredient;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StateIngredientBlock implements StateIngredient {
	private final Block block;

	public StateIngredientBlock(Block block) {
		this.block = block;
	}

	@Override
	public boolean test(BlockState blockState) {
		return block == blockState.getBlock();
	}

	@Override
	public BlockState pick(Random random) {
		return block.getDefaultState();
	}

	@Override
	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", "block");
		object.addProperty("block", Registry.BLOCK.getKey(block).toString());
		return object;
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeVarInt(1);
		buffer.writeRegistryIdUnsafe(ForgeRegistries.BLOCKS, block);
	}

	@Override
	public List<ItemStack> getDisplayedStacks() {
		if (block.asItem() == Items.AIR) {
			return Collections.emptyList();
		}
		return Collections.singletonList(new ItemStack(block));
	}

	@Override
	public List<BlockState> getDisplayed() {
		return Collections.singletonList(block.getDefaultState());
	}

	public Block getBlock() {
		return block;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		return block == ((StateIngredientBlock) o).block;
	}

	@Override
	public int hashCode() {
		return block.hashCode();
	}

	@Override
	public String toString() {
		return "StateIngredientBlock{" + block + "}";
	}
}
