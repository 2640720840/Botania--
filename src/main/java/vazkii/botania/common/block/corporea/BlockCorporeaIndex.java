/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.corporea.TileCorporeaBase;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;

import javax.annotation.Nonnull;

public class BlockCorporeaIndex extends BlockModWaterloggable implements ITileEntityProvider {
	private static final VoxelShape SHAPE = makeCuboidShape(2, 2, 2, 14, 14, 14);

	public BlockCorporeaIndex(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public TileCorporeaBase createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileCorporeaIndex();
	}
}
