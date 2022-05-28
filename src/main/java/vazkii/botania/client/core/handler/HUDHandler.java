/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.ICreativeManaProvider;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex.InputHandler;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ItemCraftingHalo;
import vazkii.botania.common.item.ItemSextant;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemDodgeRing;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.equipment.bauble.ItemMonocle;

import java.util.List;

public final class HUDHandler {

	private HUDHandler() {}

	public static final ResourceLocation manaBar = new ResourceLocation(LibResources.GUI_MANA_HUD);

	public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		IProfiler profiler = mc.getProfiler();
		ItemStack main = mc.player.getHeldItemMainhand();
		ItemStack offhand = mc.player.getHeldItemOffhand();
		MatrixStack ms = event.getMatrixStack();

		if (event.getType() == ElementType.ALL) {
			profiler.startSection("botania-hud");

			if (Minecraft.getInstance().playerController.shouldDrawHUD()) {
				ItemStack tiara = EquipmentHandler.findOrEmpty(ModItems.flightTiara, mc.player);
				if (!tiara.isEmpty()) {
					profiler.startSection("flugelTiara");
					ItemFlightTiara.renderHUD(ms, mc.player, tiara);
					profiler.endSection();
				}

				ItemStack dodgeRing = EquipmentHandler.findOrEmpty(ModItems.dodgeRing, mc.player);
				if (!dodgeRing.isEmpty()) {
					profiler.startSection("dodgeRing");
					ItemDodgeRing.renderHUD(ms, mc.player, dodgeRing, event.getPartialTicks());
					profiler.endSection();
				}
			}

			RayTraceResult pos = mc.objectMouseOver;

			if (pos != null) {
				BlockPos bpos = pos.getType() == RayTraceResult.Type.BLOCK ? ((BlockRayTraceResult) pos).getPos() : null;
				BlockState state = bpos != null ? mc.world.getBlockState(bpos) : null;
				Block block = state == null ? null : state.getBlock();
				TileEntity tile = bpos != null ? mc.world.getTileEntity(bpos) : null;

				if (PlayerHelper.hasAnyHeldItem(mc.player)) {
					if (PlayerHelper.hasHeldItem(mc.player, ModItems.twigWand)) {
						if (block instanceof IWandHUD) {
							profiler.startSection("wandItem");
							((IWandHUD) block).renderHUD(ms, mc, mc.world, bpos);
							profiler.endSection();
						}
					}
					if (tile instanceof TilePool && !mc.player.getHeldItemMainhand().isEmpty()) {
						renderPoolRecipeHUD(ms, (TilePool) tile, mc.player.getHeldItemMainhand());
					}
				}
				if (!PlayerHelper.hasHeldItem(mc.player, ModItems.lexicon)) {
					if (tile instanceof TileAltar) {
						((TileAltar) tile).renderHUD(ms, mc);
					} else if (tile instanceof TileRuneAltar) {
						((TileRuneAltar) tile).renderHUD(ms, mc);
					} else if (tile instanceof TileCorporeaCrystalCube) {
						renderCrystalCubeHUD(ms, (TileCorporeaCrystalCube) tile);
					}
				}
			}

			TileCorporeaIndex.getInputHandler();
			if (!InputHandler.getNearbyIndexes(mc.player).isEmpty() && mc.currentScreen instanceof ChatScreen) {
				profiler.startSection("nearIndex");
				renderNearIndexDisplay(ms);
				profiler.endSection();
			}

			if (!main.isEmpty() && main.getItem() instanceof ItemCraftingHalo) {
				profiler.startSection("craftingHalo_main");
				ItemCraftingHalo.renderHUD(ms, mc.player, main);
				profiler.endSection();
			} else if (!offhand.isEmpty() && offhand.getItem() instanceof ItemCraftingHalo) {
				profiler.startSection("craftingHalo_off");
				ItemCraftingHalo.renderHUD(ms, mc.player, offhand);
				profiler.endSection();
			}

			if (!main.isEmpty() && main.getItem() instanceof ItemSextant) {
				profiler.startSection("sextant");
				ItemSextant.renderHUD(ms, mc.player, main);
				profiler.endSection();
			}

			/*if(equippedStack != null && equippedStack.getItem() == ModItems.flugelEye) {
				profiler.startSection("flugelEye");
				ItemFlugelEye.renderHUD(event.getResolution(), mc.player, equippedStack);
				profiler.endSection();
			}*/

			if (Botania.proxy.isClientPlayerWearingMonocle()) {
				profiler.startSection("monocle");
				ItemMonocle.renderHUD(ms, mc.player);
				profiler.endSection();
			}

			profiler.startSection("manaBar");

			PlayerEntity player = mc.player;
			if (!player.isSpectator()) {
				int totalMana = 0;
				int totalMaxMana = 0;
				boolean anyRequest = false;
				boolean creative = false;

				IInventory mainInv = player.inventory;
				IInventory accInv = BotaniaAPI.instance().getAccessoriesInventory(player);

				int invSize = mainInv.getSizeInventory();
				int size = invSize + accInv.getSizeInventory();

				for (int i = 0; i < size; i++) {
					boolean useAccessories = i >= invSize;
					IInventory inv = useAccessories ? accInv : mainInv;
					ItemStack stack = inv.getStackInSlot(i - (useAccessories ? invSize : 0));

					if (!stack.isEmpty()) {
						Item item = stack.getItem();
						if (item instanceof IManaUsingItem) {
							anyRequest = anyRequest || ((IManaUsingItem) item).usesMana(stack);
						}
					}
				}

				List<ItemStack> items = ManaItemHandler.instance().getManaItems(player);
				for (ItemStack stack : items) {
					Item item = stack.getItem();
					if (!((IManaItem) item).isNoExport(stack)) {
						totalMana += ((IManaItem) item).getMana(stack);
						totalMaxMana += ((IManaItem) item).getMaxMana(stack);
					}
					if (item instanceof ICreativeManaProvider && ((ICreativeManaProvider) item).isCreative(stack)) {
						creative = true;
					}
				}

				List<ItemStack> acc = ManaItemHandler.instance().getManaAccesories(player);
				for (ItemStack stack : acc) {
					Item item = stack.getItem();
					if (!((IManaItem) item).isNoExport(stack)) {
						totalMana += ((IManaItem) item).getMana(stack);
						totalMaxMana += ((IManaItem) item).getMaxMana(stack);
					}
					if (item instanceof ICreativeManaProvider && ((ICreativeManaProvider) item).isCreative(stack)) {
						creative = true;
					}
				}

				if (anyRequest) {
					renderManaInvBar(ms, creative, totalMana, totalMaxMana);
				}
			}

			profiler.endStartSection("itemsRemaining");
			ItemsRemainingRenderHandler.render(ms, event.getPartialTicks());
			profiler.endSection();
			profiler.endSection();

			RenderSystem.color4f(1F, 1F, 1F, 1F);
		}
	}

	private static void renderManaInvBar(MatrixStack ms, boolean hasCreative, int totalMana, int totalMaxMana) {
		Minecraft mc = Minecraft.getInstance();
		int width = 182;
		int x = mc.getMainWindow().getScaledWidth() / 2 - width / 2;
		int y = mc.getMainWindow().getScaledHeight() - ConfigHandler.CLIENT.manaBarHeight.get();

		if (!hasCreative) {
			if (totalMaxMana == 0) {
				width = 0;
			} else {
				width *= (double) totalMana / (double) totalMaxMana;
			}
		}

		if (width == 0) {
			if (totalMana > 0) {
				width = 1;
			} else {
				return;
			}
		}

		int color = MathHelper.hsvToRGB(0.55F, (float) Math.min(1F, Math.sin(Util.milliTime() / 200D) * 0.5 + 1F), 1F);
		int r = (color >> 16 & 0xFF);
		int g = (color >> 8 & 0xFF);
		int b = color & 0xFF;
		RenderSystem.color4f(r / 255F, g / 255F, b / 255F, 1 - (r / 255F));
		mc.textureManager.bindTexture(manaBar);

		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.drawTexturedModalRect(ms, x, y, 0, 251, width, 5);
		RenderSystem.disableBlend();
		RenderSystem.color4f(1, 1, 1, 1);
	}

	private static void renderPoolRecipeHUD(MatrixStack ms, TilePool tile, ItemStack stack) {
		Minecraft mc = Minecraft.getInstance();
		IProfiler profiler = mc.getProfiler();

		profiler.startSection("poolRecipe");
		IManaInfusionRecipe recipe = tile.getMatchingRecipe(stack, tile.getWorld().getBlockState(tile.getPos().down()));
		if (recipe != null) {
			int x = mc.getMainWindow().getScaledWidth() / 2 - 11;
			int y = mc.getMainWindow().getScaledHeight() / 2 + 10;

			int u = tile.getCurrentMana() >= recipe.getManaToConsume() ? 0 : 22;
			int v = mc.player.getName().getString().equals("haighyorkie") && mc.player.isSneaking() ? 23 : 8;

			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			mc.textureManager.bindTexture(manaBar);
			RenderHelper.drawTexturedModalRect(ms, x, y, u, v, 22, 15);
			RenderSystem.color4f(1F, 1F, 1F, 1F);

			mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, x - 20, y);
			mc.getItemRenderer().renderItemAndEffectIntoGUI(recipe.getRecipeOutput(), x + 26, y);
			mc.getItemRenderer().renderItemOverlays(mc.fontRenderer, recipe.getRecipeOutput(), x + 26, y);

			RenderSystem.disableLighting();
			RenderSystem.disableBlend();
		}
		profiler.endSection();
	}

	private static void renderCrystalCubeHUD(MatrixStack ms, TileCorporeaCrystalCube tile) {
		Minecraft mc = Minecraft.getInstance();
		IProfiler profiler = mc.getProfiler();

		profiler.startSection("crystalCube");
		ItemStack target = tile.getRequestTarget();
		if (!target.isEmpty()) {
			String s1 = target.getDisplayName().getString();
			String s2 = tile.getItemCount() + "x";
			int strlen = Math.max(mc.fontRenderer.getStringWidth(s1), mc.fontRenderer.getStringWidth(s2));
			int w = mc.getMainWindow().getScaledWidth();
			int h = mc.getMainWindow().getScaledHeight();
			int boxH = h / 2 + (tile.locked ? 20 : 10);
			AbstractGui.fill(ms, w / 2 + 8, h / 2 - 12, w / 2 + strlen + 32, boxH, 0x44000000);
			AbstractGui.fill(ms, w / 2 + 6, h / 2 - 14, w / 2 + strlen + 34, boxH + 2, 0x44000000);

			mc.fontRenderer.drawStringWithShadow(ms, s1, w / 2 + 30, h / 2 - 10, 0x6666FF);
			mc.fontRenderer.drawStringWithShadow(ms, tile.getItemCount() + "x", w / 2 + 30, h / 2, 0xFFFFFF);
			if (tile.locked) {
				mc.fontRenderer.drawStringWithShadow(ms, I18n.format("botaniamisc.locked"), w / 2 + 30, h / 2 + 10, 0xFFAA00);
			}
			RenderSystem.enableRescaleNormal();
			mc.getItemRenderer().renderItemAndEffectIntoGUI(target, w / 2 + 10, h / 2 - 10);
		}

		profiler.endSection();
	}

	private static void renderNearIndexDisplay(MatrixStack ms) {
		Minecraft mc = Minecraft.getInstance();
		String txt0 = I18n.format("botaniamisc.nearIndex0");
		String txt1 = TextFormatting.GRAY + I18n.format("botaniamisc.nearIndex1");
		String txt2 = TextFormatting.GRAY + I18n.format("botaniamisc.nearIndex2");

		int l = Math.max(mc.fontRenderer.getStringWidth(txt0), Math.max(mc.fontRenderer.getStringWidth(txt1), mc.fontRenderer.getStringWidth(txt2))) + 20;
		int x = mc.getMainWindow().getScaledWidth() - l - 20;
		int y = mc.getMainWindow().getScaledHeight() - 60;

		AbstractGui.fill(ms, x - 6, y - 6, x + l + 6, y + 37, 0x44000000);
		AbstractGui.fill(ms, x - 4, y - 4, x + l + 4, y + 35, 0x44000000);
		RenderSystem.enableRescaleNormal();
		mc.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(ModBlocks.corporeaIndex), x, y + 10);

		mc.fontRenderer.drawStringWithShadow(ms, txt0, x + 20, y, 0xFFFFFF);
		mc.fontRenderer.drawStringWithShadow(ms, txt1, x + 20, y + 14, 0xFFFFFF);
		mc.fontRenderer.drawStringWithShadow(ms, txt2, x + 20, y + 24, 0xFFFFFF);
	}

	public static void drawSimpleManaHUD(MatrixStack ms, int color, int mana, int maxMana, String name) {
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft mc = Minecraft.getInstance();
		int x = mc.getMainWindow().getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(name) / 2;
		int y = mc.getMainWindow().getScaledHeight() / 2 + 10;

		mc.fontRenderer.drawStringWithShadow(ms, name, x, y, color);

		x = mc.getMainWindow().getScaledWidth() / 2 - 51;
		y += 10;

		renderManaBar(ms, x, y, color, 1F, mana, maxMana);

		RenderSystem.disableBlend();
	}

	public static void drawComplexManaHUD(int color, MatrixStack ms, int mana, int maxMana, String name, ItemStack bindDisplay, boolean properlyBound) {
		drawSimpleManaHUD(ms, color, mana, maxMana, name);

		Minecraft mc = Minecraft.getInstance();

		int x = mc.getMainWindow().getScaledWidth() / 2 + 55;
		int y = mc.getMainWindow().getScaledHeight() / 2 + 12;

		RenderSystem.enableRescaleNormal();
		mc.getItemRenderer().renderItemAndEffectIntoGUI(bindDisplay, x, y);

		RenderSystem.disableDepthTest();
		if (properlyBound) {
			mc.fontRenderer.drawStringWithShadow(ms, "\u2714", x + 10, y + 9, 0x004C00);
			mc.fontRenderer.drawStringWithShadow(ms, "\u2714", x + 10, y + 8, 0x0BD20D);
		} else {
			mc.fontRenderer.drawStringWithShadow(ms, "\u2718", x + 10, y + 9, 0x4C0000);
			mc.fontRenderer.drawStringWithShadow(ms, "\u2718", x + 10, y + 8, 0xD2080D);
		}
		RenderSystem.enableDepthTest();
	}

	public static void renderManaBar(MatrixStack ms, int x, int y, int color, float alpha, int mana, int maxMana) {
		Minecraft mc = Minecraft.getInstance();

		RenderSystem.color4f(1F, 1F, 1F, alpha);
		mc.textureManager.bindTexture(manaBar);
		RenderHelper.drawTexturedModalRect(ms, x, y, 0, 0, 102, 5);

		int manaPercentage = Math.max(0, (int) ((double) mana / (double) maxMana * 100));

		if (manaPercentage == 0 && mana > 0) {
			manaPercentage = 1;
		}

		RenderHelper.drawTexturedModalRect(ms, x + 1, y + 1, 0, 5, 100, 3);

		float red = (color >> 16 & 0xFF) / 255F;
		float green = (color >> 8 & 0xFF) / 255F;
		float blue = (color & 0xFF) / 255F;
		RenderSystem.color4f(red, green, blue, alpha);
		RenderHelper.drawTexturedModalRect(ms, x + 1, y + 1, 0, 5, Math.min(100, manaPercentage), 3);
		RenderSystem.color4f(1, 1, 1, 1);
	}
}
