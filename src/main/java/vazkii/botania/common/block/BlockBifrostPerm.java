/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.decor.BlockModGlass;

import java.util.Random;

public class BlockBifrostPerm extends BlockModGlass {
	public BlockBifrostPerm(Properties builder) {
		super(builder);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		if (rand.nextBoolean()) {
			SparkleParticleData data = SparkleParticleData.sparkle(0.45F + 0.2F * (float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 6);
			world.addParticle(data, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0, 0);
		}
	}

	@Override
	public float[] getBeaconColorMultiplier(BlockState state, IWorldReader world, BlockPos pos, BlockPos beaconPos) {
		int rgb = MathHelper.hsvToRGB(((World) world).getGameTime() * 5 % 360 / 360F, 0.4F, 0.9F);
		float[] ret = new float[3];
		ret[0] = ((rgb >> 16) & 0xFF) / 255.0F;
		ret[1] = ((rgb >> 8) & 0xFF) / 255.0F;
		ret[2] = (rgb & 0xFF) / 255.0F;
		return ret;
	}
}
