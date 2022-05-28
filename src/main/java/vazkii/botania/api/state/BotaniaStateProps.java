/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.state;

import com.google.common.base.Predicates;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.ModelProperty;

import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.api.state.enums.CratePattern;

/**
 * Holds all Botania block state properties. Use these to set botania blockstates
 */
public final class BotaniaStateProps {

	// The property for floating flower island type
	public static final ModelProperty<IFloatingFlower> FLOATING_DATA = new ModelProperty<>();

	// BlockAlfPortal
	public static final EnumProperty<AlfPortalState> ALFPORTAL_STATE = EnumProperty.create("state", AlfPortalState.class);

	// BlockEnchanter
	public static final EnumProperty<Direction.Axis> ENCHANTER_DIRECTION = EnumProperty.create("facing", Direction.Axis.class, Predicates.not(Predicates.equalTo(Direction.Axis.Y)));

	// BlockCraftyCrate
	public static final EnumProperty<CratePattern> CRATE_PATTERN = EnumProperty.create("pattern", CratePattern.class);

	// BlockPrism
	public static final BooleanProperty HAS_LENS = BooleanProperty.create("has_lens");

	private BotaniaStateProps() {}

}
