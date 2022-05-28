/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;
import vazkii.botania.client.core.handler.TooltipHandler;
import vazkii.botania.common.advancements.ManaGunTrigger;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntityManaBurst;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class ItemManaGun extends Item implements IManaUsingItem {

	private static final String TAG_LENS = "lens";
	private static final String TAG_CLIP = "clip";
	private static final String TAG_CLIP_POS = "clipPos";
	private static final String TAG_COOLDOWN = "cooldown";

	private static final int CLIP_SLOTS = 6;
	private static final int COOLDOWN = 30;

	public ItemManaGun(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		int effCd = COOLDOWN;
		EffectInstance effect = player.getActivePotionEffect(Effects.HASTE);
		if (effect != null) {
			effCd = Math.max(2, COOLDOWN - (effect.getAmplifier() + 1) * 8);
		}

		if (player.isSneaking() && hasClip(stack)) {
			rotatePos(stack);
			world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.PLAYERS, 0.6F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
			if (!world.isRemote) {
				ItemStack lens = getLens(stack);
				ItemsRemainingRenderHandler.send(player, lens, -2);
				setCooldown(stack, effCd);
			}
			return ActionResult.func_233538_a_(stack, world.isRemote);
		} else if (getCooldown(stack) <= 0) {
			EntityManaBurst burst = getBurst(player, stack, true, hand);
			if (burst != null && ManaItemHandler.instance().requestManaExact(stack, player, burst.getMana(), true)) {
				if (!world.isRemote) {
					world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.manaBlaster, SoundCategory.PLAYERS, 0.6F, 1);
					world.addEntity(burst);
					ManaGunTrigger.INSTANCE.trigger((ServerPlayerEntity) player, stack);
					setCooldown(stack, effCd);
				} else {
					player.setMotion(player.getMotion().subtract(burst.getMotion().mul(0.1, 0.3, 0.1)));
				}
			} else if (!world.isRemote) {
				world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.PLAYERS, 0.6F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
			}
			return ActionResult.func_233538_a_(stack, world.isRemote);
		}

		return ActionResult.resultPass(stack);
	}

	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	// ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN ASADA-SAN
	public static boolean isSugoiKawaiiDesuNe(ItemStack stack) {
		return stack.getDisplayName().getString().equalsIgnoreCase("desu gun");
	}

	@Nonnull
	public BurstProperties getBurstProps(PlayerEntity player, ItemStack stack, boolean request, Hand hand) {
		int maxMana = 120;
		int color = 0x20FF20;
		int ticksBeforeManaLoss = 60;
		float manaLossPerTick = 4F;
		float motionModifier = 5F;
		float gravity = 0F;
		BurstProperties props = new BurstProperties(maxMana, ticksBeforeManaLoss, manaLossPerTick, gravity, motionModifier, color);

		ItemStack lens = getLens(stack);
		if (!lens.isEmpty()) {
			((ILens) lens.getItem()).apply(lens, props);
		}
		return props;
	}

	private EntityManaBurst getBurst(PlayerEntity player, ItemStack stack, boolean request, Hand hand) {
		EntityManaBurst burst = new EntityManaBurst(player);
		BurstProperties props = getBurstProps(player, stack, request, hand);

		burst.setSourceLens(getLens(stack));
		if (!request || ManaItemHandler.instance().requestManaExact(stack, player, props.maxMana, false)) {
			burst.setColor(props.color);
			burst.setMana(props.maxMana);
			burst.setStartingMana(props.maxMana);
			burst.setMinManaLoss(props.ticksBeforeManaLoss);
			burst.setManaLossPerTick(props.manaLossPerTick);
			burst.setGravity(props.gravity);
			burst.setBurstMotion(burst.getMotion().getX() * props.motionModifier,
					burst.getMotion().getY() * props.motionModifier,
					burst.getMotion().getZ() * props.motionModifier);

			return burst;
		}
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flags) {
		boolean clip = hasClip(stack);
		if (clip && !Screen.hasShiftDown()) {
			tooltip.add(TooltipHandler.getShiftInfoTooltip());
			return;
		}

		ItemStack lens = getLens(stack);
		if (!lens.isEmpty()) {
			List<ITextComponent> lensTip = lens.getTooltip(Minecraft.getInstance().player, ITooltipFlag.TooltipFlags.NORMAL);
			if (lensTip.size() > 1) {
				tooltip.addAll(lensTip.subList(1, lensTip.size()));
			}
		}

		if (clip) {
			int pos = getClipPos(stack);
			tooltip.add(new TranslationTextComponent("botaniamisc.hasClip"));
			for (int i = 0; i < CLIP_SLOTS; i++) {
				ItemStack lensAt = getLensAtPos(stack, i);

				ITextComponent name;
				if (lensAt.isEmpty()) {
					name = new TranslationTextComponent("botaniamisc.clipEmpty");
				} else {
					name = lensAt.getDisplayName();
				}

				IFormattableTextComponent tip = new StringTextComponent(" - ").append(name);
				tip.mergeStyle(i == pos ? TextFormatting.GREEN : TextFormatting.GRAY);
				tooltip.add(tip);
			}
		}
	}

	@Nonnull
	@Override
	public ITextComponent getDisplayName(@Nonnull ItemStack stack) {
		ItemStack lens = getLens(stack);
		IFormattableTextComponent cmp = super.getDisplayName(stack).deepCopy();
		if (!lens.isEmpty()) {
			cmp.appendString(" (");
			cmp.append(lens.getDisplayName().deepCopy().mergeStyle(TextFormatting.GREEN));
			cmp.appendString(")");
		}
		return cmp;
	}

	public static boolean hasClip(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_CLIP, false);
	}

	public static void setClip(ItemStack stack, boolean clip) {
		ItemNBTHelper.setBoolean(stack, TAG_CLIP, clip);
	}

	public static int getClipPos(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_CLIP_POS, 0);
	}

	public static void setClipPos(ItemStack stack, int pos) {
		ItemNBTHelper.setInt(stack, TAG_CLIP_POS, pos);
	}

	public static void rotatePos(ItemStack stack) {
		int currPos = getClipPos(stack);
		boolean acceptEmpty = !getLensAtPos(stack, currPos).isEmpty();
		int[] slots = new int[CLIP_SLOTS - 1];

		int index = 0;
		for (int i = currPos + 1; i < CLIP_SLOTS; i++, index++) {
			slots[index] = i;
		}
		for (int i = 0; i < currPos; i++, index++) {
			slots[index] = i;
		}

		for (int i : slots) {
			ItemStack lensAt = getLensAtPos(stack, i);
			if (acceptEmpty || !lensAt.isEmpty()) {
				setClipPos(stack, i);
				return;
			}
		}
	}

	public static ItemStack getLensAtPos(ItemStack stack, int pos) {
		CompoundNBT cmp = ItemNBTHelper.getCompound(stack, TAG_LENS + pos, true);
		if (cmp != null) {
			return ItemStack.read(cmp);
		}
		return ItemStack.EMPTY;
	}

	public static void setLensAtPos(ItemStack stack, ItemStack lens, int pos) {
		CompoundNBT cmp = new CompoundNBT();
		if (lens != null) {
			cmp = lens.write(cmp);
		}
		ItemNBTHelper.setCompound(stack, TAG_LENS + pos, cmp);
	}

	public static void setLens(ItemStack stack, ItemStack lens) {
		if (hasClip(stack)) {
			setLensAtPos(stack, lens, getClipPos(stack));
		}

		CompoundNBT cmp = new CompoundNBT();
		if (!lens.isEmpty()) {
			cmp = lens.write(cmp);
		}
		ItemNBTHelper.setCompound(stack, TAG_LENS, cmp);
	}

	public static ItemStack getLens(ItemStack stack) {
		if (hasClip(stack)) {
			return getLensAtPos(stack, getClipPos(stack));
		}

		CompoundNBT cmp = ItemNBTHelper.getCompound(stack, TAG_LENS, true);
		if (cmp != null) {
			return ItemStack.read(cmp);
		}
		return ItemStack.EMPTY;
	}

	public static List<ItemStack> getAllLens(ItemStack stack) {
		List<ItemStack> ret = new ArrayList<>();

		for (int i = 0; i < 6; i++) {
			ret.add(getLensAtPos(stack, i));
		}

		return ret;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (getCooldown(stack) > 0) {
			setCooldown(stack, getCooldown(stack) - 1);
		}
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return getCooldown(stack) > 0;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return getCooldown(stack) / (double) COOLDOWN;
	}

	private int getCooldown(ItemStack stack) {
		return stack.getOrCreateTag().getInt(TAG_COOLDOWN);
	}

	private void setCooldown(ItemStack stack, int cooldown) {
		CompoundNBT tag = stack.getOrCreateTag();
		tag.putInt(TAG_COOLDOWN, cooldown);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}
}
