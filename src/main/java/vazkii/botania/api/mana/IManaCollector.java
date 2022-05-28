/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import vazkii.botania.api.internal.IManaBurst;

/**
 * Any TileEntity that implements this is considered a mana collector, by
 * which nearby generating flowers will pump mana into it.<br>
 * <br>
 * <b>Implementation Instructions:</b><br>
 * - Override invalidate() and onChunkUnload(), calling <i>ManaNetworkEvent.removeCollector(this);</i> on both.<br>
 * - On the first tick of update() (or onLoad() in Forge build 1606 or higher), call
 * </i>ManaNetworkEvent.addCollector(this);<i>
 */
public interface IManaCollector extends IManaReceiver {

	/**
	 * Called every tick on the client case the player is holding a Wand of the Forest.
	 */
	void onClientDisplayTick();

	/**
	 * Get the multiplier of mana to input into the block, 1.0 is the original amount of mana
	 * in the burst. 0.9, for example, is 90%, so 10% of the mana in the burst will get
	 * dissipated.
	 */
	float getManaYieldMultiplier(IManaBurst burst);

	/**
	 * Gets the maximum amount of mana this collector can have.
	 */
	int getMaxMana();

}
