/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.function.Consumer;

import static vazkii.botania.common.lib.ModTags.Items.*;
import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PetalProvider extends RecipeProvider {
	public PetalProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	public String getName() {
		return "Botania petal apothecary recipes";
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		Ingredient white = tagIngr(PETALS_WHITE);
		Ingredient orange = tagIngr(PETALS_ORANGE);
		Ingredient magenta = tagIngr(PETALS_MAGENTA);
		Ingredient lightBlue = tagIngr(PETALS_LIGHT_BLUE);
		Ingredient yellow = tagIngr(PETALS_YELLOW);
		Ingredient lime = tagIngr(PETALS_LIME);
		Ingredient pink = tagIngr(PETALS_PINK);
		Ingredient gray = tagIngr(PETALS_GRAY);
		Ingredient lightGray = tagIngr(PETALS_LIGHT_GRAY);
		Ingredient cyan = tagIngr(PETALS_CYAN);
		Ingredient purple = tagIngr(PETALS_PURPLE);
		Ingredient blue = tagIngr(PETALS_BLUE);
		Ingredient brown = tagIngr(PETALS_BROWN);
		Ingredient green = tagIngr(PETALS_GREEN);
		Ingredient red = tagIngr(PETALS_RED);
		Ingredient black = tagIngr(PETALS_BLACK);
		Ingredient runeWater = tagIngr(RUNES_WATER);
		Ingredient runeFire = tagIngr(RUNES_FIRE);
		Ingredient runeEarth = tagIngr(RUNES_EARTH);
		Ingredient runeAir = tagIngr(RUNES_AIR);
		Ingredient runeSpring = tagIngr(RUNES_SPRING);
		Ingredient runeSummer = tagIngr(RUNES_SUMMER);
		Ingredient runeAutumn = tagIngr(RUNES_AUTUMN);
		Ingredient runeWinter = tagIngr(RUNES_WINTER);
		Ingredient runeMana = tagIngr(RUNES_MANA);
		Ingredient runeLust = tagIngr(RUNES_LUST);
		Ingredient runeGluttony = tagIngr(RUNES_GLUTTONY);
		Ingredient runeGreed = tagIngr(RUNES_GREED);
		Ingredient runeSloth = tagIngr(RUNES_SLOTH);
		Ingredient runeWrath = tagIngr(RUNES_WRATH);
		Ingredient runeEnvy = tagIngr(RUNES_ENVY);
		Ingredient runePride = tagIngr(RUNES_PRIDE);

		Ingredient redstoneRoot = Ingredient.fromItems(ModItems.redstoneRoot);
		Ingredient pixieDust = Ingredient.fromItems(ModItems.pixieDust);
		Ingredient gaiaSpirit = Ingredient.fromItems(ModItems.lifeEssence);

		consumer.accept(make(ModSubtiles.pureDaisy, white, white, white, white));
		consumer.accept(make(ModSubtiles.manastar, lightBlue, green, red, cyan));

		consumer.accept(make(ModSubtiles.endoflame, brown, brown, red, lightGray));
		consumer.accept(make(ModSubtiles.hydroangeas, blue, blue, cyan, cyan));
		consumer.accept(make(ModSubtiles.thermalily, red, orange, orange, runeEarth, runeFire));
		consumer.accept(make(ModSubtiles.rosaArcana, pink, pink, purple, purple, lime, runeMana));
		consumer.accept(make(ModSubtiles.munchdew, lime, lime, red, red, green, runeGluttony));
		consumer.accept(make(ModSubtiles.entropinnyum, red, red, gray, gray, white, white, runeWrath, runeFire));
		consumer.accept(make(ModSubtiles.kekimurus, white, white, orange, orange, brown, brown, runeGluttony, pixieDust));
		consumer.accept(make(ModSubtiles.gourmaryllis, lightGray, lightGray, yellow, yellow, red, runeFire, runeSummer));
		consumer.accept(make(ModSubtiles.narslimmus, lime, lime, green, green, black, runeSummer, runeWater));
		consumer.accept(make(ModSubtiles.spectrolus, red, red, green, green, blue, blue, white, white, runeWinter, runeAir, pixieDust));
		consumer.accept(make(ModSubtiles.rafflowsia, purple, purple, green, green, black, runeEarth, runePride, pixieDust));
		consumer.accept(make(ModSubtiles.shulkMeNot, purple, purple, magenta, magenta, lightGray, gaiaSpirit, runeEnvy, runeWrath));
		consumer.accept(make(ModSubtiles.dandelifeon, purple, purple, lime, green, runeWater, runeFire, runeEarth, runeAir, gaiaSpirit));

		consumer.accept(make(ModSubtiles.jadedAmaranthus, purple, lime, green, runeSpring, redstoneRoot));
		consumer.accept(make(ModSubtiles.bellethorn, red, red, red, cyan, cyan, redstoneRoot));
		consumer.accept(make(ModSubtiles.dreadthorn, black, black, black, cyan, cyan, redstoneRoot));
		consumer.accept(make(ModSubtiles.heiseiDream, magenta, magenta, purple, pink, runeWrath, pixieDust));
		consumer.accept(make(ModSubtiles.tigerseye, yellow, brown, orange, lime, runeAutumn));

		IFinishedRecipe base = make(ModSubtiles.orechid, gray, gray, yellow, green, red, runePride, runeGreed, redstoneRoot, pixieDust);
		IFinishedRecipe gog = make(ModSubtiles.orechid, gray, gray, yellow, yellow, green, green, red, red);
		consumer.accept(new GogAlternationResult(gog, base));

		consumer.accept(make(ModSubtiles.orechidIgnem, red, red, white, white, pink, runePride, runeGreed, redstoneRoot, pixieDust));
		consumer.accept(make(ModSubtiles.fallenKanade, white, white, yellow, yellow, orange, runeSpring));
		consumer.accept(make(ModSubtiles.exoflame, red, red, gray, lightGray, runeFire, runeSummer));
		consumer.accept(make(ModSubtiles.agricarnation, lime, lime, green, yellow, runeSpring, redstoneRoot));
		consumer.accept(make(ModSubtiles.hopperhock, gray, gray, lightGray, lightGray, runeAir, redstoneRoot));
		consumer.accept(make(ModSubtiles.tangleberrie, cyan, cyan, gray, lightGray, runeAir, runeEarth));
		consumer.accept(make(ModSubtiles.jiyuulia, pink, pink, purple, lightGray, runeWater, runeAir));
		consumer.accept(make(ModSubtiles.rannuncarpus, orange, orange, yellow, runeEarth, redstoneRoot));
		consumer.accept(make(ModSubtiles.hyacidus, purple, purple, magenta, magenta, green, runeWater, runeAutumn, redstoneRoot));
		consumer.accept(make(ModSubtiles.pollidisiac, red, red, pink, pink, orange, runeLust, runeFire));
		consumer.accept(make(ModSubtiles.clayconia, lightGray, lightGray, gray, cyan, runeEarth));
		consumer.accept(make(ModSubtiles.loonium, green, green, green, green, gray, runeSloth, runeGluttony, runeEnvy, redstoneRoot, pixieDust));
		consumer.accept(make(ModSubtiles.daffomill, white, white, brown, yellow, runeAir, redstoneRoot));
		consumer.accept(make(ModSubtiles.vinculotus, black, black, purple, purple, green, runeWater, runeSloth, runeLust, redstoneRoot));
		consumer.accept(make(ModSubtiles.spectranthemum, white, white, lightGray, lightGray, cyan, runeEnvy, runeWater, redstoneRoot, pixieDust));
		consumer.accept(make(ModSubtiles.medumone, brown, brown, gray, gray, runeEarth, redstoneRoot));
		consumer.accept(make(ModSubtiles.marimorphosis, gray, yellow, green, red, runeEarth, runeFire, redstoneRoot));
		consumer.accept(make(ModSubtiles.bubbell, cyan, cyan, lightBlue, lightBlue, blue, blue, runeWater, runeSummer, pixieDust));
		consumer.accept(make(ModSubtiles.solegnolia, brown, brown, red, blue, redstoneRoot));
		consumer.accept(make(ModSubtiles.bergamute, orange, green, green, redstoneRoot));
		consumer.accept(make(ModSubtiles.labellia, yellow, yellow, blue, white, black, runeAutumn, redstoneRoot, pixieDust));

		consumer.accept(make(ModBlocks.motifDaybloom, yellow, yellow, orange, lightBlue));
		consumer.accept(make(ModBlocks.motifNightshade, black, black, purple, gray));

		ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
		ItemNBTHelper.setString(stack, "SkullOwner", "Vazkii");
		Ingredient[] inputs = new Ingredient[16];
		Arrays.fill(inputs, pink);
		consumer.accept(new FinishedRecipe(idFor(prefix("vazkii_head")), stack, inputs));
	}

	protected static Ingredient tagIngr(String tag) {
		return Ingredient.fromTag(ItemTags.makeWrapperTag(prefix(tag).toString()));
	}

	protected static Ingredient tagIngr(ITag<Item> tag) {
		return Ingredient.fromTag(tag);
	}

	protected static FinishedRecipe make(IItemProvider item, Ingredient... ingredients) {
		return new FinishedRecipe(idFor(Registry.ITEM.getKey(item.asItem())), new ItemStack(item), ingredients);
	}

	protected static ResourceLocation idFor(ResourceLocation name) {
		return new ResourceLocation(name.getNamespace(), "petal_apothecary/" + name.getPath());
	}

	protected static class FinishedRecipe implements IFinishedRecipe {
		private final ResourceLocation id;
		private final ItemStack output;
		private final Ingredient[] inputs;

		private FinishedRecipe(ResourceLocation id, ItemStack output, Ingredient... inputs) {
			this.id = id;
			this.output = output;
			this.inputs = inputs;
		}

		@Override
		public void serialize(JsonObject json) {
			json.add("output", ItemNBTHelper.serializeStack(output));
			JsonArray ingredients = new JsonArray();
			for (Ingredient ingr : inputs) {
				ingredients.add(ingr.serialize());
			}
			json.add("ingredients", ingredients);
		}

		@Override
		public ResourceLocation getID() {
			return id;
		}

		@Override
		public IRecipeSerializer<?> getSerializer() {
			return ModRecipeTypes.PETAL_SERIALIZER;
		}

		@Nullable
		@Override
		public JsonObject getAdvancementJson() {
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementID() {
			return null;
		}
	}
}
