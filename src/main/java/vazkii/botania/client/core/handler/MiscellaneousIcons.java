/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.tileentity.BannerPattern;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;

import vazkii.botania.client.model.GunModel;
import vazkii.botania.client.model.LexiconModel;
import vazkii.botania.client.model.PlatformModel;
import vazkii.botania.client.model.TinyPotatoModel;
import vazkii.botania.client.render.tile.RenderTileCorporeaCrystalCube;
import vazkii.botania.client.render.tile.RenderTilePump;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.relic.ItemKingKey;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.mixin.AccessorItemOverrideList;
import vazkii.botania.mixin.AccessorModelBakery;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class MiscellaneousIcons {
	public static final MiscellaneousIcons INSTANCE = new MiscellaneousIcons();

	public final RenderMaterial alfPortalTex = mainAtlas("block/alfheim_portal_swirl");
	public final RenderMaterial lightRelayWorldIcon = mainAtlas("block/light_relay");
	public final RenderMaterial lightRelayDetectorWorldIcon = mainAtlas("block/detector_light_relay");
	public final RenderMaterial lightRelayForkWorldIcon = mainAtlas("block/fork_light_relay");
	public final RenderMaterial lightRelayToggleWorldIcon = mainAtlas("block/toggle_light_relay");
	public final RenderMaterial alchemyCatalystOverlay = mainAtlas("block/alchemy_catalyst_overlay");
	public final RenderMaterial conjurationCatalystOverlay = mainAtlas("block/conjuration_catalyst_overlay");
	public final RenderMaterial enchanterOverlay = mainAtlas("block/enchanter_overlay");
	public final RenderMaterial manaVoidOverlay = mainAtlas("block/mana_void_overlay");
	public final RenderMaterial manaWater = mainAtlas("block/mana_water");
	public final RenderMaterial terraPlateOverlay = mainAtlas("block/terra_plate_overlay");
	public final RenderMaterial corporeaWorldIcon = mainAtlas("item/corporea_spark");
	public final RenderMaterial corporeaWorldIconMaster = mainAtlas("item/corporea_spark_master");
	public final RenderMaterial corporeaIconStar = mainAtlas("item/corporea_spark_star");
	public final RenderMaterial sparkWorldIcon = mainAtlas("item/spark");

	public final RenderMaterial[] sparkUpgradeIcons = new RenderMaterial[] {
			mainAtlas("item/spark_upgrade_rune_dispersive"),
			mainAtlas("item/spark_upgrade_rune_dominant"),
			mainAtlas("item/spark_upgrade_rune_recessive"),
			mainAtlas("item/spark_upgrade_rune_isolated")
	};
	public final IBakedModel[] tiaraWingIcons = new IBakedModel[ItemFlightTiara.WING_TYPES];
	public final IBakedModel[] thirdEyeLayers = new IBakedModel[3];

	public IBakedModel goldfishModel,
			phiFlowerModel,
			nerfBatModel,
			bloodPendantChain,
			bloodPendantGem,
			snowflakePendantGem,
			itemFinderGem,
			pyroclastGem,
			crimsonGem,
			cirrusGem,
			nimbusGem,
			terrasteelHelmWillModel,
			elvenSpreaderInside,
			gaiaSpreaderInside,
			manaSpreaderInside,
			redstoneSpreaderInside;

	public final IBakedModel[] kingKeyWeaponModels = new IBakedModel[ItemKingKey.WEAPON_TYPES];

	public void onModelRegister(ModelRegistryEvent evt) {
		Set<RenderMaterial> materials = AccessorModelBakery.getMaterials();

		materials.addAll(Arrays.asList(alfPortalTex, lightRelayWorldIcon, lightRelayDetectorWorldIcon,
				lightRelayForkWorldIcon, lightRelayToggleWorldIcon, alchemyCatalystOverlay, conjurationCatalystOverlay,
				enchanterOverlay, manaVoidOverlay, manaWater, terraPlateOverlay, corporeaWorldIcon, corporeaWorldIconMaster,
				corporeaIconStar, sparkWorldIcon));
		materials.addAll(Arrays.asList(sparkUpgradeIcons));
		materials.add(RenderLexicon.TEXTURE);
		materials.add(RenderLexicon.ELVEN_TEXTURE);
		for (BannerPattern pattern : BannerPattern.values()) {
			if (pattern.getFileName().startsWith(LibMisc.MOD_ID)) {
				materials.add(new RenderMaterial(Atlases.SHIELD_ATLAS, pattern.getTextureLocation(false)));
				materials.add(new RenderMaterial(Atlases.BANNER_ATLAS, pattern.getTextureLocation(true)));
			}
		}
		ModelLoader.addSpecialModel(prefix("icon/goldfish"));
		ModelLoader.addSpecialModel(prefix("icon/phiflower"));
		ModelLoader.addSpecialModel(prefix("icon/nerfbat"));
		ModelLoader.addSpecialModel(prefix("icon/blood_pendant_chain"));
		ModelLoader.addSpecialModel(prefix("icon/blood_pendant_gem"));
		for (int i = 0; i < ItemKingKey.WEAPON_TYPES; i++) {
			ModelLoader.addSpecialModel(prefix("icon/gate_weapon_" + i));
		}
		ModelLoader.addSpecialModel(prefix("icon/will_flame"));
		for (int i = 0; i < thirdEyeLayers.length; i++) {
			ModelLoader.addSpecialModel(prefix("icon/third_eye_" + i));
		}
		ModelLoader.addSpecialModel(prefix("icon/lava_pendant_gem"));
		ModelLoader.addSpecialModel(prefix("icon/super_lava_pendant_gem"));
		ModelLoader.addSpecialModel(prefix("icon/itemfinder_gem"));
		ModelLoader.addSpecialModel(prefix("icon/cloud_pendant_gem"));
		ModelLoader.addSpecialModel(prefix("icon/super_cloud_pendant_gem"));
		ModelLoader.addSpecialModel(prefix("icon/ice_pendant_gem"));
		for (int i = 0; i < tiaraWingIcons.length; i++) {
			ModelLoader.addSpecialModel(prefix("icon/tiara_wing_" + (i + 1)));
		}
	}

	public void onModelBake(ModelBakeEvent evt) {
		if (!ModelHandler.registeredModels) {
			Botania.LOGGER.error("Additional models failed to register! Aborting baking models to avoid early crashing.");
			return;
		}
		// Platforms
		ModelResourceLocation abstruseName = new ModelResourceLocation("botania:abstruse_platform", "");
		IBakedModel abstruse = evt.getModelRegistry().get(abstruseName);
		ModelResourceLocation spectralName = new ModelResourceLocation("botania:spectral_platform", "");
		IBakedModel spectral = evt.getModelRegistry().get(spectralName);
		ModelResourceLocation infrangibleName = new ModelResourceLocation("botania:infrangible_platform", "");
		IBakedModel infrangible = evt.getModelRegistry().get(infrangibleName);

		evt.getModelRegistry().put(abstruseName, new PlatformModel(abstruse));
		evt.getModelRegistry().put(spectralName, new PlatformModel(spectral));
		evt.getModelRegistry().put(infrangibleName, new PlatformModel(infrangible));

		// Lexicon
		IBakedModel original = evt.getModelRegistry().get(new ModelResourceLocation("botania:lexicon", "inventory"));
		evt.getModelRegistry().put(new ModelResourceLocation("botania:lexicon", "inventory"),
				new LexiconModel(original));

		// models referenced using json overrides aren't put in the model registry, so just go through all override models and wrap them there
		List<IBakedModel> overrides = ((AccessorItemOverrideList) original.getOverrides()).getOverrideBakedModels();
		for (int i = 0; i < overrides.size(); i++) {
			overrides.set(i, new LexiconModel(overrides.get(i)));
		}

		// Mana Blaster
		ModelResourceLocation key = new ModelResourceLocation("botania:mana_gun", "inventory");
		IBakedModel originalModel = evt.getModelRegistry().get(key);
		ModelResourceLocation clipKey = new ModelResourceLocation("botania:mana_gun_clip", "inventory");
		IBakedModel originalModelClip = evt.getModelRegistry().get(clipKey);
		evt.getModelRegistry().put(key, new GunModel(evt.getModelLoader(), originalModel, originalModelClip));

		// Tiny Potato
		ModelResourceLocation tinyPotato = new ModelResourceLocation("botania:tiny_potato", "inventory");
		IBakedModel originalPotato = evt.getModelRegistry().get(tinyPotato);
		evt.getModelRegistry().put(tinyPotato, new TinyPotatoModel(originalPotato));

		RenderTileCorporeaCrystalCube.cubeModel = evt.getModelRegistry().get(prefix("block/corporea_crystal_cube_glass"));
		RenderTilePump.headModel = evt.getModelRegistry().get(prefix("block/pump_head"));
		elvenSpreaderInside = evt.getModelRegistry().get(prefix("block/elven_spreader_inside"));
		gaiaSpreaderInside = evt.getModelRegistry().get(prefix("block/gaia_spreader_inside"));
		manaSpreaderInside = evt.getModelRegistry().get(prefix("block/mana_spreader_inside"));
		redstoneSpreaderInside = evt.getModelRegistry().get(prefix("block/redstone_spreader_inside"));

		// Icons
		goldfishModel = evt.getModelRegistry().get(prefix("icon/goldfish"));
		phiFlowerModel = evt.getModelRegistry().get(prefix("icon/phiflower"));
		nerfBatModel = evt.getModelRegistry().get(prefix("icon/nerfbat"));
		bloodPendantChain = evt.getModelRegistry().get(prefix("icon/blood_pendant_chain"));
		bloodPendantGem = evt.getModelRegistry().get(prefix("icon/blood_pendant_gem"));
		for (int i = 0; i < ItemKingKey.WEAPON_TYPES; i++) {
			kingKeyWeaponModels[i] = evt.getModelRegistry().get(prefix("icon/gate_weapon_" + i));
		}
		terrasteelHelmWillModel = evt.getModelRegistry().get(prefix("icon/will_flame"));
		for (int i = 0; i < thirdEyeLayers.length; i++) {
			thirdEyeLayers[i] = evt.getModelRegistry().get(prefix("icon/third_eye_" + i));
		}
		pyroclastGem = evt.getModelRegistry().get(prefix("icon/lava_pendant_gem"));
		crimsonGem = evt.getModelRegistry().get(prefix("icon/super_lava_pendant_gem"));
		itemFinderGem = evt.getModelRegistry().get(prefix("icon/itemfinder_gem"));

		cirrusGem = evt.getModelRegistry().get(prefix("icon/cloud_pendant_gem"));
		nimbusGem = evt.getModelRegistry().get(prefix("icon/super_cloud_pendant_gem"));
		snowflakePendantGem = evt.getModelRegistry().get(prefix("icon/ice_pendant_gem"));
		for (int i = 0; i < tiaraWingIcons.length; i++) {
			tiaraWingIcons[i] = evt.getModelRegistry().get(prefix("icon/tiara_wing_" + (i + 1)));
		}
	}

	private static RenderMaterial mainAtlas(String name) {
		return new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, prefix(name));
	}

	private MiscellaneousIcons() {}
}
