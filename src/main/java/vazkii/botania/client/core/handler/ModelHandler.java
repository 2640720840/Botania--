/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.FloatingFlowerModel;
import vazkii.botania.client.render.tile.*;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.lib.LibMisc;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class ModelHandler {
	static boolean registeredModels = false;

	public static void registerModels(ModelRegistryEvent evt) {
		if (!registeredModels) {
			registeredModels = true;
			ModelLoaderRegistry.registerLoader(FloatingFlowerModel.Loader.ID, FloatingFlowerModel.Loader.INSTANCE);
		}
		ModelLoader.addSpecialModel(new ModelResourceLocation(LibMisc.MOD_ID + ":mana_gun_clip", "inventory"));
		ModelLoader.addSpecialModel(new ModelResourceLocation(LibMisc.MOD_ID + ":desu_gun", "inventory"));
		ModelLoader.addSpecialModel(new ModelResourceLocation(LibMisc.MOD_ID + ":desu_gun_clip", "inventory"));
		ModelLoader.addSpecialModel(prefix("block/corporea_crystal_cube_glass"));
		ModelLoader.addSpecialModel(prefix("block/pump_head"));
		ModelLoader.addSpecialModel(prefix("block/elven_spreader_inside"));
		ModelLoader.addSpecialModel(prefix("block/gaia_spreader_inside"));
		ModelLoader.addSpecialModel(prefix("block/mana_spreader_inside"));
		ModelLoader.addSpecialModel(prefix("block/redstone_spreader_inside"));
		registerIslands();
		registerTaters();

		ClientRegistry.bindTileEntityRenderer(ModTiles.ALTAR, RenderTileAltar::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.SPREADER, RenderTileSpreader::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.POOL, RenderTilePool::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.RUNE_ALTAR, RenderTileRuneAltar::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.PYLON, RenderTilePylon::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.ENCHANTER, RenderTileEnchanter::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.ALF_PORTAL, RenderTileAlfPortal::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.MINI_ISLAND, RenderTileFloatingFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.TINY_POTATO, RenderTileTinyPotato::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.STARFIELD, RenderTileStarfield::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.BREWERY, RenderTileBrewery::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.TERRA_PLATE, RenderTileTerraPlate::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.RED_STRING_COMPARATOR, RenderTileRedString::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.RED_STRING_CONTAINER, RenderTileRedString::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.RED_STRING_DISPENSER, RenderTileRedString::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.RED_STRING_FERTILIZER, RenderTileRedString::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.RED_STRING_INTERCEPTOR, RenderTileRedString::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.RED_STRING_RELAY, RenderTileRedString::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.PRISM, RenderTilePrism::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.CORPOREA_INDEX, RenderTileCorporeaIndex::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.PUMP, RenderTilePump::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.CORPOREA_CRYSTAL_CUBE, RenderTileCorporeaCrystalCube::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.INCENSE_PLATE, RenderTileIncensePlate::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.HOURGLASS, RenderTileHourglass::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.SPARK_CHANGER, RenderTileSparkChanger::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.COCOON, RenderTileCocoon::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.LIGHT_RELAY, RenderTileLightRelay::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.BELLOWS, RenderTileBellows::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.GAIA_HEAD, RenderTileGaiaHead::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.TERU_TERU_BOZU, RenderTileTeruTeruBozu::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.AVATAR, RenderTileAvatar::new);
		ClientRegistry.bindTileEntityRenderer(ModTiles.ANIMATED_TORCH, RenderTileAnimatedTorch::new);

		ClientRegistry.bindTileEntityRenderer(ModSubtiles.PURE_DAISY, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.MANASTAR, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.HYDROANGEAS, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.ENDOFLAME, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.THERMALILY, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.ROSA_ARCANA, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.MUNCHDEW, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.ENTROPINNYUM, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.KEKIMURUS, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.GOURMARYLLIS, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.NARSLIMMUS, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.SPECTROLUS, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.DANDELIFEON, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.RAFFLOWSIA, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.SHULK_ME_NOT, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.BELLETHORNE, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.BELLETHORNE_CHIBI, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.BERGAMUTE, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.DREADTHORN, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.HEISEI_DREAM, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.TIGERSEYE, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.JADED_AMARANTHUS, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.ORECHID, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.FALLEN_KANADE, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.EXOFLAME, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.AGRICARNATION, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.AGRICARNATION_CHIBI, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.HOPPERHOCK, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.HOPPERHOCK_CHIBI, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.TANGLEBERRIE, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.TANGLEBERRIE_CHIBI, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.JIYUULIA, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.JIYUULIA_CHIBI, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.RANNUNCARPUS, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.RANNUNCARPUS_CHIBI, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.HYACIDUS, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.POLLIDISIAC, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.CLAYCONIA, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.CLAYCONIA_CHIBI, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.LOONIUM, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.DAFFOMILL, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.VINCULOTUS, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.SPECTRANTHEMUM, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.MEDUMONE, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.MARIMORPHOSIS, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.MARIMORPHOSIS_CHIBI, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.BUBBELL, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.BUBBELL_CHIBI, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.SOLEGNOLIA, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.SOLEGNOLIA_CHIBI, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.ORECHID_IGNEM, RenderTileSpecialFlower::new);
		ClientRegistry.bindTileEntityRenderer(ModSubtiles.LABELLIA, RenderTileSpecialFlower::new);
	}

	private static void registerIslands() {
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.GRASS, prefix("block/islands/island_grass"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.PODZOL, prefix("block/islands/island_podzol"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.MYCEL, prefix("block/islands/island_mycel"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.SNOW, prefix("block/islands/island_snow"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.DRY, prefix("block/islands/island_dry"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.GOLDEN, prefix("block/islands/island_golden"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.VIVID, prefix("block/islands/island_vivid"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.SCORCHED, prefix("block/islands/island_scorched"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.INFUSED, prefix("block/islands/island_infused"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.MUTATED, prefix("block/islands/island_mutated"));
	}

	private static void registerTaters() {
		IResourceManager rm = Minecraft.getInstance().getResourceManager();
		for (ResourceLocation model : rm.getAllResourceLocations(LibResources.PREFIX_MODELS + LibResources.PREFIX_TINY_POTATO, s -> s.endsWith(LibResources.ENDING_JSON))) {
			if (LibMisc.MOD_ID.equals(model.getNamespace())) {
				String path = model.getPath();
				path = path.substring(LibResources.PREFIX_MODELS.length(), path.length() - LibResources.ENDING_JSON.length());
				ModelLoader.addSpecialModel(new ResourceLocation(LibMisc.MOD_ID, path));
			}
		}
	}

	private ModelHandler() {}
}
