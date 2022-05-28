/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IShearable;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IForgeShearable;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.subtile.functional.SubTileBergamute;
import vazkii.botania.common.item.ItemHorn;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockForestDrum extends BlockModWaterloggable implements IManaTrigger {

	public enum Variant {
		WILD,
		GATHERING,
		CANOPY
	}

	private static final VoxelShape SHAPE = Block.makeCuboidShape(3, 1, 3, 13, 15, 13);
	private final Variant variant;

	public BlockForestDrum(Variant v, Properties builder) {
		super(builder);
		this.variant = v;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	public void convertNearby(Entity entity, Item from, Item to) {
		World world = entity.world;
		List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, entity.getBoundingBox());
		for (ItemEntity item : items) {
			ItemStack itemstack = item.getItem();
			if (!itemstack.isEmpty() && itemstack.getItem() == from && !world.isRemote) {
				while (itemstack.getCount() > 0) {
					ItemEntity ent = entity.entityDropItem(new ItemStack(to), 1.0F);
					ent.setMotion(ent.getMotion().add(
							world.rand.nextFloat() * 0.05F,
							(world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F,
							(world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F
					));
					itemstack.shrink(1);
				}
				item.remove();
			}
		}
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, BlockPos pos) {
		if (burst.isFake()) {
			return;
		}
		if (world.isRemote) {
			world.addParticle(ParticleTypes.NOTE, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5D, 1.0 / 24.0, 0, 0);
			return;
		}
		if (variant == Variant.WILD) {
			ItemHorn.breakGrass(world, new ItemStack(ModItems.grassHorn), pos);
		} else if (variant == Variant.CANOPY) {
			ItemHorn.breakGrass(world, new ItemStack(ModItems.leavesHorn), pos);
		} else {
			int range = 10;
			List<MobEntity> entities = world.getEntitiesWithinAABB(MobEntity.class, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range + 1, range + 1, range + 1)), e -> !SubTileBergamute.isBergamuteNearby(world, e.getPosX(), e.getPosY(), e.getPosZ()));
			List<MobEntity> shearables = new ArrayList<>();
			ItemStack stack = new ItemStack(ModBlocks.gatheringDrum);

			for (MobEntity entity : entities) {
				if (entity instanceof CowEntity) {
					convertNearby(entity, Items.BUCKET, Items.MILK_BUCKET);
					if (entity instanceof MooshroomEntity) {
						convertNearby(entity, Items.BOWL, Items.MUSHROOM_STEW);
					}
				} else if (entity instanceof IShearable && ((IShearable) entity).isShearable()
						|| entity instanceof IForgeShearable && ((IForgeShearable) entity).isShearable(stack, world, entity.getPosition())) {
					shearables.add(entity);
				}
			}

			Collections.shuffle(shearables);
			int sheared = 0;

			for (MobEntity entity : shearables) {
				if (sheared > 4) {
					break;
				}

				if (entity instanceof IShearable) {
					((IShearable) entity).shear(SoundCategory.BLOCKS);
				} else {
					List<ItemStack> stacks = ((IForgeShearable) entity).onSheared(null, stack, world, entity.getPosition(), 0);
					for (ItemStack wool : stacks) {
						ItemEntity ent = entity.entityDropItem(wool, 1.0F);
						ent.setMotion(ent.getMotion().add(
								world.rand.nextFloat() * 0.05F,
								(world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F,
								(world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F
						));
					}
				}

				++sheared;
			}
		}

		for (int i = 0; i < 10; i++) {
			world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundCategory.BLOCKS, 1F, 1F);
		}
	}
}
