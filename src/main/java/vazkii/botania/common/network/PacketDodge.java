/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemDodgeRing;

import java.util.function.Supplier;

public class PacketDodge {
	public static void encode(PacketDodge msg, PacketBuffer buf) {}

	public static PacketDodge decode(PacketBuffer buf) {
		return new PacketDodge();
	}

	public static void handle(PacketDodge msg, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide().isServer()) {
			ctx.get().enqueueWork(() -> {
				ServerPlayerEntity player = ctx.get().getSender();
				player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.dash, SoundCategory.PLAYERS, 1F, 1F);

				ItemStack ringStack = EquipmentHandler.findOrEmpty(ModItems.dodgeRing, player);
				if (ringStack.isEmpty()) {
					player.connection.disconnect(new TranslationTextComponent("botaniamisc.invalidDodge"));
					return;
				}

				player.addExhaustion(0.3F);
				ItemNBTHelper.setInt(ringStack, ItemDodgeRing.TAG_DODGE_COOLDOWN, ItemDodgeRing.MAX_CD);
			});
		}
		ctx.get().setPacketHandled(true);
	}
}
