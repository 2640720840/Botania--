/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;

import vazkii.botania.common.core.handler.ContributorList;

import javax.annotation.Nonnull;

import java.util.*;

public final class ContributorFancinessHandler extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {

	public ContributorFancinessHandler(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> renderer) {
		super(renderer);
	}

	@Override
	public void render(MatrixStack ms, IRenderTypeBuffer buffers, int light, @Nonnull AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ContributorList.firstStart();

		if (player.isInvisible()) {
			return;
		}

		String name = player.getGameProfile().getName();

		if (name.equals("haighyorkie")) {
			renderGoldfish(ms, buffers);
		}

		if (player.isWearing(PlayerModelPart.CAPE)) {
			ItemStack flower = ContributorList.getFlower(name.toLowerCase(Locale.ROOT));
			if (!flower.isEmpty()) {
				renderFlower(ms, buffers, player, flower);
			}
		}

	}

	private void renderGoldfish(MatrixStack ms, IRenderTypeBuffer buffers) {
		ms.push();
		getEntityModel().bipedHead.translateRotate(ms);
		ms.translate(-0.15F, -0.60F, 0F);
		ms.scale(0.4F, -0.4F, -0.4F);
		Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(ms.getLast(), buffers.getBuffer(Atlases.getTranslucentCullBlockType()), null, MiscellaneousIcons.INSTANCE.goldfishModel, 1, 1, 1, 0xF000F0, OverlayTexture.NO_OVERLAY);
		ms.pop();
	}

	private void renderFlower(MatrixStack ms, IRenderTypeBuffer buffers, PlayerEntity player, ItemStack flower) {
		ms.push();
		getEntityModel().bipedHead.translateRotate(ms);
		ms.translate(0, -0.75, 0);
		ms.scale(0.5F, -0.5F, -0.5F);
		Minecraft.getInstance().getItemRenderer().renderItem(player, flower, ItemCameraTransforms.TransformType.NONE, false, ms, buffers, player.world, 0xF000F0, OverlayTexture.NO_OVERLAY);
		ms.pop();
	}
}
