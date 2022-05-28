/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.common.block.tile.ModTiles;

import java.util.Random;

public class TileRedStringFertilizer extends TileRedString {
	public TileRedStringFertilizer() {
		super(ModTiles.RED_STRING_FERTILIZER);
	}

	public boolean canGrow(IBlockReader world, boolean isClient) {
		BlockPos binding = getBinding();
		Block block = getBlockAtBinding();

		return block instanceof IGrowable && ((IGrowable) block).canGrow(world, binding, world.getBlockState(binding), isClient);
	}

	public boolean canUseBonemeal(World world, Random rand) {
		BlockPos binding = getBinding();
		Block block = getBlockAtBinding();
		return block instanceof IGrowable && ((IGrowable) block).canUseBonemeal(world, rand, binding, world.getBlockState(binding));
	}

	public void grow(ServerWorld world, Random rand) {
		BlockPos binding = getBinding();
		Block block = getBlockAtBinding();
		if (block instanceof IGrowable) {
			((IGrowable) block).grow(world, rand, binding, world.getBlockState(binding));
		}
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return world.getBlockState(pos).getBlock() instanceof IGrowable;
	}

}
