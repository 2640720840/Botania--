/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.capability.FloatingFlowerImpl;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.item.IExoflameHeatable;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.fx.ModParticles;
import vazkii.botania.common.advancements.ModCriteriaTriggers;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.string.BlockRedStringInterceptor;
import vazkii.botania.common.block.subtile.functional.SubTileDaffomill;
import vazkii.botania.common.block.subtile.functional.SubTileLoonuim;
import vazkii.botania.common.block.subtile.functional.SubTileVinculotus;
import vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum;
import vazkii.botania.common.block.subtile.generating.SubTileNarslimmus;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.block.tile.TileCraftCrate;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.block.tile.TileTerraPlate;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.brew.ModPotions;
import vazkii.botania.common.brew.potion.PotionEmptiness;
import vazkii.botania.common.brew.potion.PotionSoulCross;
import vazkii.botania.common.capability.NoopCapStorage;
import vazkii.botania.common.capability.NoopExoflameHeatable;
import vazkii.botania.common.core.ModStats;
import vazkii.botania.common.core.command.SkyblockCommand;
import vazkii.botania.common.core.handler.*;
import vazkii.botania.common.core.loot.LootHandler;
import vazkii.botania.common.core.loot.ModLootModifiers;
import vazkii.botania.common.core.proxy.IProxy;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.impl.BotaniaAPIImpl;
import vazkii.botania.common.impl.corporea.CorporeaItemStackMatcher;
import vazkii.botania.common.impl.corporea.CorporeaStringMatcher;
import vazkii.botania.common.integration.crafttweaker.CTIntegration;
import vazkii.botania.common.item.ItemGrassSeeds;
import vazkii.botania.common.item.ItemKeepIvy;
import vazkii.botania.common.item.ItemVirus;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemGoddessCharm;
import vazkii.botania.common.item.material.ItemEnderAir;
import vazkii.botania.common.item.relic.ItemLokiRing;
import vazkii.botania.common.item.relic.ItemOdinRing;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.network.PacketHandler;
import vazkii.botania.common.world.ModFeatures;
import vazkii.botania.common.world.SkyblockChunkGenerator;
import vazkii.botania.common.world.SkyblockWorldEvents;
import vazkii.botania.data.DataGenerators;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod(LibMisc.MOD_ID)
public class Botania {

	public static boolean gardenOfGlassLoaded = false;

	public static boolean curiosLoaded = false;

	public static IProxy proxy = new IProxy() {};
	public static volatile boolean configLoaded = false;

	public static final Logger LOGGER = LogManager.getLogger(LibMisc.MOD_ID);

