/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Random;
import java.util.function.Supplier;

public class BlockSpecialFlower extends FlowerBlock implements ITileEntityProvider, IWandable, IWandHUD {
	private static final VoxelShape SHAPE = makeCuboidShape(4.8, 0, 4.8, 12.8, 16, 12.8);
	private final Supplier<? extends TileEntitySpecialFlower> teProvider;

	public BlockSpecialFlower(Effect stewEffect, int stewDuration, Properties props, Supplier<? extends TileEntitySpecialFlower> teProvider) {
		super(stewEffect, stewDuration, props);
		this.teProvider = teProvider;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, ISelectionContext ctx) {
		Vector3d shift = state.getOffset(world, pos);
		return SHAPE.withOffset(shift.x, shift.y, shift.z);
	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return state.getBlock() == ModBlocks.redStringRelay
				|| state.getBlock() == Blocks.MYCELIUM
				|| super.isValidGround(state, worldIn, pos);
	}

	@Override
	public boolean eventReceived(BlockState state, World world, BlockPos pos, int event, int param) {
		super.eventReceived(state, world, pos, event, param);
		TileEntity tileentity = world.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(event, param);
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return teProvider.get();
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		return ((TileEntitySpecialFlower) world.getTileEntity(pos)).onWanded(player, stack);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		((TileEntitySpecialFlower) world.getTileEntity(pos)).onBlockPlacedBy(world, pos, state, entity, stack);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, Minecraft mc, World world, BlockPos pos) {
		((TileEntitySpecialFlower) world.getTileEntity(pos)).renderHUD(ms, mc);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		redstoneParticlesIfPowered(state, world, pos, rand);
	}

	public static void redstoneParticlesIfPowered(BlockState state, World world, BlockPos pos, Random rand) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityFunctionalFlower && rand.nextBoolean()) {
			TileEntityFunctionalFlower flower = (TileEntityFunctionalFlower) te;
			if (flower.acceptsRedstone() && flower.redstoneSignal > 0) {
				VoxelShape shape = state.getShape(world, pos);
				if (!shape.isEmpty()) {
					AxisAlignedBB localBox = shape.getBoundingBox();
					BlockPos effPos = flower.getEffectivePos();
					double x = effPos.getX() + localBox.minX + rand.nextDouble() * (localBox.maxX - localBox.minX);
					double y = effPos.getY() + localBox.minY + rand.nextDouble() * (localBox.maxY - localBox.minY);
					double z = effPos.getZ() + localBox.minZ + rand.nextDouble() * (localBox.maxZ - localBox.minZ);
					world.addParticle(RedstoneParticleData.REDSTONE_DUST, x, y, z, 0, 0, 0);
				}
			}
		}
	}
}
