/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.item.ItemStack;

/**
 * Have an Item implement this to be counted as a lens that can be combined with an ItemLens.
 */
public interface ICompositableLens extends ILens {

	/**
	 * Returns the properties of the itemstack, used to check if two lenses can combine.
	 */
	int getProps(ItemStack stack);

	/**
	 * Checks if the lens is combinable.
	 */
	boolean isCombinable(ItemStack stack);

}
