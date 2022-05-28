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
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.core.helper.ShaderCallback;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.entity.EntityDoppleganger;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

public final class BossBarHandler {

	private BossBarHandler() {}

	// Only access on the client thread!
	public static final Set<EntityDoppleganger> bosses = Collections.newSetFromMap(new WeakHashMap<>());
	public static final ResourceLocation defaultBossBar = new ResourceLocation(LibResources.GUI_BOSS_BAR);
	private static final BarCallback barUniformCallback = new BarCallback();

	public static void onBarRender(RenderGameOverlayEvent.BossInfo evt) {
		UUID infoUuid = evt.getBossInfo().getUniqueId();
		for (EntityDoppleganger currentBoss : bosses) {
			if (currentBoss.getBossInfoUuid().equals(infoUuid)) {
				MatrixStack ms = evt.getMatrixStack();
				evt.setCanceled(true);

				Minecraft mc = Minecraft.getInstance();
				Rectangle2d bgRect = currentBoss.getBossBarTextureRect();
				Rectangle2d fgRect = currentBoss.getBossBarHPTextureRect();
				ITextComponent name = evt.getBossInfo().getName();
				int c = Minecraft.getInstance().getMainWindow().getScaledWidth() / 2;
				int x = evt.getX();
				int y = evt.getY();
				int xf = x + (bgRect.getWidth() - fgRect.getWidth()) / 2;
				int yf = y + (bgRect.getHeight() - fgRect.getHeight()) / 2;
				int fw = (int) ((double) fgRect.getWidth() * evt.getBossInfo().getPercent());
				int tx = c - mc.fontRenderer.getStringPropertyWidth(name) / 2;

				RenderSystem.color4f(1F, 1F, 1F, 1F);
				int auxHeight = currentBoss.bossBarRenderCallback(ms, x, y);
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				mc.textureManager.bindTexture(currentBoss.getBossBarTexture());
				drawBar(ms, currentBoss, x, y, bgRect.getX(), bgRect.getY(), bgRect.getWidth(), bgRect.getHeight(), true);
				drawBar(ms, currentBoss, xf, yf, fgRect.getX(), fgRect.getY(), fw, fgRect.getHeight(), false);
				mc.fontRenderer.func_243246_a(ms, name, tx, y - 10, 0xA2018C);
				RenderSystem.enableBlend();
				evt.setIncrement(Math.max(bgRect.getHeight(), fgRect.getHeight()) + auxHeight + mc.fontRenderer.FONT_HEIGHT);
			}
		}
	}

	private static void drawBar(MatrixStack ms, EntityDoppleganger currentBoss, int x, int y, int u, int v, int w, int h, boolean bg) {
		ShaderHelper.BotaniaShader program = currentBoss.getBossBarShaderProgram(bg);

		if (program != null) {
			ShaderCallback callback = currentBoss.getBossBarShaderCallback(bg);
			barUniformCallback.set(u, v, callback);
			ShaderHelper.useShader(program, barUniformCallback);
		}

		RenderHelper.drawTexturedModalRect(ms, x, y, u, v, w, h);

		if (program != null) {
			ShaderHelper.releaseShader();
		}
	}

	private static class BarCallback implements ShaderCallback {
		int x, y;
		ShaderCallback callback;

		@Override
		public void call(int shader) {
			int startXUniform = GlStateManager.getUniformLocation(shader, "startX");
			int startYUniform = GlStateManager.getUniformLocation(shader, "startY");

			GlStateManager.uniform1i(startXUniform, x);
			GlStateManager.uniform1i(startYUniform, y);

			if (callback != null) {
				callback.call(shader);
			}
		}

		void set(int x, int y, ShaderCallback callback) {
			this.x = x;
			this.y = y;
			this.callback = callback;
		}
	}

}
