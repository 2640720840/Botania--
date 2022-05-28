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
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.TileLightRelay;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockLightRelay extends BlockModWaterloggable implements ITileEntityProvider, IWandable {

	private static final VoxelShape SHAPE = makeCuboidShape(5, 5, 5, 11, 11, 11);
	public final LuminizerVariant variant;

	protected BlockLightRelay(LuminizerVariant variant, Properties builder) {
		super(builder);
		this.variant = variant;
		setDefaultState(getDefaultState().with(BlockStateProperties.POWERED, false));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (player.getHeldItem(hand).getItem() != Items.ENDER_PEARL) {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TileLightRelay) {
				((TileLightRelay) te).mountEntity(player);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (!worldIn.isRemote && variant == LuminizerVariant.TOGGLE) {
			if (state.get(BlockStateProperties.POWERED) && !worldIn.isBlockPowered(pos)) {
				worldIn.setBlockState(pos, state.with(BlockStateProperties.POWERED, false));
			} else if (!state.get(BlockStateProperties.POWERED) && worldIn.isBlockPowered(pos)) {
				worldIn.setBlockState(pos, state.with(BlockStateProperties.POWERED, true));
			}
		}
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
		world.setBlockState(pos, state.with(BlockStateProperties.POWERED, false));
	}

	@Override
	public boolean canProvidePower(BlockState state) {
		return variant == LuminizerVariant.DETECTOR;
	}

	@Override
	public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction s) {
		return variant == LuminizerVariant.DETECTOR
				&& state.get(BlockStateProperties.POWERED) ? 15 : 0;
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileLightRelay();
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		return false;
	}

}
