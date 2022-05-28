/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.helper;

/**
 * A Callback for when a shader is called. Used to define shader uniforms.
 */
@FunctionalInterface
public interface ShaderCallback {

	void call(int shader);

}
