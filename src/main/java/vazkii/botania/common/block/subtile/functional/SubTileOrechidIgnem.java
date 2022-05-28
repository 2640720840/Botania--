/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.OrechidOutput;
import vazkii.botania.common.block.ModSubtiles;

import java.util.List;
import java.util.function.Predicate;

public class SubTileOrechidIgnem extends SubTileOrechid {
	private static final int COST = 20000;

	public SubTileOrechidIgnem() {
		super(ModSubtiles.ORECHID_IGNEM);
	}

	@Override
	public boolean canOperate() {
		return getWorld().getDimensionType().getHasCeiling();
	}

	@Override
	public List<OrechidOutput> getOreList() {
		return BotaniaAPI.instance().getNetherOrechidWeights();
	}

	@Override
	public Predicate<BlockState> getReplaceMatcher() {
		return state -> state.getBlock() == Blocks.NETHERRACK;
	}

	@Override
	public int getCost() {
		return COST;
	}

	@Override
	public int getColor() {
		return 0xAE3030;
	}

}
