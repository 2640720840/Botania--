/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.armor.terrasteel;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyValue;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.armor.ModelArmorTerrasteel;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.UUID;

public class ItemTerrasteelArmor extends ItemManasteelArmor {

	public ItemTerrasteelArmor(EquipmentSlotType type, Properties props) {
		super(type, BotaniaAPI.instance().getTerrasteelArmorMaterial(), props);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BipedModel<?> provideArmorModelForSlot(EquipmentSlotType slot) {
		return new ModelArmorTerrasteel(slot);
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlotType slot) {
		return LibResources.MODEL_TERRASTEEL_NEW;
	}

	@Nonnull
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlotType slot) {
		Multimap<Attribute, AttributeModifier> ret = super.getAttributeModifiers(slot);
		UUID uuid = new UUID(Registry.ITEM.getKey(this).hashCode() + slot.toString().hashCode(), 0);
		if (slot == getEquipmentSlot()) {
			ret = HashMultimap.create(ret);
			int reduction = material.getDamageReductionAmount(slot);
			ret.put(Attributes.KNOCKBACK_RESISTANCE,
					new AttributeModifier(uuid, "Terrasteel modifier " + type, (double) reduction / 20, AttributeModifier.Operation.ADDITION));
		}
		return ret;
	}

	private static final LazyValue<ItemStack[]> armorSet = new LazyValue<>(() -> new ItemStack[] {
			new ItemStack(ModItems.terrasteelHelm),
			new ItemStack(ModItems.terrasteelChest),
			new ItemStack(ModItems.terrasteelLegs),
			new ItemStack(ModItems.terrasteelBoots)
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
			return stack.getItem() == ModItems.terrasteelHelm;
		case CHEST:
			return stack.getItem() == ModItems.terrasteelChest;
		case LEGS:
			return stack.getItem() == ModItems.terrasteelLegs;
		case FEET:
			return stack.getItem() == ModItems.terrasteelBoots;
		}

		return false;
	}

	@Override
	public IFormattableTextComponent getArmorSetName() {
		return new TranslationTextComponent("botania.armorset.terrasteel.name");
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addArmorSetDescription(ItemStack stack, List<ITextComponent> list) {
		list.add(new TranslationTextComponent("botania.armorset.terrasteel.desc0").mergeStyle(TextFormatting.GRAY));
		list.add(new TranslationTextComponent("botania.armorset.terrasteel.desc1").mergeStyle(TextFormatting.GRAY));
		list.add(new TranslationTextComponent("botania.armorset.terrasteel.desc2").mergeStyle(TextFormatting.GRAY));
	}

	@Override
	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
		return true;
	}
}