	public Botania() {
		gardenOfGlassLoaded = ModList.get().isLoaded(LibMisc.GOG_MOD_ID);
		curiosLoaded = ModList.get().isLoaded("curios");

		DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> proxy = new ClientProxy());
		proxy.registerHandlers();
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::commonSetup);
		modBus.addListener(DataGenerators::gatherData);
		modBus.addGenericListener(Feature.class, ModFeatures::registerFeatures);
		modBus.addGenericListener(Item.class, ModItems::registerItems);
		modBus.addGenericListener(ContainerType.class, ModItems::registerContainers);
		modBus.addGenericListener(IRecipeSerializer.class, ModItems::registerRecipeSerializers);
		modBus.addGenericListener(EntityType.class, ModEntities::registerEntities);
		modBus.addGenericListener(IRecipeSerializer.class, ModRecipeTypes::register);
		modBus.addGenericListener(SoundEvent.class, ModSounds::registerSounds);
		modBus.addGenericListener(Brew.class, ModBrews::registerBrews);
		modBus.addListener(ModBrews::registerRegistry);
		modBus.addGenericListener(Effect.class, ModPotions::registerPotions);
		modBus.addGenericListener(Block.class, ModBlocks::registerBlocks);
		modBus.addGenericListener(Item.class, ModBlocks::registerItemBlocks);
		modBus.addGenericListener(TileEntityType.class, ModTiles::registerTiles);
		modBus.addGenericListener(Block.class, ModFluffBlocks::registerBlocks);
		modBus.addGenericListener(Item.class, ModFluffBlocks::registerItemBlocks);
		modBus.addGenericListener(ParticleType.class, ModParticles::registerParticles);
		modBus.addGenericListener(Block.class, ModSubtiles::registerBlocks);
		modBus.addGenericListener(Item.class, ModSubtiles::registerItemBlocks);
		modBus.addGenericListener(TileEntityType.class, ModSubtiles::registerTEs);
		modBus.addGenericListener(Attribute.class, PixieHandler::registerAttribute);
		modBus.addGenericListener(ForgeWorldType.class, ModFeatures::registerWorldType);
		modBus.addListener((ModConfig.Loading e) -> ConfigHandler.onConfigLoad());
		modBus.addListener((ModConfig.Reloading e) -> ConfigHandler.onConfigLoad());
		modBus.addListener(ModEntities::registerAttributes);

		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		forgeBus.addListener(this::serverAboutToStart);
		forgeBus.addListener(this::registerCommands);
		forgeBus.addListener(this::serverStopping);
		forgeBus.addListener(ItemLokiRing::onPlayerInteract);
		forgeBus.addListener(ItemOdinRing::onPlayerAttacked);
		forgeBus.addListener(ItemEnderAir::onPlayerInteract);
		forgeBus.addListener(ItemGoddessCharm::onExplosion);
		forgeBus.addListener(ItemGrassSeeds::onTickEnd);
		forgeBus.addListener(ItemKeepIvy::onPlayerDrops);
		forgeBus.addListener(ItemKeepIvy::onPlayerRespawn);
		forgeBus.addListener(ItemVirus::onLivingHurt);
		forgeBus.addListener(SleepingHandler::trySleep);
		forgeBus.addListener(PixieHandler::onDamageTaken);
		forgeBus.addGenericListener(TileEntity.class, ExoflameFurnaceHandler::attachFurnaceCapability);
		forgeBus.addListener(CommonTickHandler::onTick);
		forgeBus.addListener(PotionEmptiness::onSpawn);
		forgeBus.addListener(PotionSoulCross::onEntityKill);
		forgeBus.addListener(SubTileNarslimmus::onSpawn);
		forgeBus.addListener(SubTileEntropinnyum::onEntityJoin);
		forgeBus.addListener(SubTileDaffomill::onItemTrack);
		forgeBus.addListener(SubTileVinculotus::onEndermanTeleport);
		forgeBus.addListener(EventPriority.LOWEST, SubTileLoonuim::onDrops);
		forgeBus.addListener(BlockRedStringInterceptor::onInteract);
		forgeBus.addListener(ManaNetworkHandler.instance::onNetworkEvent);
		forgeBus.addListener(EventPriority.HIGHEST, TileCorporeaIndex.getInputHandler()::onChatMessage);
		forgeBus.addListener(LootHandler::lootLoad);
		forgeBus.addListener(EventPriority.HIGH, ModFeatures::onBiomeLoad);
		forgeBus.addListener(OrechidResourceListener::registerListener);
		forgeBus.addListener(TileCraftCrate::registerListener);
		ModSubtiles.registerRemappers(forgeBus);

		ModLootModifiers.init();
		ModCriteriaTriggers.init();
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(IFloatingFlower.class, new IFloatingFlower.Storage(), FloatingFlowerImpl::new);
		CapabilityManager.INSTANCE.register(IExoflameHeatable.class, new NoopCapStorage<>(), NoopExoflameHeatable::new);

		PacketHandler.init();
		PaintableData.init();

		EquipmentHandler.init();
		CorporeaHelper.instance().registerRequestMatcher(prefix("string"), CorporeaStringMatcher.class, CorporeaStringMatcher::createFromNBT);
		CorporeaHelper.instance().registerRequestMatcher(prefix("item_stack"), CorporeaItemStackMatcher.class, CorporeaItemStackMatcher::createFromNBT);

		if (Botania.gardenOfGlassLoaded) {
			MinecraftForge.EVENT_BUS.addListener(SkyblockWorldEvents::syncGogStatus);
			MinecraftForge.EVENT_BUS.addListener(SkyblockWorldEvents::onPlayerJoin);
			MinecraftForge.EVENT_BUS.addListener(SkyblockWorldEvents::onPlayerInteract);
		}

		if (ModList.get().isLoaded("crafttweaker")) {
			CTIntegration.register();
		}

		event.enqueueWork(() -> {
			SkyblockChunkGenerator.init();

			PatchouliAPI.get().registerMultiblock(Registry.BLOCK.getKey(ModBlocks.alfPortal), TileAlfPortal.MULTIBLOCK.getValue());
			PatchouliAPI.get().registerMultiblock(Registry.BLOCK.getKey(ModBlocks.terraPlate), TileTerraPlate.MULTIBLOCK.getValue());
			PatchouliAPI.get().registerMultiblock(Registry.BLOCK.getKey(ModBlocks.enchanter), TileEnchanter.MULTIBLOCK.getValue());

			String[][] pat = new String[][] {
					{
							"P_______P",
							"_________",
							"_________",
							"_________",
							"_________",
							"_________",
							"_________",
							"_________",
							"P_______P",
					},
					{
							"_________",
							"_________",
							"_________",
							"_________",
							"____B____",
							"_________",
							"_________",
							"_________",
							"_________",
					},
					{
							"_________",
							"_________",
							"_________",
							"___III___",
							"___I0I___",
							"___III___",
							"_________",
							"_________",
							"_________",
					}
			};
			IStateMatcher sm = PatchouliAPI.get().predicateMatcher(Blocks.IRON_BLOCK,
					state -> state.isIn(BlockTags.BEACON_BASE_BLOCKS));
			IMultiblock mb = PatchouliAPI.get().makeMultiblock(
					pat,
					'P', ModBlocks.gaiaPylon,
					'B', Blocks.BEACON,
					'I', sm,
					'0', sm
			);
			PatchouliAPI.get().registerMultiblock(prefix("gaia_ritual"), mb);

			ModBlocks.addDispenserBehaviours();

			ModStats.init();
		});
	}

	private void serverAboutToStart(FMLServerAboutToStartEvent event) {
		if (BotaniaAPI.instance().getClass() != BotaniaAPIImpl.class) {
			String clname = BotaniaAPI.instance().getClass().getName();
			throw new IllegalAccessError("The Botania API has been overriden. "
					+ "This will cause crashes and compatibility issues, and that's why it's marked as"
					+ " \"Do not Override\". Whoever had the brilliant idea of overriding it needs to go"
					+ " back to elementary school and learn to read. (Actual classname: " + clname + ")");
		}

		if (FMLEnvironment.dist.isDedicatedServer()) {
			ContributorList.firstStart();
		}
	}

	private void registerCommands(RegisterCommandsEvent event) {
		if (Botania.gardenOfGlassLoaded) {
			SkyblockCommand.register(event.getDispatcher());
		}
	}

	private void serverStopping(FMLServerStoppingEvent event) {
		ManaNetworkHandler.instance.clear();
		TileCorporeaIndex.clearCache();
	}

}
