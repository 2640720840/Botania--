/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.patchouli.processor;

import net.minecraft.util.ResourceLocation;

import vazkii.botania.api.recipe.IRuneAltarRecipe;
import vazkii.botania.client.patchouli.PatchouliUtils;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class RunicAltarProcessor extends PetalApothecaryProcessor {
	@Override
	public void setup(IVariableProvider variables) {
		ResourceLocation id = new ResourceLocation(variables.get("recipe").asString());
		this.recipe = PatchouliUtils.getRecipe(ModRecipeTypes.RUNE_TYPE, id);
	}

	@Override
	public IVariable process(String key) {
		if (recipe == null) {
			return super.process(key);
		}
		if (key.equals("mana")) {
			return IVariable.wrap(((IRuneAltarRecipe) recipe).getManaUsage());
		}
		return super.process(key);
	}
}
