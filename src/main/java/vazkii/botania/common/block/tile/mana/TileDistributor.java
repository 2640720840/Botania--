/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.mana;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileMod;

import java.util.ArrayList;
import java.util.List;

public class TileDistributor extends TileMod implements IManaReceiver, ITickableTileEntity {
	private final List<IManaReceiver> validPools = new ArrayList<>();

	public TileDistributor() {
		super(ModTiles.DISTRIBUTOR);
	}

	@Override
	public void tick() {
		if (world.isRemote) {
			return;
		}
		validPools.clear();
		for (Direction dir : Direction.Plane.HORIZONTAL) {
			BlockPos pos = this.pos.offset(dir);
			if (world.isBlockLoaded(pos)) {
				TileEntity tileAt = world.getTileEntity(pos);
				if (tileAt instanceof IManaPool && !tileAt.isRemoved()) {
					IManaReceiver receiver = (IManaReceiver) tileAt;
					if (!receiver.isFull()) {
						validPools.add(receiver);
					}
				}
			}
		}
	}

	@Override
	public int getCurrentMana() {
		return 0;
	}

	@Override
	public boolean isFull() {
		return validPools.isEmpty();
	}

	@Override
	public void receiveMana(int mana) {
		int tiles = validPools.size();
		if (tiles != 0) {
			int manaForEach = mana / tiles;
			for (IManaReceiver pool : validPools) {
				pool.receiveMana(manaForEach);
			}
		}
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return !isFull();
	}
}
