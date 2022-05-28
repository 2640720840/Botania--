/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.api.wand.IWireframeAABBProvider;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.core.helper.ColorHelper;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;

public class BlockSpreader extends BlockModWaterloggable implements ITileEntityProvider, IWandable, IWandHUD, IWireframeAABBProvider {
	private static final VoxelShape RENDER_SHAPE = makeCuboidShape(1, 1, 1, 15, 15, 15);

	public enum Variant {
		MANA(160, 1000, 0x20FF20, 0x00FF00, 60, 4f, 1f),
		REDSTONE(160, 1000, 0xFF2020, 0xFF0000, 60, 4f, 1f),
		ELVEN(240, 1000, 0xFF45C4, 0xFF00AE, 80, 4f, 1.25f),
		GAIA(640, 6400, 0x20FF20, 0x00FF00, 120, 20f, 2f);

		public final int burstMana;
		public final int manaCapacity;
		public final int color;
		public final int hudColor;
		public final int preLossTicks;
		public final float lossPerTick;
		public final float motionModifier;

		Variant(int bm, int mc, int c, int hc, int plt, float lpt, float mm) {
			burstMana = bm;
			manaCapacity = mc;
			color = c;
			hudColor = hc;
			preLossTicks = plt;
			lossPerTick = lpt;
			motionModifier = mm;
		}
	}

	public final Variant variant;

	public BlockSpreader(Variant v, Properties builder) {
		super(builder);
		this.variant = v;
	}

	@Nonnull
	@Override
	public VoxelShape getRenderShape(BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos) {
		return RENDER_SHAPE;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		Direction orientation = placer == null ? Direction.WEST : Direction.getFacingDirections(placer)[0].getOpposite();
		TileSpreader spreader = (TileSpreader) world.getTileEntity(pos);

		switch (orientation) {
		case DOWN:
			spreader.rotationY = -90F;
			break;
		case UP:
			spreader.rotationY = 90F;
			break;
		case NORTH:
			spreader.rotationX = 270F;
			break;
		case SOUTH:
			spreader.rotationX = 90F;
			break;
		case WEST:
			break;
		case EAST:
			spreader.rotationX = 180F;
			break;
		}
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		TileEntity tile = world.getTileEntity(pos);
		if (!(tile instanceof TileSpreader)) {
			return ActionResultType.PASS;
		}

		TileSpreader spreader = (TileSpreader) tile;
		ItemStack lens = spreader.getItemHandler().getStackInSlot(0);
		ItemStack heldItem = player.getHeldItem(hand);
		boolean isHeldItemLens = !heldItem.isEmpty() && heldItem.getItem() instanceof ILens;
		boolean wool = !heldItem.isEmpty() && ColorHelper.isWool(Block.getBlockFromItem(heldItem.getItem()));

		if (!heldItem.isEmpty()) {
			if (heldItem.getItem() == ModItems.twigWand) {
				return ActionResultType.PASS;
			}
		}

		if (lens.isEmpty() && isHeldItemLens) {
			if (!player.abilities.isCreativeMode) {
				player.setHeldItem(hand, ItemStack.EMPTY);
			}

			spreader.getItemHandler().setInventorySlotContents(0, heldItem.copy());

			return ActionResultType.SUCCESS;
		} else if (!lens.isEmpty() && !wool) {
			player.inventory.placeItemBackInInventory(player.world, lens);
			spreader.getItemHandler().setInventorySlotContents(0, ItemStack.EMPTY);

			return ActionResultType.SUCCESS;
		}

		if (wool && spreader.paddingColor == null) {
			Block block = Block.getBlockFromItem(heldItem.getItem());
			spreader.paddingColor = ColorHelper.getWoolColor(block);

			if (!player.abilities.isCreativeMode) {
				heldItem.shrink(1);
				if (heldItem.isEmpty()) {
					player.setHeldItem(hand, ItemStack.EMPTY);
				}
			}

			world.playSound(player, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 1f, 1f);

			return ActionResultType.SUCCESS;
		} else if (wool || heldItem.isEmpty() && spreader.paddingColor != null && lens.isEmpty()) {
			ItemStack pad = new ItemStack(ColorHelper.WOOL_MAP.apply(spreader.paddingColor));
			player.inventory.placeItemBackInInventory(player.world, pad);
			spreader.paddingColor = null;
			spreader.markDirty();

			world.playSound(player, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 1f, 1f);

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tile = world.getTileEntity(pos);
			if (!(tile instanceof TileSpreader)) {
				return;
			}

			TileSpreader inv = (TileSpreader) tile;

			if (inv.paddingColor != null) {
				ItemStack padding = new ItemStack(ColorHelper.WOOL_MAP.apply(inv.paddingColor));
				world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), padding));
			}

			InventoryHelper.dropInventoryItems(world, pos, inv.getItemHandler());

			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		((TileSpreader) world.getTileEntity(pos)).onWanded(player, stack);
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileSpreader();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, Minecraft mc, World world, BlockPos pos) {
		((TileSpreader) world.getTileEntity(pos)).renderHUD(ms, mc);
	}

	@Override
	public List<AxisAlignedBB> getWireframeAABB(World world, BlockPos pos) {
		return ImmutableList.of(new AxisAlignedBB(pos).shrink(1.0 / 16.0));
	}
}
