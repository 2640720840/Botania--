/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.elementium;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyValue;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.armor.ModelArmorElementium;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;

import java.util.List;

public abstract class ItemElementiumArmor extends ItemManasteelArmor {

	public ItemElementiumArmor(EquipmentSlotType type, Properties props) {
		super(type, BotaniaAPI.instance().getElementiumArmorMaterial(), props);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BipedModel<?> provideArmorModelForSlot(EquipmentSlotType slot) {
		return new ModelArmorElementium(slot);
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlotType slot) {
		return LibResources.MODEL_ELEMENTIUM_NEW;
	}

	private static final LazyValue<ItemStack[]> armorSet = new LazyValue<>(() -> new ItemStack[] {
			new ItemStack(ModItems.elementiumHelm),
			new ItemStack(ModItems.elementiumChest),
			new ItemStack(ModItems.elementiumLegs),
			new ItemStack(ModItems.elementiumBoots)
	});

	@Override
	public ItemStack[] getArmorSetStacks() {
		return armorSet.getValue();
	}

	@Override
	public boolean hasArmorSetItem(PlayerEntity player, EquipmentSlotType slot) {
		if (player == null) {
			return false;
		}

		ItemStack stack = player.getItemStackFromSlot(slot);
		if (stack.isEmpty()) {
			return false;
		}

		switch (slot) {
		case HEAD:
			return stack.getItem() == ModItems.elementiumHelm;
		case CHEST:
			return stack.getItem() == ModItems.elementiumChest;
		case LEGS:
			return stack.getItem() == ModItems.elementiumLegs;
		case FEET:
			return stack.getItem() == ModItems.elementiumBoots;
		}

		return false;
	}

	@Override
	public IFormattableTextComponent getArmorSetName() {
		return new TranslationTextComponent("botania.armorset.elementium.name");
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addArmorSetDescription(ItemStack stack, List<ITextComponent> list) {
		super.addArmorSetDescription(stack, list);
		list.add(new TranslationTextComponent("botania.armorset.elementium.desc").mergeStyle(TextFormatting.GRAY));
	}

}
