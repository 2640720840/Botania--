/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.core.helper.ColorHelper;

public class PaintableData {
	public static void init() {
		BotaniaAPI.instance().registerPaintableBlock(Blocks.GLASS, ColorHelper.STAINED_GLASS_MAP);
		for (DyeColor color : DyeColor.values()) {
			BotaniaAPI.instance().registerPaintableBlock(ColorHelper.STAINED_GLASS_MAP.apply(color), ColorHelper.STAINED_GLASS_MAP);
		}

		BotaniaAPI.instance().registerPaintableBlock(Blocks.GLASS_PANE, ColorHelper.STAINED_GLASS_PANE_MAP);
		for (DyeColor color : DyeColor.values()) {
			BotaniaAPI.instance().registerPaintableBlock(ColorHelper.STAINED_GLASS_PANE_MAP.apply(color), ColorHelper.STAINED_GLASS_PANE_MAP);
		}

		BotaniaAPI.instance().registerPaintableBlock(Blocks.TERRACOTTA, ColorHelper.TERRACOTTA_MAP);
		for (DyeColor color : DyeColor.values()) {
			BotaniaAPI.instance().registerPaintableBlock(ColorHelper.TERRACOTTA_MAP.apply(color), ColorHelper.TERRACOTTA_MAP);
		}

		for (DyeColor color : DyeColor.values()) {
			BotaniaAPI.instance().registerPaintableBlock(ColorHelper.GLAZED_TERRACOTTA_MAP.apply(color), ColorHelper.GLAZED_TERRACOTTA_MAP);
		}

		for (DyeColor color : DyeColor.values()) {
			BotaniaAPI.instance().registerPaintableBlock(ColorHelper.WOOL_MAP.apply(color), ColorHelper.WOOL_MAP);
		}

		for (DyeColor color : DyeColor.values()) {
			BotaniaAPI.instance().registerPaintableBlock(ColorHelper.CARPET_MAP.apply(color), ColorHelper.CARPET_MAP);
		}

		for (DyeColor color : DyeColor.values()) {
			BotaniaAPI.instance().registerPaintableBlock(ColorHelper.CONCRETE_MAP.apply(color), ColorHelper.CONCRETE_MAP);
		}

		for (DyeColor color : DyeColor.values()) {
			BotaniaAPI.instance().registerPaintableBlock(ColorHelper.CONCRETE_POWDER_MAP.apply(color), ColorHelper.CONCRETE_POWDER_MAP);
		}
	}
}
