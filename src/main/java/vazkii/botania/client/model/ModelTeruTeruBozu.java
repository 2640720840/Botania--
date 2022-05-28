/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelTeruTeruBozu extends Model {

	public final ModelRenderer thread;
	public final ModelRenderer cloth;
	public final ModelRenderer happyFace;
	public final ModelRenderer sadFace;

	public ModelTeruTeruBozu() {
		super(RenderType::getEntityCutoutNoCull);
		textureWidth = 64;
		textureHeight = 32;
		sadFace = new ModelRenderer(this, 32, 0);
		sadFace.setRotationPoint(0.0F, 14.5F, 0.0F);
		sadFace.addBox(-4.0F, -6.0F, -4.0F, 8, 8, 8, 0.0F);
		setRotateAngle(sadFace, 0.17453292519943295F, 0.0F, 0.0F);
		happyFace = new ModelRenderer(this, 0, 0);
		happyFace.setRotationPoint(0.0F, 14.5F, 0.0F);
		happyFace.addBox(-4.0F, -6.0F, -4.0F, 8, 8, 8, 0.0F);
		setRotateAngle(happyFace, -0.17453292519943295F, 0.0F, 0.0F);
		thread = new ModelRenderer(this, 32, 16);
		thread.setRotationPoint(0.0F, 14.0F, 0.0F);
		thread.addBox(-3.0F, 2.0F, -3.0F, 6, 1, 6, 0.0F);
		cloth = new ModelRenderer(this, 0, 16);
		cloth.setRotationPoint(0.0F, 21.5F, -1.0F);
		cloth.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F);
		setRotateAngle(cloth, 0.7853981633974483F, 2.2689280275926285F, 1.5707963267948966F);
	}

	@Override
	public void render(MatrixStack ms, IVertexBuilder buffer, int light, int overlay, float r, float g, float b, float a) {
		if (Minecraft.getInstance().world.isRaining()) {
			sadFace.render(ms, buffer, light, overlay, r, g, b, a);
		} else {
			happyFace.render(ms, buffer, light, overlay, r, g, b, a);
		}
		thread.render(ms, buffer, light, overlay, r, g, b, a);
		cloth.render(ms, buffer, light, overlay, r, g, b, a);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
