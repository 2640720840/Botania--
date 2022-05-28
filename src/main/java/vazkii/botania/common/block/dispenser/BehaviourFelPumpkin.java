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
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

// Taken from vanilla pumpkin dispense behaviour
public class BehaviourFelPumpkin extends OptionalDispenseBehavior {
	@Nonnull
	@Override
	protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		World world = source.getWorld();
		BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
		Block blockcarvedpumpkin = ModBlocks.felPumpkin;
		this.setSuccessful(false);
		if (world.isAirBlock(blockpos) && world.getBlockState(blockpos.down()).getBlock() == Blocks.IRON_BARS
				&& world.getBlockState(blockpos.down(2)).getBlock() == Blocks.IRON_BARS) // Botania - Check for iron bars
		{
			this.setSuccessful(true);
			if (!world.isRemote) {
				world.setBlockState(blockpos, blockcarvedpumpkin.getDefaultState(), 3);
			}

			stack.shrink(1);
		}

		return stack;
	}
}
