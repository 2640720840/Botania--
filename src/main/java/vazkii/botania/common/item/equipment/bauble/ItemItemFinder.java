/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import it.unimi.dsi.fastutil.ints.IntArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.CapWrapper;

import java.util.List;

public class ItemItemFinder extends ItemBauble {

	private static final String TAG_ENTITY_POSITIONS = "highlightPositionsEnt";
	private static final String TAG_BLOCK_POSITIONS = "highlightPositionsBlock";

	public ItemItemFinder(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity player) {
		if (!(player instanceof PlayerEntity)) {
			return;
		}

		if (player.world.isRemote) {
			this.tickClient(stack, (PlayerEntity) player);
		} else {
			this.tickServer(stack, (PlayerEntity) player);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(BipedModel<?> bipedModel, ItemStack stack, LivingEntity living, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean armor = !living.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty();
		bipedModel.bipedHead.translateRotate(ms);
		ms.translate(-0.35, -0.2, armor ? 0.05 : 0.1);
		ms.scale(0.75F, -0.75F, -0.75F);

		IBakedModel model = MiscellaneousIcons.INSTANCE.itemFinderGem;
		IVertexBuilder buffer = buffers.getBuffer(Atlases.getCutoutBlockType());
		Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer()
				.renderModelBrightnessColor(ms.getLast(), buffer, null, model, 1, 1, 1, light, OverlayTexture.NO_OVERLAY);
	}

	protected void tickClient(ItemStack stack, PlayerEntity player) {
		if (!Botania.proxy.isTheClientPlayer(player)) {
			return;
		}

		ListNBT blocks = ItemNBTHelper.getList(stack, TAG_BLOCK_POSITIONS, Constants.NBT.TAG_LONG, false);

		for (int i = 0; i < blocks.size(); i++) {
			BlockPos pos = BlockPos.fromLong(((LongNBT) blocks.get(i)).getLong());
			float m = 0.02F;
			WispParticleData data = WispParticleData.wisp(0.15F + 0.05F * (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), false);
			player.world.addParticle(data, pos.getX() + (float) Math.random(), pos.getY() + (float) Math.random(), pos.getZ() + (float) Math.random(), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5), m * (float) (Math.random() - 0.5));
		}

		int[] entities = ItemNBTHelper.getIntArray(stack, TAG_ENTITY_POSITIONS);
		for (int i : entities) {
			Entity e = player.world.getEntityByID(i);
			if (e != null && Math.random() < 0.6) {
				WispParticleData data = WispParticleData.wisp(0.15F + 0.05F * (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), Math.random() < 0.6);
				player.world.addParticle(data, e.getPosX() + (float) (Math.random() * 0.5 - 0.25) * 0.45F, e.getPosY() + e.getHeight(), e.getPosZ() + (float) (Math.random() * 0.5 - 0.25) * 0.45F, 0, 0.05F + 0.03F * (float) Math.random(), 0);
			}
		}
	}

	protected void tickServer(ItemStack stack, PlayerEntity player) {
		IntArrayList entPosBuilder = new IntArrayList();
		ListNBT blockPosBuilder = new ListNBT();

		scanForStack(player.getHeldItemMainhand(), player, entPosBuilder, blockPosBuilder);
		scanForStack(player.getHeldItemOffhand(), player, entPosBuilder, blockPosBuilder);

		int[] currentEnts = entPosBuilder.elements();

		ItemNBTHelper.setIntArray(stack, TAG_ENTITY_POSITIONS, currentEnts);
		ItemNBTHelper.setList(stack, TAG_BLOCK_POSITIONS, blockPosBuilder);
	}

	private void scanForStack(ItemStack pstack, PlayerEntity player, IntArrayList entIdBuilder, ListNBT blockPosBuilder) {
		if (!pstack.isEmpty() || player.isSneaking()) {
			int range = 24;

			List<Entity> entities = player.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(player.getPosX() - range, player.getPosY() - range, player.getPosZ() - range, player.getPosX() + range, player.getPosY() + range, player.getPosZ() + range));
			for (Entity e : entities) {
				if (e == player) {
					continue;
				}
				if (e instanceof ItemEntity) {
					ItemEntity item = (ItemEntity) e;
					ItemStack istack = item.getItem();
					if (player.isSneaking() || istack.isItemEqual(pstack) && ItemStack.areItemStackTagsEqual(istack, pstack)) {
						entIdBuilder.add(item.getEntityId());
					}
				} else if (e instanceof PlayerEntity) {
					PlayerEntity targetPlayer = (PlayerEntity) e;
					IInventory binv = BotaniaAPI.instance().getAccessoriesInventory(targetPlayer);
					if (scanInventory(targetPlayer.inventory, pstack) || scanInventory(binv, pstack)) {
						entIdBuilder.add(targetPlayer.getEntityId());
					}

				} else if (e instanceof AbstractVillagerEntity) {
					AbstractVillagerEntity villager = (AbstractVillagerEntity) e;
					for (MerchantOffer offer : villager.getOffers()) {
						if (equalStacks(pstack, offer.getBuyingStackFirst())
								|| equalStacks(pstack, offer.getBuyingStackSecond())
								|| equalStacks(pstack, offer.getSellingStack())) {
							entIdBuilder.add(villager.getEntityId());
						}
					}
				} else if (e.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
					IItemHandler cap = e.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(EmptyHandler.INSTANCE);
					if (scanInventory(new CapWrapper(cap), pstack)) {
						entIdBuilder.add(e.getEntityId());
					}
				} else if (e instanceof IInventory) {
					IInventory inv = (IInventory) e;
					if (scanInventory(inv, pstack)) {
						entIdBuilder.add(e.getEntityId());
					}
				}
			}

			if (!pstack.isEmpty()) {
				range = 12;
				BlockPos pos = player.getPosition();
				for (BlockPos pos_ : BlockPos.getAllInBoxMutable(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1))) {
					TileEntity tile = player.world.getTileEntity(pos_);
					if (tile != null) {
						boolean foundCap = false;
						for (Direction e : Direction.values()) {
							IItemHandler cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, e).orElse(EmptyHandler.INSTANCE);
							if (scanInventory(new CapWrapper(cap), pstack)) {
								blockPosBuilder.add(LongNBT.valueOf(pos_.toLong()));
								foundCap = true;
								break;
							}
						}
						if (!foundCap && tile instanceof IInventory) {
							IInventory inv = (IInventory) tile;
							if (scanInventory(inv, pstack)) {
								blockPosBuilder.add(LongNBT.valueOf(pos_.toLong()));
							}
						}
					}
				}
			}
		}
	}

	private boolean equalStacks(ItemStack stack1, ItemStack stack2) {
		return stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
	}

	private boolean scanInventory(IInventory inv, ItemStack pstack) {
		if (pstack.isEmpty()) {
			return false;
		}

		for (int l = 0; l < inv.getSizeInventory(); l++) {
			ItemStack istack = inv.getStackInSlot(l);
			// Some mods still set stuff to null apparently...
			if (istack != null && !istack.isEmpty() && equalStacks(istack, pstack)) {
				return true;
			}
		}
		return false;
	}

}
