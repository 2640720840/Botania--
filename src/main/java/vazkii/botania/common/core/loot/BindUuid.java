/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;

import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.relic.ItemRelic;

import javax.annotation.Nonnull;

public class BindUuid extends LootFunction {

	protected BindUuid(ILootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Nonnull
	@Override
	public ItemStack doApply(@Nonnull ItemStack stack, @Nonnull LootContext context) {
		if (context.get(LootParameters.KILLER_ENTITY) instanceof PlayerEntity) {
			((ItemRelic) ModItems.dice).bindToUUID(context.get(LootParameters.KILLER_ENTITY).getUniqueID(), stack);
		}

		return stack;
	}

	@Override
	public LootFunctionType getFunctionType() {
		return ModLootModifiers.BIND_UUID;
	}

	public static class Serializer extends LootFunction.Serializer<BindUuid> {
		@Nonnull
		@Override
		public BindUuid deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull ILootCondition[] conditionsIn) {
			return new BindUuid(conditionsIn);
		}
	}

}
