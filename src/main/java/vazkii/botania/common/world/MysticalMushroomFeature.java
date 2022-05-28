/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;

import java.util.Random;

public class MysticalMushroomFeature extends Feature<MysticalMushroomConfig> {
	public MysticalMushroomFeature() {
		super(MysticalMushroomConfig.CODEC);
	}

	@Override
	public boolean generate(@Nonnull ISeedReader world, @Nonnull ChunkGenerator generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull MysticalMushroomConfig config) {
		boolean any = false;
		for (int i = 0; i < config.getMushroomPatchSize(); i++) {
			int x = pos.getX() + rand.nextInt(16) + 8;
			int z = pos.getZ() + rand.nextInt(16) + 8;
			int y = rand.nextInt(26) + 4;
			BlockPos pos3 = new BlockPos(x, y, z);
			DyeColor color = DyeColor.byId(rand.nextInt(16));
			BlockState mushroom = ModBlocks.getMushroom(color).getDefaultState();
			if (world.isAirBlock(pos3) && mushroom.isValidPosition(world, pos3)) {
				world.setBlockState(pos3, mushroom, 2);
				any = true;
			}
		}
		return any;
	}
}
