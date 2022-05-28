/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Vector3f;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.tile.TileTerraPlate;

import javax.annotation.Nonnull;

public class RenderTileTerraPlate extends TileEntityRenderer<TileTerraPlate> {

	public RenderTileTerraPlate(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TileTerraPlate plate, float f, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		float alphaMod = Math.min(1.0F, plate.getCompletion() / 0.1F);

		ms.push();
		ms.rotate(Vector3f.XP.rotationDegrees(90F));
		ms.translate(0F, 0F, -3F / 16F - 0.001F);

		float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + f) / 8D) + 1D) / 5D + 0.6D) * alphaMod;

		IVertexBuilder buffer = buffers.getBuffer(RenderHelper.TERRA_PLATE);
		IconHelper.renderIcon(ms, buffer, 0, 0, MiscellaneousIcons.INSTANCE.terraPlateOverlay.getSprite(), 1, 1, alpha);

		ms.pop();
	}

}
