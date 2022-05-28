/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;

import vazkii.botania.common.Botania;
import vazkii.botania.common.block.subtile.SubTileManastar;
import vazkii.botania.common.block.subtile.SubTilePureDaisy;
import vazkii.botania.common.block.subtile.functional.*;
import vazkii.botania.common.block.subtile.generating.*;
import vazkii.botania.common.brew.ModPotions;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;
import java.util.function.Consumer;

import static vazkii.botania.common.block.ModBlocks.register;

public class ModSubtiles {
	private static final AbstractBlock.Properties FLOWER_PROPS = AbstractBlock.Properties.from(Blocks.POPPY);
	private static final AbstractBlock.Properties FLOATING_PROPS = ModBlocks.FLOATING_PROPS;

	public static final Block pureDaisy = new BlockSpecialFlower(ModPotions.clear, 1, FLOWER_PROPS, SubTilePureDaisy::new);
	public static final Block pureDaisyFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTilePureDaisy::new);

	public static final Block manastar = new BlockSpecialFlower(Effects.GLOWING, 10, FLOWER_PROPS, SubTileManastar::new);
	public static final Block manastarFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileManastar::new);

	public static final Block hydroangeas = new BlockSpecialFlower(Effects.UNLUCK, 10, FLOWER_PROPS, SubTileHydroangeas::new);
	public static final Block hydroangeasFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileHydroangeas::new);

	public static final Block endoflame = new BlockSpecialFlower(Effects.SLOWNESS, 10, FLOWER_PROPS, SubTileEndoflame::new);
	public static final Block endoflameFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileEndoflame::new);

	public static final Block thermalily = new BlockSpecialFlower(Effects.FIRE_RESISTANCE, 120, FLOWER_PROPS, SubTileThermalily::new);
	public static final Block thermalilyFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileThermalily::new);

	public static final Block rosaArcana = new BlockSpecialFlower(Effects.LUCK, 64, FLOWER_PROPS, SubTileArcaneRose::new);
	public static final Block rosaArcanaFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileArcaneRose::new);

	public static final Block munchdew = new BlockSpecialFlower(Effects.SLOW_FALLING, 300, FLOWER_PROPS, SubTileMunchdew::new);
	public static final Block munchdewFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileMunchdew::new);

	public static final Block entropinnyum = new BlockSpecialFlower(Effects.RESISTANCE, 72, FLOWER_PROPS, SubTileEntropinnyum::new);
	public static final Block entropinnyumFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileEntropinnyum::new);

	public static final Block kekimurus = new BlockSpecialFlower(Effects.SATURATION, 15, FLOWER_PROPS, SubTileKekimurus::new);
	public static final Block kekimurusFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileKekimurus::new);

	public static final Block gourmaryllis = new BlockSpecialFlower(Effects.HUNGER, 180, FLOWER_PROPS, SubTileGourmaryllis::new);
	public static final Block gourmaryllisFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileGourmaryllis::new);

	public static final Block narslimmus = new BlockSpecialFlower(ModPotions.featherfeet, 240, FLOWER_PROPS, SubTileNarslimmus::new);
	public static final Block narslimmusFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileNarslimmus::new);

	public static final Block spectrolus = new BlockSpecialFlower(Effects.BLINDNESS, 240, FLOWER_PROPS, SubTileSpectrolus::new);
	public static final Block spectrolusFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileSpectrolus::new);

	public static final Block dandelifeon = new BlockSpecialFlower(Effects.NAUSEA, 240, FLOWER_PROPS, SubTileDandelifeon::new);
	public static final Block dandelifeonFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileDandelifeon::new);

	public static final Block rafflowsia = new BlockSpecialFlower(Effects.HEALTH_BOOST, 18, FLOWER_PROPS, SubTileRafflowsia::new);
	public static final Block rafflowsiaFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileRafflowsia::new);

	public static final Block shulkMeNot = new BlockSpecialFlower(Effects.LEVITATION, 72, FLOWER_PROPS, SubTileShulkMeNot::new);
	public static final Block shulkMeNotFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileShulkMeNot::new);

	public static final Block bellethorn = new BlockSpecialFlower(Effects.WITHER, 10, FLOWER_PROPS, SubTileBellethorn::new);
	public static final Block bellethornChibi = new BlockSpecialFlower(Effects.WITHER, 10, FLOWER_PROPS, SubTileBellethorn.Mini::new);
	public static final Block bellethornFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileBellethorn::new);
	public static final Block bellethornChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileBellethorn.Mini::new);

	public static final Block bergamute = new BlockSpecialFlower(Effects.BLINDNESS, 10, FLOWER_PROPS, SubTileBergamute::new);
	public static final Block bergamuteFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileBergamute::new);

	public static final Block dreadthorn = new BlockSpecialFlower(Effects.WITHER, 10, FLOWER_PROPS, SubTileDreadthorn::new);
	public static final Block dreadthornFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileDreadthorn::new);

	public static final Block heiseiDream = new BlockSpecialFlower(ModPotions.soulCross, 300, FLOWER_PROPS, SubTileHeiseiDream::new);
	public static final Block heiseiDreamFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileHeiseiDream::new);

	public static final Block tigerseye = new BlockSpecialFlower(Effects.STRENGTH, 90, FLOWER_PROPS, SubTileTigerseye::new);
	public static final Block tigerseyeFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileTigerseye::new);

	public static final Block jadedAmaranthus = new BlockSpecialFlower(Effects.INSTANT_HEALTH, 1, FLOWER_PROPS, SubTileJadedAmaranthus::new);
	public static final Block jadedAmaranthusFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileJadedAmaranthus::new);

	public static final Block orechid = new BlockSpecialFlower(Effects.HASTE, 10, FLOWER_PROPS, SubTileOrechid::new);
	public static final Block orechidFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileOrechid::new);

	public static final Block fallenKanade = new BlockSpecialFlower(Effects.REGENERATION, 90, FLOWER_PROPS, SubTileFallenKanade::new);
	public static final Block fallenKanadeFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileFallenKanade::new);

	public static final Block exoflame = new BlockSpecialFlower(Effects.SPEED, 240, FLOWER_PROPS, SubTileExoflame::new);
	public static final Block exoflameFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileExoflame::new);

	public static final Block agricarnation = new BlockSpecialFlower(Effects.ABSORPTION, 48, FLOWER_PROPS, SubTileAgricarnation::new);
	public static final Block agricarnationChibi = new BlockSpecialFlower(Effects.ABSORPTION, 48, FLOWER_PROPS, SubTileAgricarnation.Mini::new);
	public static final Block agricarnationFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileAgricarnation::new);
	public static final Block agricarnationChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileAgricarnation.Mini::new);

	public static final Block hopperhock = new BlockSpecialFlower(Effects.SPEED, 30, FLOWER_PROPS, SubTileHopperhock::new);
	public static final Block hopperhockChibi = new BlockSpecialFlower(Effects.SPEED, 30, FLOWER_PROPS, SubTileHopperhock.Mini::new);
	public static final Block hopperhockFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileHopperhock::new);
	public static final Block hopperhockChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileHopperhock.Mini::new);

	public static final Block tangleberrie = new BlockSpecialFlower(ModPotions.bloodthrst, 120, FLOWER_PROPS, SubTileTangleberrie::new);
	public static final Block tangleberrieChibi = new BlockSpecialFlower(ModPotions.bloodthrst, 120, FLOWER_PROPS, SubTileTangleberrie.Mini::new);
	public static final Block tangleberrieFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileTangleberrie::new);
	public static final Block tangleberrieChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileTangleberrie.Mini::new);

	public static final Block jiyuulia = new BlockSpecialFlower(ModPotions.emptiness, 120, FLOWER_PROPS, SubTileJiyuulia::new);
	public static final Block jiyuuliaChibi = new BlockSpecialFlower(ModPotions.emptiness, 120, FLOWER_PROPS, SubTileJiyuulia.Mini::new);
	public static final Block jiyuuliaFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileJiyuulia::new);
	public static final Block jiyuuliaChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileJiyuulia.Mini::new);

	public static final Block rannuncarpus = new BlockSpecialFlower(Effects.JUMP_BOOST, 30, FLOWER_PROPS, SubTileRannuncarpus::new);
	public static final Block rannuncarpusChibi = new BlockSpecialFlower(Effects.JUMP_BOOST, 30, FLOWER_PROPS, SubTileRannuncarpus.Mini::new);
	public static final Block rannuncarpusFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileRannuncarpus::new);
	public static final Block rannuncarpusChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileRannuncarpus.Mini::new);

	public static final Block hyacidus = new BlockSpecialFlower(Effects.POISON, 48, FLOWER_PROPS, SubTileHyacidus::new);
	public static final Block hyacidusFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileHyacidus::new);

	public static final Block pollidisiac = new BlockSpecialFlower(Effects.HASTE, 369, FLOWER_PROPS, SubTilePollidisiac::new);
	public static final Block pollidisiacFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTilePollidisiac::new);

	public static final Block clayconia = new BlockSpecialFlower(Effects.WEAKNESS, 30, FLOWER_PROPS, SubTileClayconia::new);
	public static final Block clayconiaChibi = new BlockSpecialFlower(Effects.WEAKNESS, 30, FLOWER_PROPS, SubTileClayconia.Mini::new);
	public static final Block clayconiaFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileClayconia::new);
	public static final Block clayconiaChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileClayconia.Mini::new);

	public static final Block loonium = new BlockSpecialFlower(ModPotions.allure, 900, FLOWER_PROPS, SubTileLoonuim::new);
	public static final Block looniumFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileLoonuim::new);

	public static final Block daffomill = new BlockSpecialFlower(Effects.LEVITATION, 6, FLOWER_PROPS, SubTileDaffomill::new);
	public static final Block daffomillFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileDaffomill::new);

	public static final Block vinculotus = new BlockSpecialFlower(Effects.NIGHT_VISION, 900, FLOWER_PROPS, SubTileVinculotus::new);
	public static final Block vinculotusFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileVinculotus::new);

	public static final Block spectranthemum = new BlockSpecialFlower(Effects.INVISIBILITY, 360, FLOWER_PROPS, SubTileSpectranthemum::new);
	public static final Block spectranthemumFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileSpectranthemum::new);

	public static final Block medumone = new BlockSpecialFlower(Effects.SLOWNESS, 3600, FLOWER_PROPS, SubTileMedumone::new);
	public static final Block medumoneFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileMedumone::new);

	public static final Block marimorphosis = new BlockSpecialFlower(Effects.MINING_FATIGUE, 60, FLOWER_PROPS, SubTileMarimorphosis::new);
	public static final Block marimorphosisChibi = new BlockSpecialFlower(Effects.MINING_FATIGUE, 60, FLOWER_PROPS, SubTileMarimorphosis.Mini::new);
	public static final Block marimorphosisFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileMarimorphosis::new);
	public static final Block marimorphosisChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileMarimorphosis.Mini::new);

	public static final Block bubbell = new BlockSpecialFlower(Effects.WATER_BREATHING, 240, FLOWER_PROPS, SubTileBubbell::new);
	public static final Block bubbellChibi = new BlockSpecialFlower(Effects.WATER_BREATHING, 240, FLOWER_PROPS, SubTileBubbell.Mini::new);
	public static final Block bubbellFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileBubbell::new);
	public static final Block bubbellChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileBubbell.Mini::new);

	public static final Block solegnolia = new BlockSpecialFlower(Effects.INSTANT_DAMAGE, 1, FLOWER_PROPS, SubTileSolegnolia::new);
	public static final Block solegnoliaChibi = new BlockSpecialFlower(Effects.INSTANT_DAMAGE, 1, FLOWER_PROPS, SubTileSolegnolia.Mini::new);
	public static final Block solegnoliaFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileSolegnolia::new);
	public static final Block solegnoliaChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileSolegnolia.Mini::new);

	public static final Block orechidIgnem = new BlockSpecialFlower(Effects.FIRE_RESISTANCE, 600, FLOWER_PROPS, SubTileOrechidIgnem::new);
	public static final Block orechidIgnemFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileOrechidIgnem::new);

	public static final Block labellia = new BlockSpecialFlower(Effects.FIRE_RESISTANCE, 600, FLOWER_PROPS, SubTileLabellia::new);
	public static final Block labelliaFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, SubTileLabellia::new);

	public static final TileEntityType<SubTilePureDaisy> PURE_DAISY = TileEntityType.Builder.create(SubTilePureDaisy::new, pureDaisy, pureDaisyFloating).build(null);
	public static final TileEntityType<SubTileManastar> MANASTAR = TileEntityType.Builder.create(SubTileManastar::new, manastar, manastarFloating).build(null);
	public static final TileEntityType<SubTileHydroangeas> HYDROANGEAS = TileEntityType.Builder.create(SubTileHydroangeas::new, hydroangeas, hydroangeasFloating).build(null);
	public static final TileEntityType<SubTileEndoflame> ENDOFLAME = TileEntityType.Builder.create(SubTileEndoflame::new, endoflame, endoflameFloating).build(null);
	public static final TileEntityType<SubTileThermalily> THERMALILY = TileEntityType.Builder.create(SubTileThermalily::new, thermalily, thermalilyFloating).build(null);
	public static final TileEntityType<SubTileArcaneRose> ROSA_ARCANA = TileEntityType.Builder.create(SubTileArcaneRose::new, rosaArcana, rosaArcanaFloating).build(null);
	public static final TileEntityType<SubTileMunchdew> MUNCHDEW = TileEntityType.Builder.create(SubTileMunchdew::new, munchdew, munchdewFloating).build(null);
	public static final TileEntityType<SubTileEntropinnyum> ENTROPINNYUM = TileEntityType.Builder.create(SubTileEntropinnyum::new, entropinnyum, entropinnyumFloating).build(null);
	public static final TileEntityType<SubTileKekimurus> KEKIMURUS = TileEntityType.Builder.create(SubTileKekimurus::new, kekimurus, kekimurusFloating).build(null);
	public static final TileEntityType<SubTileGourmaryllis> GOURMARYLLIS = TileEntityType.Builder.create(SubTileGourmaryllis::new, gourmaryllis, gourmaryllisFloating).build(null);
	public static final TileEntityType<SubTileNarslimmus> NARSLIMMUS = TileEntityType.Builder.create(SubTileNarslimmus::new, narslimmus, narslimmusFloating).build(null);
	public static final TileEntityType<SubTileSpectrolus> SPECTROLUS = TileEntityType.Builder.create(SubTileSpectrolus::new, spectrolus, spectrolusFloating).build(null);
	public static final TileEntityType<SubTileDandelifeon> DANDELIFEON = TileEntityType.Builder.create(SubTileDandelifeon::new, dandelifeon, dandelifeonFloating).build(null);
	public static final TileEntityType<SubTileRafflowsia> RAFFLOWSIA = TileEntityType.Builder.create(SubTileRafflowsia::new, rafflowsia, rafflowsiaFloating).build(null);
	public static final TileEntityType<SubTileShulkMeNot> SHULK_ME_NOT = TileEntityType.Builder.create(SubTileShulkMeNot::new, shulkMeNot, shulkMeNotFloating).build(null);
	public static final TileEntityType<SubTileBellethorn> BELLETHORNE = TileEntityType.Builder.create(SubTileBellethorn::new, bellethorn, bellethornFloating).build(null);
	public static final TileEntityType<SubTileBellethorn.Mini> BELLETHORNE_CHIBI = TileEntityType.Builder.create(SubTileBellethorn.Mini::new, bellethornChibi, bellethornChibiFloating).build(null);
	public static final TileEntityType<SubTileBergamute> BERGAMUTE = TileEntityType.Builder.create(SubTileBergamute::new, bergamute, bergamuteFloating).build(null);
	public static final TileEntityType<SubTileDreadthorn> DREADTHORN = TileEntityType.Builder.create(SubTileDreadthorn::new, dreadthorn, dreadthornFloating).build(null);
	public static final TileEntityType<SubTileHeiseiDream> HEISEI_DREAM = TileEntityType.Builder.create(SubTileHeiseiDream::new, heiseiDream, heiseiDreamFloating).build(null);
	public static final TileEntityType<SubTileTigerseye> TIGERSEYE = TileEntityType.Builder.create(SubTileTigerseye::new, tigerseye, tigerseyeFloating).build(null);
	public static final TileEntityType<SubTileJadedAmaranthus> JADED_AMARANTHUS = TileEntityType.Builder.create(SubTileJadedAmaranthus::new, jadedAmaranthus, jadedAmaranthusFloating).build(null);
	public static final TileEntityType<SubTileOrechid> ORECHID = TileEntityType.Builder.create(SubTileOrechid::new, orechid, orechidFloating).build(null);
	public static final TileEntityType<SubTileFallenKanade> FALLEN_KANADE = TileEntityType.Builder.create(SubTileFallenKanade::new, fallenKanade, fallenKanadeFloating).build(null);
	public static final TileEntityType<SubTileExoflame> EXOFLAME = TileEntityType.Builder.create(SubTileExoflame::new, exoflame, exoflameFloating).build(null);
	public static final TileEntityType<SubTileAgricarnation> AGRICARNATION = TileEntityType.Builder.create(SubTileAgricarnation::new, agricarnation, agricarnationFloating).build(null);
	public static final TileEntityType<SubTileAgricarnation.Mini> AGRICARNATION_CHIBI = TileEntityType.Builder.create(SubTileAgricarnation.Mini::new, agricarnationChibi, agricarnationChibiFloating).build(null);
	public static final TileEntityType<SubTileHopperhock> HOPPERHOCK = TileEntityType.Builder.create(SubTileHopperhock::new, hopperhock, hopperhockFloating).build(null);
	public static final TileEntityType<SubTileHopperhock.Mini> HOPPERHOCK_CHIBI = TileEntityType.Builder.create(SubTileHopperhock.Mini::new, hopperhockChibi, hopperhockChibiFloating).build(null);
	public static final TileEntityType<SubTileTangleberrie> TANGLEBERRIE = TileEntityType.Builder.create(SubTileTangleberrie::new, tangleberrie, tangleberrieFloating).build(null);
	public static final TileEntityType<SubTileTangleberrie.Mini> TANGLEBERRIE_CHIBI = TileEntityType.Builder.create(SubTileTangleberrie.Mini::new, tangleberrieChibi, tangleberrieChibiFloating).build(null);
	public static final TileEntityType<SubTileJiyuulia> JIYUULIA = TileEntityType.Builder.create(SubTileJiyuulia::new, jiyuulia, jiyuuliaFloating).build(null);
	public static final TileEntityType<SubTileJiyuulia.Mini> JIYUULIA_CHIBI = TileEntityType.Builder.create(SubTileJiyuulia.Mini::new, jiyuuliaChibi, jiyuuliaChibiFloating).build(null);
	public static final TileEntityType<SubTileRannuncarpus> RANNUNCARPUS = TileEntityType.Builder.create(SubTileRannuncarpus::new, rannuncarpus, rannuncarpusFloating).build(null);
	public static final TileEntityType<SubTileRannuncarpus.Mini> RANNUNCARPUS_CHIBI = TileEntityType.Builder.create(SubTileRannuncarpus.Mini::new, rannuncarpusChibi, rannuncarpusChibiFloating).build(null);
	public static final TileEntityType<SubTileHyacidus> HYACIDUS = TileEntityType.Builder.create(SubTileHyacidus::new, hyacidus, hyacidusFloating).build(null);
	public static final TileEntityType<SubTilePollidisiac> POLLIDISIAC = TileEntityType.Builder.create(SubTilePollidisiac::new, pollidisiac, pollidisiacFloating).build(null);
	public static final TileEntityType<SubTileClayconia> CLAYCONIA = TileEntityType.Builder.create(SubTileClayconia::new, clayconia, clayconiaFloating).build(null);
	public static final TileEntityType<SubTileClayconia.Mini> CLAYCONIA_CHIBI = TileEntityType.Builder.create(SubTileClayconia.Mini::new, clayconiaChibi, clayconiaChibiFloating).build(null);
	public static final TileEntityType<SubTileLoonuim> LOONIUM = TileEntityType.Builder.create(SubTileLoonuim::new, loonium, looniumFloating).build(null);
	public static final TileEntityType<SubTileDaffomill> DAFFOMILL = TileEntityType.Builder.create(SubTileDaffomill::new, daffomill, daffomillFloating).build(null);
	public static final TileEntityType<SubTileVinculotus> VINCULOTUS = TileEntityType.Builder.create(SubTileVinculotus::new, vinculotus, vinculotusFloating).build(null);
	public static final TileEntityType<SubTileSpectranthemum> SPECTRANTHEMUM = TileEntityType.Builder.create(SubTileSpectranthemum::new, spectranthemum, spectranthemumFloating).build(null);
	public static final TileEntityType<SubTileMedumone> MEDUMONE = TileEntityType.Builder.create(SubTileMedumone::new, medumone, medumoneFloating).build(null);
	public static final TileEntityType<SubTileMarimorphosis> MARIMORPHOSIS = TileEntityType.Builder.create(SubTileMarimorphosis::new, marimorphosis, marimorphosisFloating).build(null);
	public static final TileEntityType<SubTileMarimorphosis.Mini> MARIMORPHOSIS_CHIBI = TileEntityType.Builder.create(SubTileMarimorphosis.Mini::new, marimorphosisChibi, marimorphosisChibiFloating).build(null);
	public static final TileEntityType<SubTileBubbell> BUBBELL = TileEntityType.Builder.create(SubTileBubbell::new, bubbell, bubbellFloating).build(null);
	public static final TileEntityType<SubTileBubbell.Mini> BUBBELL_CHIBI = TileEntityType.Builder.create(SubTileBubbell.Mini::new, bubbellChibi, bubbellChibiFloating).build(null);
	public static final TileEntityType<SubTileSolegnolia> SOLEGNOLIA = TileEntityType.Builder.create(SubTileSolegnolia::new, solegnolia, solegnoliaFloating).build(null);
	public static final TileEntityType<SubTileSolegnolia.Mini> SOLEGNOLIA_CHIBI = TileEntityType.Builder.create(SubTileSolegnolia.Mini::new, solegnoliaChibi, solegnoliaChibiFloating).build(null);
	public static final TileEntityType<SubTileOrechidIgnem> ORECHID_IGNEM = TileEntityType.Builder.create(SubTileOrechidIgnem::new, orechidIgnem, orechidIgnemFloating).build(null);
	public static final TileEntityType<SubTileLabellia> LABELLIA = TileEntityType.Builder.create(SubTileLabellia::new, labellia, labelliaFloating).build(null);

	private static ResourceLocation floating(ResourceLocation orig) {
		return new ResourceLocation(orig.getNamespace(), "floating_" + orig.getPath());
	}

	private static ResourceLocation chibi(ResourceLocation orig) {
		return new ResourceLocation(orig.getNamespace(), orig.getPath() + "_chibi");
	}

	private static ResourceLocation getId(Block b) {
		return Registry.BLOCK.getKey(b);
	}

	public static void registerBlocks(RegistryEvent.Register<Block> evt) {
		IForgeRegistry<Block> r = evt.getRegistry();
		register(r, LibBlockNames.SUBTILE_PUREDAISY, pureDaisy);
		register(r, floating(LibBlockNames.SUBTILE_PUREDAISY), pureDaisyFloating);

		register(r, LibBlockNames.SUBTILE_MANASTAR, manastar);
		register(r, floating(LibBlockNames.SUBTILE_MANASTAR), manastarFloating);

		register(r, LibBlockNames.SUBTILE_HYDROANGEAS, hydroangeas);
		register(r, floating(LibBlockNames.SUBTILE_HYDROANGEAS), hydroangeasFloating);

		register(r, LibBlockNames.SUBTILE_ENDOFLAME, endoflame);
		register(r, floating(LibBlockNames.SUBTILE_ENDOFLAME), endoflameFloating);

		register(r, LibBlockNames.SUBTILE_THERMALILY, thermalily);
		register(r, floating(LibBlockNames.SUBTILE_THERMALILY), thermalilyFloating);

		register(r, LibBlockNames.SUBTILE_ARCANE_ROSE, rosaArcana);
		register(r, floating(LibBlockNames.SUBTILE_ARCANE_ROSE), rosaArcanaFloating);

		register(r, LibBlockNames.SUBTILE_MUNCHDEW, munchdew);
		register(r, floating(LibBlockNames.SUBTILE_MUNCHDEW), munchdewFloating);

		register(r, LibBlockNames.SUBTILE_ENTROPINNYUM, entropinnyum);
		register(r, floating(LibBlockNames.SUBTILE_ENTROPINNYUM), entropinnyumFloating);

		register(r, LibBlockNames.SUBTILE_KEKIMURUS, kekimurus);
		register(r, floating(LibBlockNames.SUBTILE_KEKIMURUS), kekimurusFloating);

		register(r, LibBlockNames.SUBTILE_GOURMARYLLIS, gourmaryllis);
		register(r, floating(LibBlockNames.SUBTILE_GOURMARYLLIS), gourmaryllisFloating);

		register(r, LibBlockNames.SUBTILE_NARSLIMMUS, narslimmus);
		register(r, floating(LibBlockNames.SUBTILE_NARSLIMMUS), narslimmusFloating);

		register(r, LibBlockNames.SUBTILE_SPECTROLUS, spectrolus);
		register(r, floating(LibBlockNames.SUBTILE_SPECTROLUS), spectrolusFloating);

		register(r, LibBlockNames.SUBTILE_DANDELIFEON, dandelifeon);
		register(r, floating(LibBlockNames.SUBTILE_DANDELIFEON), dandelifeonFloating);

		register(r, LibBlockNames.SUBTILE_RAFFLOWSIA, rafflowsia);
		register(r, floating(LibBlockNames.SUBTILE_RAFFLOWSIA), rafflowsiaFloating);

		register(r, LibBlockNames.SUBTILE_SHULK_ME_NOT, shulkMeNot);
		register(r, floating(LibBlockNames.SUBTILE_SHULK_ME_NOT), shulkMeNotFloating);

		register(r, LibBlockNames.SUBTILE_BELLETHORN, bellethorn);
		register(r, chibi(LibBlockNames.SUBTILE_BELLETHORN), bellethornChibi);
		register(r, floating(LibBlockNames.SUBTILE_BELLETHORN), bellethornFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_BELLETHORN)), bellethornChibiFloating);

		register(r, LibBlockNames.SUBTILE_BERGAMUTE, bergamute);
		register(r, floating(LibBlockNames.SUBTILE_BERGAMUTE), bergamuteFloating);

		register(r, LibBlockNames.SUBTILE_DREADTHORN, dreadthorn);
		register(r, floating(LibBlockNames.SUBTILE_DREADTHORN), dreadthornFloating);

		register(r, LibBlockNames.SUBTILE_HEISEI_DREAM, heiseiDream);
		register(r, floating(LibBlockNames.SUBTILE_HEISEI_DREAM), heiseiDreamFloating);

		register(r, LibBlockNames.SUBTILE_TIGERSEYE, tigerseye);
		register(r, floating(LibBlockNames.SUBTILE_TIGERSEYE), tigerseyeFloating);

		register(r, LibBlockNames.SUBTILE_JADED_AMARANTHUS, jadedAmaranthus);
		register(r, floating(LibBlockNames.SUBTILE_JADED_AMARANTHUS), jadedAmaranthusFloating);

		register(r, LibBlockNames.SUBTILE_ORECHID, orechid);
		register(r, floating(LibBlockNames.SUBTILE_ORECHID), orechidFloating);

		register(r, LibBlockNames.SUBTILE_FALLEN_KANADE, fallenKanade);
		register(r, floating(LibBlockNames.SUBTILE_FALLEN_KANADE), fallenKanadeFloating);

		register(r, LibBlockNames.SUBTILE_EXOFLAME, exoflame);
		register(r, floating(LibBlockNames.SUBTILE_EXOFLAME), exoflameFloating);

		register(r, LibBlockNames.SUBTILE_AGRICARNATION, agricarnation);
		register(r, chibi(LibBlockNames.SUBTILE_AGRICARNATION), agricarnationChibi);
		register(r, floating(LibBlockNames.SUBTILE_AGRICARNATION), agricarnationFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_AGRICARNATION)), agricarnationChibiFloating);

		register(r, LibBlockNames.SUBTILE_HOPPERHOCK, hopperhock);
		register(r, chibi(LibBlockNames.SUBTILE_HOPPERHOCK), hopperhockChibi);
		register(r, floating(LibBlockNames.SUBTILE_HOPPERHOCK), hopperhockFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_HOPPERHOCK)), hopperhockChibiFloating);

		register(r, LibBlockNames.SUBTILE_TANGLEBERRIE, tangleberrie);
		register(r, chibi(LibBlockNames.SUBTILE_TANGLEBERRIE), tangleberrieChibi);
		register(r, floating(LibBlockNames.SUBTILE_TANGLEBERRIE), tangleberrieFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_TANGLEBERRIE)), tangleberrieChibiFloating);

		register(r, LibBlockNames.SUBTILE_JIYUULIA, jiyuulia);
		register(r, chibi(LibBlockNames.SUBTILE_JIYUULIA), jiyuuliaChibi);
		register(r, floating(LibBlockNames.SUBTILE_JIYUULIA), jiyuuliaFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_JIYUULIA)), jiyuuliaChibiFloating);

		register(r, LibBlockNames.SUBTILE_RANNUNCARPUS, rannuncarpus);
		register(r, chibi(LibBlockNames.SUBTILE_RANNUNCARPUS), rannuncarpusChibi);
		register(r, floating(LibBlockNames.SUBTILE_RANNUNCARPUS), rannuncarpusFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_RANNUNCARPUS)), rannuncarpusChibiFloating);

		register(r, LibBlockNames.SUBTILE_HYACIDUS, hyacidus);
		register(r, floating(LibBlockNames.SUBTILE_HYACIDUS), hyacidusFloating);

		register(r, LibBlockNames.SUBTILE_POLLIDISIAC, pollidisiac);
		register(r, floating(LibBlockNames.SUBTILE_POLLIDISIAC), pollidisiacFloating);

		register(r, LibBlockNames.SUBTILE_CLAYCONIA, clayconia);
		register(r, chibi(LibBlockNames.SUBTILE_CLAYCONIA), clayconiaChibi);
		register(r, floating(LibBlockNames.SUBTILE_CLAYCONIA), clayconiaFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_CLAYCONIA)), clayconiaChibiFloating);

		register(r, LibBlockNames.SUBTILE_LOONIUM, loonium);
		register(r, floating(LibBlockNames.SUBTILE_LOONIUM), looniumFloating);

		register(r, LibBlockNames.SUBTILE_DAFFOMILL, daffomill);
		register(r, floating(LibBlockNames.SUBTILE_DAFFOMILL), daffomillFloating);

		register(r, LibBlockNames.SUBTILE_VINCULOTUS, vinculotus);
		register(r, floating(LibBlockNames.SUBTILE_VINCULOTUS), vinculotusFloating);

		register(r, LibBlockNames.SUBTILE_SPECTRANTHEMUM, spectranthemum);
		register(r, floating(LibBlockNames.SUBTILE_SPECTRANTHEMUM), spectranthemumFloating);

		register(r, LibBlockNames.SUBTILE_MEDUMONE, medumone);
		register(r, floating(LibBlockNames.SUBTILE_MEDUMONE), medumoneFloating);

		register(r, LibBlockNames.SUBTILE_MARIMORPHOSIS, marimorphosis);
		register(r, chibi(LibBlockNames.SUBTILE_MARIMORPHOSIS), marimorphosisChibi);
		register(r, floating(LibBlockNames.SUBTILE_MARIMORPHOSIS), marimorphosisFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_MARIMORPHOSIS)), marimorphosisChibiFloating);

		register(r, LibBlockNames.SUBTILE_BUBBELL, bubbell);
		register(r, chibi(LibBlockNames.SUBTILE_BUBBELL), bubbellChibi);
		register(r, floating(LibBlockNames.SUBTILE_BUBBELL), bubbellFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_BUBBELL)), bubbellChibiFloating);

		register(r, LibBlockNames.SUBTILE_SOLEGNOLIA, solegnolia);
		register(r, chibi(LibBlockNames.SUBTILE_SOLEGNOLIA), solegnoliaChibi);
		register(r, floating(LibBlockNames.SUBTILE_SOLEGNOLIA), solegnoliaFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_SOLEGNOLIA)), solegnoliaChibiFloating);

		register(r, LibBlockNames.SUBTILE_ORECHID_IGNEM, orechidIgnem);
		register(r, floating(LibBlockNames.SUBTILE_ORECHID_IGNEM), orechidIgnemFloating);

		register(r, LibBlockNames.SUBTILE_LABELLIA, labellia);
		register(r, floating(LibBlockNames.SUBTILE_LABELLIA), labelliaFloating);
	}

	public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Item> r = evt.getRegistry();
		Item.Properties props = ModItems.defaultBuilder();

		register(r, getId(pureDaisy), new ItemBlockSpecialFlower(pureDaisy, props));
		register(r, getId(pureDaisyFloating), new ItemBlockSpecialFlower(pureDaisyFloating, props));

		register(r, getId(manastar), new ItemBlockSpecialFlower(manastar, props));
		register(r, getId(manastarFloating), new ItemBlockSpecialFlower(manastarFloating, props));

		register(r, getId(hydroangeas), new ItemBlockSpecialFlower(hydroangeas, props));
		register(r, getId(hydroangeasFloating), new ItemBlockSpecialFlower(hydroangeasFloating, props));

		register(r, getId(endoflame), new ItemBlockSpecialFlower(endoflame, props));
		register(r, getId(endoflameFloating), new ItemBlockSpecialFlower(endoflameFloating, props));

		register(r, getId(thermalily), new ItemBlockSpecialFlower(thermalily, props));
		register(r, getId(thermalilyFloating), new ItemBlockSpecialFlower(thermalilyFloating, props));

		register(r, getId(rosaArcana), new ItemBlockSpecialFlower(rosaArcana, props));
		register(r, getId(rosaArcanaFloating), new ItemBlockSpecialFlower(rosaArcanaFloating, props));

		register(r, getId(munchdew), new ItemBlockSpecialFlower(munchdew, props));
		register(r, getId(munchdewFloating), new ItemBlockSpecialFlower(munchdewFloating, props));

		register(r, getId(entropinnyum), new ItemBlockSpecialFlower(entropinnyum, props));
		register(r, getId(entropinnyumFloating), new ItemBlockSpecialFlower(entropinnyumFloating, props));

		register(r, getId(kekimurus), new ItemBlockSpecialFlower(kekimurus, props));
		register(r, getId(kekimurusFloating), new ItemBlockSpecialFlower(kekimurusFloating, props));

		register(r, getId(gourmaryllis), new ItemBlockSpecialFlower(gourmaryllis, props));
		register(r, getId(gourmaryllisFloating), new ItemBlockSpecialFlower(gourmaryllisFloating, props));

		register(r, getId(narslimmus), new ItemBlockSpecialFlower(narslimmus, props));
		register(r, getId(narslimmusFloating), new ItemBlockSpecialFlower(narslimmusFloating, props));

		register(r, getId(spectrolus), new ItemBlockSpecialFlower(spectrolus, props));
		register(r, getId(spectrolusFloating), new ItemBlockSpecialFlower(spectrolusFloating, props));

		register(r, getId(dandelifeon), new ItemBlockSpecialFlower(dandelifeon, props));
		register(r, getId(dandelifeonFloating), new ItemBlockSpecialFlower(dandelifeonFloating, props));

		register(r, getId(rafflowsia), new ItemBlockSpecialFlower(rafflowsia, props));
		register(r, getId(rafflowsiaFloating), new ItemBlockSpecialFlower(rafflowsiaFloating, props));

		register(r, getId(shulkMeNot), new ItemBlockSpecialFlower(shulkMeNot, props));
		register(r, getId(shulkMeNotFloating), new ItemBlockSpecialFlower(shulkMeNotFloating, props));

		register(r, getId(bellethorn), new ItemBlockSpecialFlower(bellethorn, props));
		register(r, getId(bellethornChibi), new ItemBlockSpecialFlower(bellethornChibi, props));
		register(r, getId(bellethornFloating), new ItemBlockSpecialFlower(bellethornFloating, props));
		register(r, getId(bellethornChibiFloating), new ItemBlockSpecialFlower(bellethornChibiFloating, props));

		register(r, getId(bergamute), new ItemBlockSpecialFlower(bergamute, props));
		register(r, getId(bergamuteFloating), new ItemBlockSpecialFlower(bergamuteFloating, props));

		register(r, getId(dreadthorn), new ItemBlockSpecialFlower(dreadthorn, props));
		register(r, getId(dreadthornFloating), new ItemBlockSpecialFlower(dreadthornFloating, props));

		register(r, getId(heiseiDream), new ItemBlockSpecialFlower(heiseiDream, props));
		register(r, getId(heiseiDreamFloating), new ItemBlockSpecialFlower(heiseiDreamFloating, props));

		register(r, getId(tigerseye), new ItemBlockSpecialFlower(tigerseye, props));
		register(r, getId(tigerseyeFloating), new ItemBlockSpecialFlower(tigerseyeFloating, props));

		register(r, getId(jadedAmaranthus), new ItemBlockSpecialFlower(jadedAmaranthus, props));
		register(r, getId(jadedAmaranthusFloating), new ItemBlockSpecialFlower(jadedAmaranthusFloating, props));

		register(r, getId(orechid), new ItemBlockSpecialFlower(orechid, props));
		register(r, getId(orechidFloating), new ItemBlockSpecialFlower(orechidFloating, props));

		register(r, getId(fallenKanade), new ItemBlockSpecialFlower(fallenKanade, props));
		register(r, getId(fallenKanadeFloating), new ItemBlockSpecialFlower(fallenKanadeFloating, props));

		register(r, getId(exoflame), new ItemBlockSpecialFlower(exoflame, props));
		register(r, getId(exoflameFloating), new ItemBlockSpecialFlower(exoflameFloating, props));

		register(r, getId(agricarnation), new ItemBlockSpecialFlower(agricarnation, props));
		register(r, getId(agricarnationChibi), new ItemBlockSpecialFlower(agricarnationChibi, props));
		register(r, getId(agricarnationFloating), new ItemBlockSpecialFlower(agricarnationFloating, props));
		register(r, getId(agricarnationChibiFloating), new ItemBlockSpecialFlower(agricarnationChibiFloating, props));

		register(r, getId(hopperhock), new ItemBlockSpecialFlower(hopperhock, props));
		register(r, getId(hopperhockChibi), new ItemBlockSpecialFlower(hopperhockChibi, props));
		register(r, getId(hopperhockFloating), new ItemBlockSpecialFlower(hopperhockFloating, props));
		register(r, getId(hopperhockChibiFloating), new ItemBlockSpecialFlower(hopperhockChibiFloating, props));

		register(r, getId(tangleberrie), new ItemBlockSpecialFlower(tangleberrie, props));
		register(r, getId(tangleberrieChibi), new ItemBlockSpecialFlower(tangleberrieChibi, props));
		register(r, getId(tangleberrieFloating), new ItemBlockSpecialFlower(tangleberrieFloating, props));
		register(r, getId(tangleberrieChibiFloating), new ItemBlockSpecialFlower(tangleberrieChibiFloating, props));

		register(r, getId(jiyuulia), new ItemBlockSpecialFlower(jiyuulia, props));
		register(r, getId(jiyuuliaChibi), new ItemBlockSpecialFlower(jiyuuliaChibi, props));
		register(r, getId(jiyuuliaFloating), new ItemBlockSpecialFlower(jiyuuliaFloating, props));
		register(r, getId(jiyuuliaChibiFloating), new ItemBlockSpecialFlower(jiyuuliaChibiFloating, props));

		register(r, getId(rannuncarpus), new ItemBlockSpecialFlower(rannuncarpus, props));
		register(r, getId(rannuncarpusChibi), new ItemBlockSpecialFlower(rannuncarpusChibi, props));
		register(r, getId(rannuncarpusFloating), new ItemBlockSpecialFlower(rannuncarpusFloating, props));
		register(r, getId(rannuncarpusChibiFloating), new ItemBlockSpecialFlower(rannuncarpusChibiFloating, props));

		register(r, getId(hyacidus), new ItemBlockSpecialFlower(hyacidus, props));
		register(r, getId(hyacidusFloating), new ItemBlockSpecialFlower(hyacidusFloating, props));

		register(r, getId(pollidisiac), new ItemBlockSpecialFlower(pollidisiac, props));
		register(r, getId(pollidisiacFloating), new ItemBlockSpecialFlower(pollidisiacFloating, props));

		register(r, getId(clayconia), new ItemBlockSpecialFlower(clayconia, props));
		register(r, getId(clayconiaChibi), new ItemBlockSpecialFlower(clayconiaChibi, props));
		register(r, getId(clayconiaFloating), new ItemBlockSpecialFlower(clayconiaFloating, props));
		register(r, getId(clayconiaChibiFloating), new ItemBlockSpecialFlower(clayconiaChibiFloating, props));

		register(r, getId(loonium), new ItemBlockSpecialFlower(loonium, props));
		register(r, getId(looniumFloating), new ItemBlockSpecialFlower(looniumFloating, props));

		register(r, getId(daffomill), new ItemBlockSpecialFlower(daffomill, props));
		register(r, getId(daffomillFloating), new ItemBlockSpecialFlower(daffomillFloating, props));

		register(r, getId(vinculotus), new ItemBlockSpecialFlower(vinculotus, props));
		register(r, getId(vinculotusFloating), new ItemBlockSpecialFlower(vinculotusFloating, props));

		register(r, getId(spectranthemum), new ItemBlockSpecialFlower(spectranthemum, props));
		register(r, getId(spectranthemumFloating), new ItemBlockSpecialFlower(spectranthemumFloating, props));

		register(r, getId(medumone), new ItemBlockSpecialFlower(medumone, props));
		register(r, getId(medumoneFloating), new ItemBlockSpecialFlower(medumoneFloating, props));

		register(r, getId(marimorphosis), new ItemBlockSpecialFlower(marimorphosis, props));
		register(r, getId(marimorphosisChibi), new ItemBlockSpecialFlower(marimorphosisChibi, props));
		register(r, getId(marimorphosisFloating), new ItemBlockSpecialFlower(marimorphosisFloating, props));
		register(r, getId(marimorphosisChibiFloating), new ItemBlockSpecialFlower(marimorphosisChibiFloating, props));

		register(r, getId(bubbell), new ItemBlockSpecialFlower(bubbell, props));
		register(r, getId(bubbellChibi), new ItemBlockSpecialFlower(bubbellChibi, props));
		register(r, getId(bubbellFloating), new ItemBlockSpecialFlower(bubbellFloating, props));
		register(r, getId(bubbellChibiFloating), new ItemBlockSpecialFlower(bubbellChibiFloating, props));

		register(r, getId(solegnolia), new ItemBlockSpecialFlower(solegnolia, props));
		register(r, getId(solegnoliaChibi), new ItemBlockSpecialFlower(solegnoliaChibi, props));
		register(r, getId(solegnoliaFloating), new ItemBlockSpecialFlower(solegnoliaFloating, props));
		register(r, getId(solegnoliaChibiFloating), new ItemBlockSpecialFlower(solegnoliaChibiFloating, props));

		register(r, getId(orechidIgnem), new ItemBlockSpecialFlower(orechidIgnem, props));
		register(r, getId(orechidIgnemFloating), new ItemBlockSpecialFlower(orechidIgnemFloating, props));

		register(r, getId(labellia), new ItemBlockSpecialFlower(labellia, props));
		register(r, getId(labelliaFloating), new ItemBlockSpecialFlower(labelliaFloating, props));
	}

	public static void registerTEs(RegistryEvent.Register<TileEntityType<?>> evt) {
		IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();
		register(r, getId(pureDaisy), PURE_DAISY);
		register(r, getId(manastar), MANASTAR);
		register(r, getId(hydroangeas), HYDROANGEAS);
		register(r, getId(endoflame), ENDOFLAME);
		register(r, getId(thermalily), THERMALILY);
		register(r, getId(rosaArcana), ROSA_ARCANA);
		register(r, getId(munchdew), MUNCHDEW);
		register(r, getId(entropinnyum), ENTROPINNYUM);
		register(r, getId(kekimurus), KEKIMURUS);
		register(r, getId(gourmaryllis), GOURMARYLLIS);
		register(r, getId(narslimmus), NARSLIMMUS);
		register(r, getId(spectrolus), SPECTROLUS);
		register(r, getId(dandelifeon), DANDELIFEON);
		register(r, getId(rafflowsia), RAFFLOWSIA);
		register(r, getId(shulkMeNot), SHULK_ME_NOT);
		register(r, getId(bellethorn), BELLETHORNE);
		register(r, getId(bellethornChibi), BELLETHORNE_CHIBI);
		register(r, getId(bergamute), BERGAMUTE);
		register(r, getId(dreadthorn), DREADTHORN);
		register(r, getId(heiseiDream), HEISEI_DREAM);
		register(r, getId(tigerseye), TIGERSEYE);
		register(r, getId(jadedAmaranthus), JADED_AMARANTHUS);
		register(r, getId(orechid), ORECHID);
		register(r, getId(fallenKanade), FALLEN_KANADE);
		register(r, getId(exoflame), EXOFLAME);
		register(r, getId(agricarnation), AGRICARNATION);
		register(r, getId(agricarnationChibi), AGRICARNATION_CHIBI);
		register(r, getId(hopperhock), HOPPERHOCK);
		register(r, getId(hopperhockChibi), HOPPERHOCK_CHIBI);
		register(r, getId(tangleberrie), TANGLEBERRIE);
		register(r, getId(tangleberrieChibi), TANGLEBERRIE_CHIBI);
		register(r, getId(jiyuulia), JIYUULIA);
		register(r, getId(jiyuuliaChibi), JIYUULIA_CHIBI);
		register(r, getId(rannuncarpus), RANNUNCARPUS);
		register(r, getId(rannuncarpusChibi), RANNUNCARPUS_CHIBI);
		register(r, getId(hyacidus), HYACIDUS);
		register(r, getId(pollidisiac), POLLIDISIAC);
		register(r, getId(clayconia), CLAYCONIA);
		register(r, getId(clayconiaChibi), CLAYCONIA_CHIBI);
		register(r, getId(loonium), LOONIUM);
		register(r, getId(daffomill), DAFFOMILL);
		register(r, getId(vinculotus), VINCULOTUS);
		register(r, getId(spectranthemum), SPECTRANTHEMUM);
		register(r, getId(medumone), MEDUMONE);
		register(r, getId(marimorphosis), MARIMORPHOSIS);
		register(r, getId(marimorphosisChibi), MARIMORPHOSIS_CHIBI);
		register(r, getId(bubbell), BUBBELL);
		register(r, getId(bubbellChibi), BUBBELL_CHIBI);
		register(r, getId(solegnolia), SOLEGNOLIA);
		register(r, getId(solegnoliaChibi), SOLEGNOLIA_CHIBI);
		register(r, getId(orechidIgnem), ORECHID_IGNEM);
		register(r, getId(labellia), LABELLIA);
	}

	public static void registerRemappers(IEventBus bus) {
		bus.addGenericListener(Block.class, (Consumer<RegistryEvent.MissingMappings<Block>>) ModSubtiles::remapLabellia);
		bus.addGenericListener(Item.class, (Consumer<RegistryEvent.MissingMappings<Item>>) ModSubtiles::remapLabellia);
		bus.addGenericListener(SoundEvent.class, (Consumer<RegistryEvent.MissingMappings<SoundEvent>>) ModSubtiles::remapLabellia);
	}

	private static <T extends IForgeRegistryEntry<T>> void remapLabellia(RegistryEvent.MissingMappings<T> event) {
		List<? extends RegistryEvent.MissingMappings.Mapping<T>> list = event.getMappings(LibMisc.MOD_ID);
		for (RegistryEvent.MissingMappings.Mapping<T> mapping : list) {
			String path = mapping.key.getPath();
			if (path.endsWith("labelia")) {
				ResourceLocation newId = new ResourceLocation(LibMisc.MOD_ID, path.replace("labelia", "labellia"));
				T value = RegistryManager.ACTIVE.getRegistry(mapping.registry.getRegistrySuperType()).getValue(newId);
				Botania.LOGGER.debug("Remapping value for {} from id {} to id {}",
						mapping.registry.getRegistrySuperType(), mapping.key, newId);
				mapping.remap(value);
			}
		}
	}
}
