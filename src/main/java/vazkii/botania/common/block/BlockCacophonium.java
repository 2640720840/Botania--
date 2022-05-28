/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.TileCacophonium;

import javax.annotation.Nonnull;

public class BlockCacophonium extends BlockMod implements ITileEntityProvider {
	protected BlockCacophonium(Properties builder) {
		super(builder);
		setDefaultState(getDefaultState().with(BlockStateProperties.POWERED, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getRedstonePowerFromNeighbors(pos) > 0 || world.getRedstonePowerFromNeighbors(pos.up()) > 0;
		boolean powered = state.get(BlockStateProperties.POWERED);

		if (power && !powered) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileCacophonium) {
				((TileCacophonium) tile).annoyDirewolf();
			}
			world.setBlockState(pos, state.with(BlockStateProperties.POWERED, true), 4);
		} else if (!power && powered) {
			world.setBlockState(pos, state.with(BlockStateProperties.POWERED, false), 4);
		}
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TileCacophonium) {
				InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), ((TileCacophonium) te).stack);
			}
			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileCacophonium();
	}

}
