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

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelAvatar extends Model {

	public final ModelRenderer body;
	public final ModelRenderer rightarm;
	public final ModelRenderer leftarm;
	public final ModelRenderer rightleg;
	public final ModelRenderer leftleg;
	public final ModelRenderer head;

	public ModelAvatar() {
		super(RenderType::getEntitySolid);
		textureWidth = 32;
		textureHeight = 32;
		leftleg = new ModelRenderer(this, 0, 20);
		leftleg.mirror = true;
		leftleg.setRotationPoint(1.5F, 18.0F, -0.5F);
		leftleg.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
		rightarm = new ModelRenderer(this, 0, 20);
		rightarm.setRotationPoint(-3.0F, 15.0F, -1.0F);
		rightarm.addBox(-2.0F, -1.0F, -1.0F, 2, 6, 3, 0.0F);
		setRotateAngle(rightarm, 0.0F, -0.0F, 0.08726646259971647F);
		leftarm = new ModelRenderer(this, 0, 20);
		leftarm.mirror = true;
		leftarm.setRotationPoint(3.0F, 15.0F, -1.0F);
		leftarm.addBox(0.0F, -1.0F, -1.0F, 2, 6, 3, 0.0F);
		setRotateAngle(leftarm, 0.0F, -0.0F, -0.08726646259971647F);
		head = new ModelRenderer(this, 0, 0);
		head.setRotationPoint(0.0F, 14.0F, 0.0F);
		head.addBox(-3.0F, -6.0F, -3.0F, 6, 6, 6, 0.0F);
		rightleg = new ModelRenderer(this, 0, 20);
		rightleg.setRotationPoint(-1.5F, 18.0F, -0.5F);
		rightleg.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
		body = new ModelRenderer(this, 0, 12);
		body.setRotationPoint(0.0F, 14.0F, 0.0F);
		body.addBox(-3.0F, 0.0F, -2.0F, 6, 4, 4, 0.0F);
	}

	@Override
	public void render(MatrixStack ms, IVertexBuilder buffer, int light, int overlay, float r, float g, float b, float a) {
		leftleg.render(ms, buffer, light, overlay, r, g, b, a);
		rightarm.render(ms, buffer, light, overlay, r, g, b, a);
		leftarm.render(ms, buffer, light, overlay, r, g, b, a);
		head.render(ms, buffer, light, overlay, r, g, b, a);
		rightleg.render(ms, buffer, light, overlay, r, g, b, a);
		body.render(ms, buffer, light, overlay, r, g, b, a);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

}
