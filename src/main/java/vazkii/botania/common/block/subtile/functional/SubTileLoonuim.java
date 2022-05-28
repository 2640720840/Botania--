/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lib.ModTags;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SubTileLoonuim extends TileEntityFunctionalFlower {
	private static final int COST = 35000;
	private static final int RANGE = 5;
	private static final String TAG_LOOT_TABLE = "lootTable";
	private static final String TAG_ITEMSTACK_TO_DROP = "botania:looniumItemStackToDrop";

	private ResourceLocation lootTable = new ResourceLocation("minecraft", "chests/simple_dungeon");

	public SubTileLoonuim() {
		super(ModSubtiles.LOONIUM);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		World world = getWorld();
		if (!world.isRemote && redstoneSignal == 0 && ticksExisted % 100 == 0
				&& getMana() >= COST && world.getDifficulty() != Difficulty.PEACEFUL) {
			Random rand = world.rand;

			ItemStack stack;
			do {
				LootContext ctx = new LootContext.Builder((ServerWorld) world).build(LootParameterSets.EMPTY);
				List<ItemStack> stacks = ((ServerWorld) world).getServer().getLootTableManager()
						.getLootTableFromLocation(lootTable).generate(ctx);
				if (stacks.isEmpty()) {
					return;
				} else {
					Collections.shuffle(stacks);
					stack = stacks.get(0);
				}
			} while (stack.isEmpty() || ModTags.Items.LOONIUM_BLACKLIST.contains(stack.getItem()));

			int bound = RANGE * 2 + 1;
			int xp = getEffectivePos().getX() - RANGE + rand.nextInt(bound);
			int yp = getEffectivePos().getY();
			int zp = getEffectivePos().getZ() - RANGE + rand.nextInt(bound);

			BlockPos pos = new BlockPos(xp, yp - 1, zp);
			do {
				pos = pos.up();
				if (pos.getY() >= 254) {
					return;
				}
			} while (world.getBlockState(pos).isSuffocating(world, pos));
			pos = pos.up();

			double x = pos.getX() + Math.random();
			double y = pos.getY() + Math.random();
			double z = pos.getZ() + Math.random();

			MonsterEntity entity = null;
			if (world.rand.nextInt(50) == 0) {
				entity = new EndermanEntity(EntityType.ENDERMAN, world);
			} else if (world.rand.nextInt(10) == 0) {
				entity = new CreeperEntity(EntityType.CREEPER, world);
				if (world.rand.nextInt(200) == 0) {
					CompoundNBT charged = new CompoundNBT();
					charged.putBoolean("powered", true);
					entity.readAdditional(charged);
				}
			} else {
				switch (world.rand.nextInt(3)) {
				case 0:
					if (world.rand.nextInt(10) == 0) {
						entity = new HuskEntity(EntityType.HUSK, world);
					} else if (world.rand.nextInt(5) == 0) {
						entity = new DrownedEntity(EntityType.DROWNED, world);
					} else {
						entity = new ZombieEntity(world);
					}
					break;
				case 1:
					if (world.rand.nextInt(10) == 0) {
						entity = new StrayEntity(EntityType.STRAY, world);
					} else {
						entity = new SkeletonEntity(EntityType.SKELETON, world);
					}
					break;
				case 2:
					if (world.rand.nextInt(10) == 0) {
						entity = new CaveSpiderEntity(EntityType.CAVE_SPIDER, world);
					} else {
						entity = new SpiderEntity(EntityType.SPIDER, world);
					}
					break;
				}
			}

			entity.setPositionAndRotation(x, y, z, world.rand.nextFloat() * 360F, 0);
			entity.setMotion(Vector3d.ZERO);

			entity.getAttribute(Attributes.MAX_HEALTH).applyPersistentModifier(new AttributeModifier("Loonium Modififer Health", 2, AttributeModifier.Operation.MULTIPLY_BASE));
			entity.getAttribute(Attributes.ATTACK_DAMAGE).applyPersistentModifier(new AttributeModifier("Loonium Modififer Damage", 1.5, AttributeModifier.Operation.MULTIPLY_BASE));

			entity.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE,
					entity instanceof CreeperEntity ? 100 : Integer.MAX_VALUE, 0));
			entity.addPotionEffect(new EffectInstance(Effects.REGENERATION,
					entity instanceof CreeperEntity ? 100 : Integer.MAX_VALUE, 0));

			CompoundNBT cmp = stack.write(new CompoundNBT());
			entity.getPersistentData().put(TAG_ITEMSTACK_TO_DROP, cmp);

			entity.onInitialSpawn((ServerWorld) world, world.getDifficultyForLocation(pos), SpawnReason.SPAWNER, null, null);
			world.addEntity(entity);
			entity.spawnExplosionParticle();

			addMana(-COST);
			sync();
		}
	}

	@Override
	public int getColor() {
		return 0x274A00;
	}

	@Override
	public int getMaxMana() {
		return COST;
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

	@Override
	public void readFromPacketNBT(CompoundNBT cmp) {
		super.readFromPacketNBT(cmp);
		if (cmp.contains(TAG_LOOT_TABLE)) {
			lootTable = new ResourceLocation(cmp.getString(TAG_LOOT_TABLE));
		}
	}

	@Override
	public void writeToPacketNBT(CompoundNBT cmp) {
		super.writeToPacketNBT(cmp);
		cmp.putString(TAG_LOOT_TABLE, lootTable.toString());
	}

	public static void onDrops(LivingDropsEvent event) {
		LivingEntity e = event.getEntityLiving();
		if (e.getPersistentData().contains(TAG_ITEMSTACK_TO_DROP)) {
			CompoundNBT cmp = e.getPersistentData().getCompound(TAG_ITEMSTACK_TO_DROP);
			ItemStack stack = ItemStack.read(cmp);
			event.getDrops().clear();
			event.getDrops().add(new ItemEntity(e.world, e.getPosX(), e.getPosY(), e.getPosZ(), stack));
		}
	}
}
