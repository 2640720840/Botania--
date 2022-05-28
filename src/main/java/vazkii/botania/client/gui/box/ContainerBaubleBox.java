/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.gui.box;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;

import vazkii.botania.client.gui.SlotLocked;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.item.ItemBaubleBox;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class ContainerBaubleBox extends Container {
	public static ContainerBaubleBox fromNetwork(int windowId, PlayerInventory inv, PacketBuffer buf) {
		Hand hand = buf.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;
		return new ContainerBaubleBox(windowId, inv, inv.player.getHeldItem(hand));
	}

	private final ItemStack box;

	public ContainerBaubleBox(int windowId, PlayerInventory playerInv, ItemStack box) {
		super(ModItems.BAUBLE_BOX_CONTAINER, windowId);
		int i;
		int j;

		this.box = box;
		IInventory baubleBoxInv;
		if (!playerInv.player.world.isRemote) {
			baubleBoxInv = ItemBaubleBox.getInventory(box);
		} else {
			baubleBoxInv = new Inventory(ItemBaubleBox.SIZE);
		}

		for (i = 0; i < 4; ++i) {
			for (j = 0; j < 6; ++j) {
				int k = j + i * 6;
				addSlot(new Slot(baubleBoxInv, k, 62 + j * 18, 8 + i * 18) {
					@Override
					public boolean isItemValid(@Nonnull ItemStack stack) {
						return EquipmentHandler.instance.isAccessory(stack);
					}
				});
			}
		}

		for (i = 0; i < 3; ++i) {
			for (j = 0; j < 9; ++j) {
				addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			if (playerInv.getStackInSlot(i) == box) {
				addSlot(new SlotLocked(playerInv, i, 8 + i * 18, 142));
			} else {
				addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
			}
		}

	}

	@Override
	public boolean canInteractWith(@Nonnull PlayerEntity player) {
		ItemStack main = player.getHeldItemMainhand();
		ItemStack off = player.getHeldItemOffhand();
		return !main.isEmpty() && main == box || !off.isEmpty() && off == box;
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(slotIndex);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			int boxStart = 0;
			int boxEnd = boxStart + 24;
			int invEnd = boxEnd + 36;

			if (slotIndex < boxEnd) {
				if (!mergeItemStack(itemstack1, boxEnd, invEnd, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (!itemstack1.isEmpty() && EquipmentHandler.instance.isAccessory(itemstack1) && !mergeItemStack(itemstack1, boxStart, boxEnd, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

}
