package dev.obscuria.afterwear.api

import dev.obscuria.afterwear.common.helper.BreakageHelperShared
import dev.obscuria.afterwear.common.helper.DegradationHelperShared
import net.minecraft.world.item.ItemStack

/**
 * Public entry point for other mods that want to read or reuse Afterwear's wear logic.
 *
 * Everything here is driven by the common config, so results depend on the
 * current whitelists, blacklists and formulas. Destroy speed, attribute scaling
 * and interaction blocking are already applied by Afterwear's mixins on
 * [ItemStack]; call the functions below directly only if you bypass vanilla
 * methods or need the same numbers elsewhere (custom tooltips, HUD, etc.).
 */
object AfterwearApi {

	/**
	 * Whether this stack loses effectiveness as it gets damaged.
	 *
	 * False if degradation is turned off in the config, the stack matches the
	 * degradation blacklist, or it doesn't match the whitelist.
	 */
	val ItemStack.isDegradable: Boolean
		get() = DegradationHelperShared.isDegradable(this)

	/**
	 * How worn out the stack is, from `0.0` (undamaged) to `1.0` (no durability left).
	 *
	 * This is simply `damage / maxDamage`, so it is not affected by the config and
	 * does not check [isDegradable].
	 */
	val ItemStack.degradation: Float
		get() = DegradationHelperShared.getDegradation(this)

	/**
	 * The multiplier applied to a degradable stack's stats.
	 *
	 * Computed by feeding [degradation] into `integrity_formula` from the config
	 * (`1 - D^3` by default), so `1.0` means full strength. The result is not
	 * clamped; a custom formula may return values outside `0..1`. Also doesn't
	 * check [isDegradable].
	 */
	val ItemStack.integrity: Float
		get() = DegradationHelperShared.getIntegrity(this)

	/**
	 * Whether the stack uses the reworked breakage behavior: instead of vanishing
	 * at zero durability it stays in place as a broken item.
	 *
	 * Depends on `enable_breakage_rework` and the breakage whitelist/blacklist.
	 */
	val ItemStack.isBreakable: Boolean
		get() = BreakageHelperShared.isBreakable(this)

	/**
	 * Whether the stack is [breakable][isBreakable] and has run out of durability.
	 */
	val ItemStack.isBroken: Boolean
		get() = BreakageHelperShared.isBroken(this)

	/**
	 * Scales a block-breaking speed by the stack's [integrity].
	 *
	 * @param stack the tool being used
	 * @param original the speed vanilla (or another mod) calculated
	 * @return [original] multiplied by the integrity, or unchanged if the stack isn't [degradable][isDegradable]
	 */
	fun degradeDestroySpeed(stack: ItemStack, original: Float): Float {
		return DegradationHelperShared.degradeDestroySpeed(stack, original)
	}

	/**
	 * Whether a use or interaction with this stack should be skipped.
	 *
	 * True only for [broken][isBroken] stacks and only when `pass_interactions`
	 * is enabled. In that case the click falls through to whatever is behind
	 * the item, such as the block or entity being targeted.
	 */
	fun shouldPassInteraction(stack: ItemStack): Boolean {
		return BreakageHelperShared.shouldPassInteraction(stack)
	}
}
