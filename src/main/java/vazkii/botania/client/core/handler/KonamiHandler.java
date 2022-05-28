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

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;

import org.lwjgl.glfw.GLFW;

import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.patchouli.api.BookDrawScreenEvent;

public class KonamiHandler {
	private static final int[] KONAMI_CODE = {
			GLFW.GLFW_KEY_UP, GLFW.GLFW_KEY_UP,
			GLFW.GLFW_KEY_DOWN, GLFW.GLFW_KEY_DOWN,
			GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_RIGHT,
			GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_RIGHT,
			GLFW.GLFW_KEY_B, GLFW.GLFW_KEY_A,
	};
	private static int nextLetter = 0;
	private static int konamiTime = 0;

	public static void clientTick(TickEvent.ClientTickEvent evt) {
		if (konamiTime > 0) {
			konamiTime--;
		}

		if (!ItemLexicon.isOpen()) {
			nextLetter = 0;
		}
	}

	public static void handleInput(InputEvent.KeyInputEvent evt) {
		Minecraft mc = Minecraft.getInstance();
		if (evt.getModifiers() == 0 && evt.getAction() == GLFW.GLFW_PRESS && ItemLexicon.isOpen()) {
			if (konamiTime == 0 && evt.getKey() == KONAMI_CODE[nextLetter]) {
				nextLetter++;
				if (nextLetter >= KONAMI_CODE.length) {
					mc.getSoundHandler().play(SimpleSound.master(ModSounds.way, 1.0F));
					nextLetter = 0;
					konamiTime = 240;
				}
			} else {
				nextLetter = 0;
			}
		}
	}

	public static void renderBook(BookDrawScreenEvent evt) {
		if (konamiTime > 0) {
			MatrixStack ms = evt.matrixStack;
			String meme = I18n.format("botania.subtitle.way");
			RenderSystem.disableDepthTest();
			ms.push();
			int fullWidth = Minecraft.getInstance().fontRenderer.getStringWidth(meme);
			int left = evt.gui.width;
			double widthPerTick = (fullWidth + evt.gui.width) / 240;
			double currWidth = left - widthPerTick * (240 - (konamiTime - evt.partialTicks)) * 3.2;

			ms.translate(currWidth, evt.gui.height / 2 - 10, 0);
			ms.scale(4, 4, 4);
			Minecraft.getInstance().fontRenderer.drawStringWithShadow(evt.matrixStack, meme, 0, 0, 0xFFFFFF);
			ms.pop();
			RenderSystem.enableDepthTest();
		}
	}
}
