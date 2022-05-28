/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.common.item.ItemCacophonium;

public class TileCacophonium extends TileMod {
	private static final String TAG_STACK = "stack";

	public ItemStack stack = ItemStack.EMPTY;

	public TileCacophonium() {
		super(ModTiles.CACOPHONIUM);
	}

	public void annoyDirewolf() {
		ItemCacophonium.playSound(world, stack, pos.getX(), pos.getY(), pos.getZ(), SoundCategory.BLOCKS, 1F);
		if (!world.isRemote) {
			float noteColor = world.rand.nextInt(25) / 24.0F;
			((ServerWorld) world).spawnParticle(ParticleTypes.NOTE, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 0, noteColor, 0, 0, 1);
		}
	}

	@Override
	public void writePacketNBT(CompoundNBT cmp) {
		super.writePacketNBT(cmp);

		CompoundNBT cmp1 = new CompoundNBT();
		if (!stack.isEmpty()) {
			cmp1 = stack.write(cmp1);
		}
		cmp.put(TAG_STACK, cmp1);
	}

	@Override
	public void readPacketNBT(CompoundNBT cmp) {
		super.readPacketNBT(cmp);

		CompoundNBT cmp1 = cmp.getCompound(TAG_STACK);
		stack = ItemStack.read(cmp1);
	}

}
