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
import com.google.gson.JsonSerializationContext;

import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;

import vazkii.botania.common.core.handler.ConfigHandler;

import javax.annotation.Nonnull;

public class EnableRelics implements ILootCondition {

	@Override
	public boolean test(@Nonnull LootContext context) {
		return ConfigHandler.COMMON.relicsEnabled.get();
	}

	@Override
	public LootConditionType func_230419_b_() {
		return ModLootModifiers.ENABLE_RELICS;
	}

	public static class Serializer implements ILootSerializer<EnableRelics> {
		@Override
		public void serialize(@Nonnull JsonObject json, @Nonnull EnableRelics value, @Nonnull JsonSerializationContext context) {}

		@Nonnull
		@Override
		public EnableRelics deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
			return new EnableRelics();
		}
	}

}
