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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.block.tile.mana.TilePrism;
import vazkii.botania.common.entity.EntityManaBurst;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockPrism extends BlockModWaterloggable implements ITileEntityProvider, IManaTrigger, IManaCollisionGhost, IWandHUD {
	private static final VoxelShape SHAPE = makeCuboidShape(4, 0, 4, 12, 16, 12);

	public BlockPrism(Properties builder) {
		super(builder);
		setDefaultState(getDefaultState()
				.with(BlockStateProperties.POWERED, false)
				.with(BotaniaStateProps.HAS_LENS, false));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
		if (context.getEntity() instanceof EntityManaBurst) {
			// Expose the shape so bursts can actually collide with us
			// they will still go through the prism via IManaCollisionGhost
			return SHAPE;
		} else {
			return super.getCollisionShape(state, world, pos, context);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		if (state.get(BlockStateProperties.POWERED)) {
			redstoneParticlesInShape(state, world, pos, rand);
		}
	}

	public static void redstoneParticlesInShape(BlockState state, World world, BlockPos pos, Random rand) {
		if (rand.nextBoolean()) {
			AxisAlignedBB localBox = state.getShape(world, pos).getBoundingBox();
			double x = pos.getX() + localBox.minX + rand.nextDouble() * (localBox.maxX - localBox.minX);
			double y = pos.getY() + localBox.minY + rand.nextDouble() * (localBox.maxY - localBox.minY);
			double z = pos.getZ() + localBox.minZ + rand.nextDouble() * (localBox.maxZ - localBox.minZ);
			world.addParticle(RedstoneParticleData.REDSTONE_DUST, x, y, z, 0, 0, 0);
		}
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(BlockStateProperties.POWERED, BotaniaStateProps.HAS_LENS);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		TileEntity tile = world.getTileEntity(pos);
		if (!(tile instanceof TilePrism)) {
			return ActionResultType.PASS;
		}

		TilePrism prism = (TilePrism) tile;
		ItemStack lens = prism.getItemHandler().getStackInSlot(0);
		ItemStack heldItem = player.getHeldItem(hand);
		boolean isHeldItemLens = !heldItem.isEmpty() && heldItem.getItem() instanceof ILens;

		if (lens.isEmpty() && isHeldItemLens) {
			if (!player.abilities.isCreativeMode) {
				player.setHeldItem(hand, ItemStack.EMPTY);
			}

			prism.getItemHandler().setInventorySlotContents(0, heldItem.copy());
		} else if (!lens.isEmpty()) {
			player.inventory.placeItemBackInInventory(player.world, lens);
			prism.getItemHandler().setInventorySlotContents(0, ItemStack.EMPTY);
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		boolean power = world.getRedstonePowerFromNeighbors(pos) > 0 || world.getRedstonePowerFromNeighbors(pos.up()) > 0;
		return this.getDefaultState().with(BlockStateProperties.POWERED, power);
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getRedstonePowerFromNeighbors(pos) > 0 || world.getRedstonePowerFromNeighbors(pos.up()) > 0;
		boolean powered = state.get(BlockStateProperties.POWERED);

		if (!world.isRemote) {
			if (power && !powered) {
				world.setBlockState(pos, state.with(BlockStateProperties.POWERED, true));
			} else if (!power && powered) {
				world.setBlockState(pos, state.with(BlockStateProperties.POWERED, false));
			}
		}
	}

	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TileSimpleInventory) {
				InventoryHelper.dropInventoryItems(world, pos, ((TileSimpleInventory) te).getItemHandler());
			}
			super.onReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TilePrism();
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TilePrism) {
			((TilePrism) tile).onBurstCollision(burst);
		}
	}

	@Override
	public boolean isGhost(BlockState state, World world, BlockPos pos) {
		return true;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, Minecraft mc, World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TilePrism) {
			ItemStack lens = ((TilePrism) te).getStackInSlot(0);
			if (!lens.isEmpty()) {
				ITextComponent lensName = lens.getDisplayName();
				int width = 16 + mc.fontRenderer.getStringPropertyWidth(lensName) / 2;
				int x = mc.getMainWindow().getScaledWidth() / 2 - width;
				int y = mc.getMainWindow().getScaledHeight() / 2;

				mc.fontRenderer.func_243246_a(ms, lensName, x + 20, y + 5, -1);
				mc.getItemRenderer().renderItemAndEffectIntoGUI(lens, x, y);
			}
		}
	}
}
