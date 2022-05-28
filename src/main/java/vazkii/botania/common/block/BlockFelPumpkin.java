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
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.mixin.AccessorMobEntity;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class BlockFelPumpkin extends BlockMod {
	private static final ResourceLocation LOOT_TABLE = prefix("fel_blaze");

	public BlockFelPumpkin(Properties builder) {
		super(builder);
		setDefaultState(getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onBlockAdded(state, world, pos, oldState, isMoving);

		if (!world.isRemote && world.getBlockState(pos.down()).getBlock() == Blocks.IRON_BARS && world.getBlockState(pos.down(2)).getBlock() == Blocks.IRON_BARS) {
			world.removeBlock(pos, false);
			world.removeBlock(pos.down(), false);
			world.removeBlock(pos.down(2), false);
			BlazeEntity blaze = EntityType.BLAZE.create(world);
			blaze.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() - 1.95D, pos.getZ() + 0.5D, 0.0F, 0.0F);
			blaze.enablePersistence();
			((AccessorMobEntity) blaze).setDeathLootTable(LOOT_TABLE);
			blaze.onInitialSpawn((ServerWorld) world, world.getDifficultyForLocation(pos), SpawnReason.EVENT, null, null);
			world.addEntity(blaze);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

}
