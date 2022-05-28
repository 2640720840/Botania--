/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.block.tile.mana.TilePump;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class EntityPoolMinecart extends AbstractMinecartEntity {
	private static final int TRANSFER_RATE = 10000;
	private static final String TAG_MANA = "mana";
	private static final DataParameter<Integer> MANA = EntityDataManager.createKey(EntityPoolMinecart.class, DataSerializers.VARINT);

	public EntityPoolMinecart(EntityType<EntityPoolMinecart> type, World world) {
		super(type, world);
	}

	public EntityPoolMinecart(World world, double x, double y, double z) {
		super(ModEntities.POOL_MINECART, world, x, y, z);
	}

	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(MANA, 0);
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Nonnull
	@Override
	public BlockState getDisplayTile() {
		return ModBlocks.manaPool.getDefaultState();
	}

	@Nonnull
	@Override
	public ItemStack getCartItem() {
		return new ItemStack(ModItems.poolMinecart);
	}

	@Nonnull
	@Override
	public AbstractMinecartEntity.Type getMinecartType() {
		return Type.RIDEABLE;
	}

	@Override
	public boolean canBeRidden() {
		return false;
	}

	@Override
	protected void applyDrag() {
		float f = 0.98F;
		this.setMotion(getMotion().mul(f, 0, f));
	}

	@Nonnull
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ModItems.poolMinecart);
	}

	@Override
	public void killMinecart(DamageSource source) {
		super.killMinecart(source);
		entityDropItem(ModBlocks.manaPool, 0);
	}

	@Override
	public int getDefaultDisplayTileOffset() {
		return 8;
	}

	@Override
	public void tick() {
		super.tick();

		if (world.isRemote) {
			double particleChance = 1F - (double) getMana() / (double) TilePool.MAX_MANA * 0.1;
			int color = TilePool.PARTICLE_COLOR;
			float red = (color >> 16 & 0xFF) / 255F;
			float green = (color >> 8 & 0xFF) / 255F;
			float blue = (color & 0xFF) / 255F;
			double x = MathHelper.floor(getPosX());
			double y = MathHelper.floor(getPosY());
			double z = MathHelper.floor(getPosZ());
			if (Math.random() > particleChance) {
				WispParticleData data = WispParticleData.wisp((float) Math.random() / 3F, red, green, blue, 2F);
				world.addParticle(data, x + 0.3 + Math.random() * 0.5, y + 0.85 + Math.random() * 0.25, z + Math.random(), 0, (float) Math.random() / 25F, 0);
			}
		}
	}

	@Override
	public void moveMinecartOnRail(BlockPos pos) {
		super.moveMinecartOnRail(pos);

		for (Direction dir : Direction.Plane.HORIZONTAL) {
			BlockPos posP = pos.offset(dir);
			Block block = world.getBlockState(posP).getBlock();
			if (block == ModBlocks.pump) {
				BlockPos posP_ = posP.offset(dir);
				TileEntity tile = world.getTileEntity(posP_);
				TileEntity tile_ = world.getTileEntity(posP);
				TilePump pump = (TilePump) tile_;

				if (tile instanceof IManaPool) {
					IManaPool pool = (IManaPool) tile;
					Direction pumpDir = world.getBlockState(posP).get(BlockStateProperties.HORIZONTAL_FACING);
					boolean did = false;
					boolean can = false;

					if (pumpDir == dir) { // Pool -> Cart
						can = true;

						if (!pump.hasRedstone) {
							int cartMana = getMana();
							int poolMana = pool.getCurrentMana();
							int transfer = Math.min(TRANSFER_RATE, poolMana);
							int actualTransfer = Math.min(TilePool.MAX_MANA - cartMana, transfer);
							if (actualTransfer > 0) {
								pool.receiveMana(-transfer);
								setMana(cartMana + actualTransfer);
								did = true;
							}
						}
					} else if (pumpDir == dir.getOpposite()) { // Cart -> Pool
						can = true;

						if (!pump.hasRedstone && !pool.isFull()) {
							int cartMana = getMana();
							int transfer = Math.min(TRANSFER_RATE, cartMana);
							if (transfer > 0) {
								pool.receiveMana(transfer);
								setMana(cartMana - transfer);
								did = true;
							}
						}
					}

					if (did) {
						pump.hasCart = true;
						pump.setActive(true);
					}

					if (can) {
						pump.hasCartOnTop = true;
						pump.comparator = (int) ((double) getMana() / (double) TilePool.MAX_MANA * 15); // different from TilePool.calculateComparatorLevel, kept for compatibility
					}

				}
			}
		}
	}

	@Override
	protected void writeAdditional(@Nonnull CompoundNBT cmp) {
		super.writeAdditional(cmp);
		cmp.putInt(TAG_MANA, getMana());
	}

	@Override
	protected void readAdditional(CompoundNBT cmp) {
		super.readAdditional(cmp);
		setMana(cmp.getInt(TAG_MANA));
	}

	@Override
	public int getComparatorLevel() {
		return TilePool.calculateComparatorLevel(getMana(), TilePool.MAX_MANA);
	}

	public int getMana() {
		return dataManager.get(MANA);
	}

	public void setMana(int mana) {
		dataManager.set(MANA, mana);
	}

}
