/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model.armor;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;

public class ModelArmorManaweave extends ModelArmor {

	private final ModelRenderer helmAnchor;
	private final ModelRenderer helm;

	private final ModelRenderer bodyAnchor;
	private final ModelRenderer bodyTop;
	private final ModelRenderer bodyBottom;

	private final ModelRenderer armLAnchor;
	private final ModelRenderer armL;
	private final ModelRenderer armLpauldron;
	private final ModelRenderer armRAnchor;
	private final ModelRenderer armR;
	private final ModelRenderer armRpauldron;

	private final ModelRenderer pantsAnchor;
	private final ModelRenderer legL;
	private final ModelRenderer skirtL;
	private final ModelRenderer legR;
	private final ModelRenderer skirtR;

	private final ModelRenderer bootL;
	private final ModelRenderer bootR;

	public ModelArmorManaweave(EquipmentSlotType slot) {
		super(slot);

		textureWidth = 64;
		textureHeight = 128;
		float s = 0.01F;

		//helm
		this.helmAnchor = new ModelRenderer(this, 0, 0);
		this.helmAnchor.addBox(-1.0F, -2.0F, 0.0F, 2, 2, 2, s);
		this.helm = new ModelRenderer(this, 0, 0);
		this.helm.addBox(-4.5F, -9.5F, -4.0F, 9, 11, 10, s);
		this.setRotateAngle(helm, 0.17453292519943295F, 0.0F, 0.0F);

		//body
		this.bodyAnchor = new ModelRenderer(this, 0, 0);
		this.bodyAnchor.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
		this.bodyTop = new ModelRenderer(this, 0, 21);
		this.bodyTop.addBox(-4.5F, -0.5F, -3.0F, 9, 7, 6, s);
		this.setRotateAngle(bodyTop, 0.0F, 0.0F, 0.0F);
		this.bodyBottom = new ModelRenderer(this, 0, 34);
		this.bodyBottom.addBox(-4.5F, 6.5F, -2.5F, 9, 5, 5, s);
		this.setRotateAngle(bodyBottom, -0F, 0.0F, 0.0F);

		//armL
		this.armLAnchor = new ModelRenderer(this, 0, 0);
		this.armLAnchor.mirror = true;
		this.armLAnchor.addBox(0.0F, -1.0F, -1.0F, 2, 2, 2, s);
		this.armL = new ModelRenderer(this, 0, 44);
		this.armL.mirror = true;
		this.armL.addBox(-1.5F, -2.5F, -2.49F, 5, 10, 5, s);
		this.armLpauldron = new ModelRenderer(this, 20, 44);
		this.armLpauldron.mirror = true;
		this.armLpauldron.addBox(-1.0F, -3.0F, -3.0F, 6, 5, 6, s);
		this.setRotateAngle(armLpauldron, 0.0F, 0.0F, -0.08726646259971647F);

		//armR
		this.armRAnchor = new ModelRenderer(this, 0, 0);
		this.armRAnchor.mirror = true;
		this.armRAnchor.addBox(-2.0F, -1.0F, -1.0F, 2, 2, 2, s);
		this.armR = new ModelRenderer(this, 0, 44);
		this.armR.addBox(-3.5F, -2.5F, -2.51F, 5, 10, 5, s);
		this.armRpauldron = new ModelRenderer(this, 20, 44);
		this.armRpauldron.addBox(-5.0F, -3.0F, -3.0F, 6, 5, 6, s);
		this.setRotateAngle(armRpauldron, 0.0F, 0.0F, 0.08726646259971647F);

		//pants
		this.pantsAnchor = new ModelRenderer(this, 0, 0);
		this.pantsAnchor.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
		this.legL = new ModelRenderer(this, 0, 78);
		this.legL.mirror = true;
		this.legL.addBox(-2.39F, -0.5F, -2.49F, 5, 6, 5, s);
		this.skirtL = new ModelRenderer(this, 0, 59);
		this.skirtL.mirror = true;
		this.skirtL.setRotationPoint(-0.5F, -2.0F, -2.5F);
		this.skirtL.addBox(-1.0F, 0.0F, -0.5F, 5, 13, 6, s);
		this.setRotateAngle(skirtL, 0.0F, -0.17453292519943295F, -0.2617993877991494F);
		this.legR = new ModelRenderer(this, 0, 78);
		this.legR.addBox(-2.61F, 0.0F, -2.51F, 5, 6, 5, s);
		this.skirtR = new ModelRenderer(this, 0, 59);
		this.skirtR.setRotationPoint(0.5F, -2.0F, -2.5F);
		this.skirtR.addBox(-4.0F, 0.0F, -0.5F, 5, 13, 6, s);
		this.setRotateAngle(skirtR, 0.0F, 0.17453292519943295F, 0.2617993877991494F);

		//boot
		this.bootL = new ModelRenderer(this, 0, 89);
		this.bootL.mirror = true;
		this.bootL.addBox(-2.39F, 8.5F, -2.49F, 5, 4, 5, s);
		this.bootR = new ModelRenderer(this, 0, 89);
		this.bootR.addBox(-2.61F, 8.5F, -2.51F, 5, 4, 5, s);

		//hierarchy
		this.helmAnchor.addChild(this.helm);

		this.bodyAnchor.addChild(this.bodyTop);
		this.bodyTop.addChild(this.bodyBottom);

		this.armLAnchor.addChild(this.armL);
		this.armL.addChild(this.armLpauldron);

		this.armRAnchor.addChild(this.armR);
		this.armR.addChild(this.armRpauldron);

		this.legL.addChild(this.skirtL);
		this.legR.addChild(this.skirtR);

		helmAnchor.showModel = slot == EquipmentSlotType.HEAD;
		bodyAnchor.showModel = slot == EquipmentSlotType.CHEST;
		armRAnchor.showModel = slot == EquipmentSlotType.CHEST;
		armLAnchor.showModel = slot == EquipmentSlotType.CHEST;
		legR.showModel = slot == EquipmentSlotType.LEGS;
		legL.showModel = slot == EquipmentSlotType.LEGS;
		bootL.showModel = slot == EquipmentSlotType.FEET;
		bootR.showModel = slot == EquipmentSlotType.FEET;
		bipedHeadwear.showModel = false;

		bipedHead.addChild(helmAnchor);
		bipedBody.addChild(bodyAnchor);
		bipedRightArm.addChild(armRAnchor);
		bipedLeftArm.addChild(armLAnchor);
		bipedRightLeg.addChild(legR);
		bipedLeftLeg.addChild(legL);
		bipedRightLeg.addChild(bootR);
		bipedLeftLeg.addChild(bootL);
	}
}
