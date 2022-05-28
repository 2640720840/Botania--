/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BehaviourCocoaBeans extends OptionalDispenseBehavior {
	@Nonnull
	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		Block block = Blocks.COCOA;
		Direction facing = source.getBlockState().get(DispenserBlock.FACING);
		BlockPos pos = source.getBlockPos().offset(facing);
		World world = source.getWorld();
		BlockItemUseContext ctx = new DirectionalPlaceContext(source.getWorld(), source.getBlockPos().offset(facing), facing, new ItemStack(block), facing.getOpposite());
		BlockState cocoa = block.getStateForPlacement(ctx);
		setSuccessful(false);
		if (cocoa != null && world.isAirBlock(pos)) {
			world.setBlockState(pos, cocoa);
			setSuccessful(true);
			stack.shrink(1);
		}

		return stack;
	}

}
