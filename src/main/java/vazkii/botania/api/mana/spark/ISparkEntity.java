/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana.spark;

import net.minecraft.item.DyeColor;

import java.util.Collection;

/**
 * An Entity that implements this is considered a Spark.
 */
public interface ISparkEntity {

	/**
	 * Which TileEntity is this Spark attached to? A common implementation is checking the block below.
	 * using world.getTileEntity(new BlockPos(this).down())
	 */
	ISparkAttachable getAttachedTile();

	/**
	 * Gets a collection of all Sparks this is tranfering to.
	 */
	Collection<ISparkEntity> getTransfers();

	/**
	 * Registers the Spark passed in as a Spark meant for mana to be transfered towards.
	 */
	void registerTransfer(ISparkEntity entity);

	SparkUpgradeType getUpgrade();

	void setUpgrade(SparkUpgradeType upgrade);

	/**
	 * See {@link ISparkAttachable#areIncomingTranfersDone()}
	 */
	boolean areIncomingTransfersDone();

	/**
	 * Gets the network that this spark is on, or the color it's displaying. Sparks may only connect to others
	 * of the same network.
	 */
	DyeColor getNetwork();
}
