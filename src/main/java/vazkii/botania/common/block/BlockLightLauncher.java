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
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import vazkii.botania.common.block.tile.TileLightRelay;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class BlockLightLauncher extends BlockModWaterloggable {

	private static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 4, 16);

	public BlockLightLauncher(Properties builder) {
		super(builder);
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
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getRedstonePowerFromNeighbors(pos) > 0 || world.getRedstonePowerFromNeighbors(pos.up()) > 0;
		boolean powered = state.get(BlockStateProperties.POWERED);

		if (power && !powered) {
			pickUpEntities(world, pos);
			world.setBlockState(pos, state.with(BlockStateProperties.POWERED, true), 4);
		} else if (!power && powered) {
			world.setBlockState(pos, state.with(BlockStateProperties.POWERED, false), 4);
		}
	}

	private void pickUpEntities(World world, BlockPos pos) {
		List<TileLightRelay> relays = new ArrayList<>();
		for (Direction dir : Direction.values()) {
			TileEntity tile = world.getTileEntity(pos.offset(dir));
			if (tile instanceof TileLightRelay) {
				TileLightRelay relay = (TileLightRelay) tile;
				if (relay.getNextDestination() != null) {
					relays.add(relay);
				}
			}
		}

		if (!relays.isEmpty()) {
			AxisAlignedBB aabb = new AxisAlignedBB(pos, pos.add(1, 1, 1));
			List<Entity> entities = world.getEntitiesWithinAABB(LivingEntity.class, aabb);
			entities.addAll(world.getEntitiesWithinAABB(ItemEntity.class, aabb));

			if (!entities.isEmpty()) {
				for (Entity entity : entities) {
					TileLightRelay relay = relays.get(world.rand.nextInt(relays.size()));
					relay.mountEntity(entity);
				}
			}
		}
	}

}
