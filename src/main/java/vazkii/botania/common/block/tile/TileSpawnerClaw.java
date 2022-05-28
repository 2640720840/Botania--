/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.AbstractSpawner;

import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.mixin.AccessorAbstractSpawner;

import java.util.Optional;

public class TileSpawnerClaw extends TileMod implements IManaReceiver, ITickableTileEntity {
	private static final String TAG_MANA = "mana";
	private static final int MAX_MANA = 160;

	private int mana = 0;

	public TileSpawnerClaw() {
		super(ModTiles.SPAWNER_CLAW);
	}

	@Override
	public void tick() {
		TileEntity tileBelow = world.getTileEntity(pos.down());
		if (mana >= 5 && tileBelow instanceof MobSpawnerTileEntity) {
			MobSpawnerTileEntity spawner = (MobSpawnerTileEntity) tileBelow;
			AbstractSpawner logic = spawner.getSpawnerBaseLogic();
			AccessorAbstractSpawner mLogic = (AccessorAbstractSpawner) logic;

			// [VanillaCopy] AbstractSpawner.tick, edits noted
			if (!mLogic.botania_isActivated()) { // Activate when vanilla is *not* running the spawner
				World world = this.getWorld();
				BlockPos blockpos = logic.getSpawnerPosition();
				if (world.isRemote) {
					// Botania - use own particles
					if (Math.random() > 0.5) {
						WispParticleData data = WispParticleData.wisp((float) Math.random() / 3F, 0.6F - (float) Math.random() * 0.3F, 0.1F, 0.6F - (float) Math.random() * 0.3F, 2F);
						world.addParticle(data, getPos().getX() + 0.3 + Math.random() * 0.5, getPos().getY() - 0.3 + Math.random() * 0.25, getPos().getZ() + Math.random(), 0, -(-0.025F - 0.005F * (float) Math.random()), 0);
					}
					if (mLogic.getSpawnDelay() > 0) {
						mLogic.setSpawnDelay(mLogic.getSpawnDelay() - 1);
					}

					mLogic.setPrevMobRotation(mLogic.getMobRotation());
					mLogic.setMobRotation((mLogic.getMobRotation() + (double) (1000.0F / ((float) mLogic.getSpawnDelay() + 200.0F))) % 360.0D);
				} else {
					// Botania - consume mana
					this.mana -= 6;

					if (mLogic.getSpawnDelay() == -1) {
						mLogic.botania_resetTimer();
					}

					if (mLogic.getSpawnDelay() > 0) {
						mLogic.setSpawnDelay(mLogic.getSpawnDelay() - 1);
						return;
					}

					boolean flag = false;

					for (int i = 0; i < mLogic.getSpawnCount(); ++i) {
						CompoundNBT compoundnbt = mLogic.getSpawnData().getNbt();
						Optional<EntityType<?>> optional = EntityType.readEntityType(compoundnbt);
						if (!optional.isPresent()) {
							mLogic.botania_resetTimer();
							return;
						}

						ListNBT listnbt = compoundnbt.getList("Pos", 6);
						int j = listnbt.size();
						double d0 = j >= 1 ? listnbt.getDouble(0) : (double) blockpos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double) mLogic.getSpawnRange() + 0.5D;
						double d1 = j >= 2 ? listnbt.getDouble(1) : (double) (blockpos.getY() + world.rand.nextInt(3) - 1);
						double d2 = j >= 3 ? listnbt.getDouble(2) : (double) blockpos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double) mLogic.getSpawnRange() + 0.5D;
						if (world.hasNoCollisions(optional.get().getBoundingBoxWithSizeApplied(d0, d1, d2))) {
							ServerWorld serverworld = (ServerWorld) world;
							if (EntitySpawnPlacementRegistry.canSpawnEntity(optional.get(), serverworld, SpawnReason.SPAWNER, new BlockPos(d0, d1, d2), world.getRandom())) {
								Entity entity = EntityType.loadEntityAndExecute(compoundnbt, world, (p_221408_6_) -> {
									p_221408_6_.setLocationAndAngles(d0, d1, d2, p_221408_6_.rotationYaw, p_221408_6_.rotationPitch);
									return p_221408_6_;
								});
								if (entity == null) {
									mLogic.botania_resetTimer();
									return;
								}

								int k = world.getEntitiesWithinAABB(entity.getClass(), (new AxisAlignedBB((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ(), (double) (blockpos.getX() + 1), (double) (blockpos.getY() + 1), (double) (blockpos.getZ() + 1))).grow((double) mLogic.getSpawnRange())).size();
								if (k >= mLogic.getMaxNearbyEntities()) {
									mLogic.botania_resetTimer();
									return;
								}

								entity.setLocationAndAngles(entity.getPosX(), entity.getPosY(), entity.getPosZ(), world.rand.nextFloat() * 360.0F, 0.0F);
								if (entity instanceof MobEntity) {
									MobEntity mobentity = (MobEntity) entity;
									if (!net.minecraftforge.event.ForgeEventFactory.canEntitySpawnSpawner(mobentity, world, (float) entity.getPosX(), (float) entity.getPosY(), (float) entity.getPosZ(), logic)) {
										continue;
									}

									if (mLogic.getSpawnData().getNbt().size() == 1 && mLogic.getSpawnData().getNbt().contains("id", 8)) {
										if (!net.minecraftforge.event.ForgeEventFactory.doSpecialSpawn(mobentity, world, (float) entity.getPosX(), (float) entity.getPosY(), (float) entity.getPosZ(), logic, SpawnReason.SPAWNER)) {
											((MobEntity) entity).onInitialSpawn((ServerWorld) world, world.getDifficultyForLocation(entity.getPosition()), SpawnReason.SPAWNER, (ILivingEntityData) null, (CompoundNBT) null);
										}
									}
								}

								if (!serverworld.func_242106_g(entity)) {
									mLogic.botania_resetTimer();
									return;
								}
								world.playEvent(2004, blockpos, 0);
								if (entity instanceof MobEntity) {
									((MobEntity) entity).spawnExplosionParticle();
								}

								flag = true;
							}
						}

						if (flag) {
							mLogic.botania_resetTimer();
						}
					}
				}
			}
		}
	}

	@Override
	public void writePacketNBT(CompoundNBT cmp) {
		cmp.putInt(TAG_MANA, mana);
	}

	@Override
	public void readPacketNBT(CompoundNBT cmp) {
		mana = cmp.getInt(TAG_MANA);
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana >= MAX_MANA;
	}

	@Override
	public void receiveMana(int mana) {
		this.mana = Math.min(3 * MAX_MANA, this.mana + mana);
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return true;
	}

}
