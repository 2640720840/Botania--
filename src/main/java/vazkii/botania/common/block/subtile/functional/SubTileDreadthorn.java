/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;

import vazkii.botania.common.block.ModSubtiles;

import java.util.function.Predicate;

public class SubTileDreadthorn extends SubTileBellethorn {
	public SubTileDreadthorn() {
		super(ModSubtiles.DREADTHORN);
	}

	@Override
	public int getColor() {
		return 0x260B45;
	}

	@Override
	public Predicate<Entity> getSelector() {
		return var1 -> var1 instanceof AnimalEntity && !((AnimalEntity) var1).isChild();
	}

	@Override
	public int getManaCost() {
		return 30;
	}

}
