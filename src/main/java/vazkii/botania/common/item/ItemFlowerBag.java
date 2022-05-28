/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.*;
import net.minecraftforge.items.wrapper.InvWrapper;

import vazkii.botania.client.gui.bag.ContainerFlowerBag;
import vazkii.botania.common.block.BlockModFlower;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemFlowerBag extends Item {
	public static final int SIZE = 16;

	public ItemFlowerBag(Properties props) {
		super(props);
		MinecraftForge.EVENT_BUS.addListener(this::onPickupItem);
	}

	public static boolean isValid(int slot, ItemStack stack) {
		Block blk = Block.getBlockFromItem(stack.getItem());
		return !stack.isEmpty()
				&& blk.getClass() == BlockModFlower.class
				&& slot == ((BlockModFlower) blk).color.getId();
	}

	public static Inventory getInventory(ItemStack stack) {
		return new ItemBackedInventory(stack, SIZE) {
			@Override
			public boolean isItemValidForSlot(int slot, @Nonnull ItemStack stack) {
				return isValid(slot, stack);
			}
		};
	}

	@Nonnull
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {
		return new InvProvider(stack);
	}

	private static class InvProvider implements ICapabilityProvider {
		private final LazyOptional<IItemHandler> opt;

		private InvProvider(ItemStack stack) {
			opt = LazyOptional.of(() -> new InvWrapper(getInventory(stack)));
		}

		@Nonnull
		@Override
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, opt);
		}
	}

	private void onPickupItem(EntityItemPickupEvent event) {
		ItemStack entityStack = event.getItem().getItem();
		if (Block.getBlockFromItem(entityStack.getItem()) instanceof BlockModFlower && entityStack.getCount() > 0) {
			int color = ((BlockModFlower) Block.getBlockFromItem(entityStack.getItem())).color.getId();

			for (int i = 0; i < event.getPlayer().inventory.getSizeInventory(); i++) {
				if (i == event.getPlayer().inventory.currentItem) {
					continue; // prevent item deletion
				}

				ItemStack bag = event.getPlayer().inventory.getStackInSlot(i);
				if (!bag.isEmpty() && bag.getItem() == this) {
					Inventory bagInv = getInventory(bag);
					ItemStack existing = bagInv.getStackInSlot(color);
					int newCount = Math.min(existing.getCount() + entityStack.getCount(),
							Math.min(existing.getMaxStackSize(), bagInv.getInventoryStackLimit()));
					int numPickedUp = newCount - existing.getCount();

					if (numPickedUp > 0) {
						if (existing.isEmpty()) {
							bagInv.setInventorySlotContents(color, entityStack.split(numPickedUp));
						} else {
							existing.grow(numPickedUp);
							entityStack.shrink(numPickedUp);
						}
						event.getItem().setItem(entityStack);
						bagInv.markDirty();

						event.setCanceled(true);
						event.getPlayer().onItemPickup(event.getItem(), numPickedUp);

						return;
					}
				}
			}
		}
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		if (!world.isRemote) {
			ItemStack stack = player.getHeldItem(hand);
			INamedContainerProvider container = new SimpleNamedContainerProvider((w, p, pl) -> new ContainerFlowerBag(w, p, stack), stack.getDisplayName());
			NetworkHooks.openGui((ServerPlayerEntity) player, container, buf -> {
				buf.writeBoolean(hand == Hand.MAIN_HAND);
			});
		}
		return ActionResult.resultSuccess(player.getHeldItem(hand));
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		Direction side = ctx.getFace();

		TileEntity tile = world.getTileEntity(pos);
		if (tile != null) {
			if (!world.isRemote) {
				IItemHandler tileInv;
				if (tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).isPresent()) {
					tileInv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).orElseThrow(NullPointerException::new);
				} else if (tile instanceof IInventory) {
					tileInv = new InvWrapper((IInventory) tile);
				} else {
					return ActionResultType.FAIL;
				}

				IInventory bagInv = getInventory(ctx.getItem());
				for (int i = 0; i < bagInv.getSizeInventory(); i++) {
					ItemStack flower = bagInv.getStackInSlot(i);
					ItemStack rem = ItemHandlerHelper.insertItemStacked(tileInv, flower, false);
					bagInv.setInventorySlotContents(i, rem);
				}

			}

			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}
