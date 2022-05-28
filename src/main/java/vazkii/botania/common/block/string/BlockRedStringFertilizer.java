/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.string;

import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringFertilizer;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockRedStringFertilizer extends BlockRedString implements IGrowable {

	public BlockRedStringFertilizer(Properties builder) {
		super(builder);
		setDefaultState(getDefaultState().with(BlockStateProperties.FACING, Direction.DOWN));
	}

	@Override
	public boolean canGrow(@Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean isClient) {
		return ((TileRedStringFertilizer) world.getTileEntity(pos)).canGrow(world, isClient);
	}

	@Override
	public boolean canUseBonemeal(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		return ((TileRedStringFertilizer) world.getTileEntity(pos)).canUseBonemeal(world, rand);
	}

	@Override
	public void grow(@Nonnull ServerWorld world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		((TileRedStringFertilizer) world.getTileEntity(pos)).grow(world, rand);
	}

	@Nonnull
	@Override
	public TileRedString createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileRedStringFertilizer();
	}
}
