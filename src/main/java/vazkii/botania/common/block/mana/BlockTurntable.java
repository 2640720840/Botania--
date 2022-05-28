/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.BlockOpenCrate;
import vazkii.botania.common.block.tile.mana.TileTurntable;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockTurntable extends BlockMod implements ITileEntityProvider, IWandable, IWandHUD {

	public BlockTurntable(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileTurntable();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, Minecraft mc, World world, BlockPos pos) {
		((TileTurntable) world.getTileEntity(pos)).renderHUD(ms, mc);
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		((TileTurntable) world.getTileEntity(pos)).onWanded(player, stack, side);
		return true;
	}

	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		if (world.isBlockPowered(pos) && rand.nextDouble() < 0.2) {
			BlockOpenCrate.redstoneParticlesOnFullBlock(world, pos, rand);
		}
	}
}
