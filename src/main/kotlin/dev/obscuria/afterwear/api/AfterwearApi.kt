package dev.obscuria.afterwear.api

import com.google.common.collect.Multimap
import dev.obscuria.afterwear.common.helper.BreakageHelper
import dev.obscuria.afterwear.common.helper.DegradationHelper
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.ItemStack

object AfterwearApi {

	val ItemStack.isDegradable: Boolean
		get() = DegradationHelper.isDegradable(this)

	val ItemStack.degradation: Float
		get() = DegradationHelper.getDegradation(this)

	val ItemStack.integrity: Float
		get() = DegradationHelper.getIntegrity(this)

	val ItemStack.isBreakable: Boolean
		get() = BreakageHelper.isBreakable(this)

	val ItemStack.isBroken: Boolean
		get() = BreakageHelper.isBroken(this)

	fun degradeDestroySpeed(stack: ItemStack, original: Float): Float {
		return DegradationHelper.degradeDestroySpeed(stack, original)
	}

	fun degradeAttributes(
		stack: ItemStack,
		slot: EquipmentSlot,
		multimap: Multimap<Attribute, AttributeModifier>
	): Multimap<Attribute, AttributeModifier> {
		return DegradationHelper.degradeAttributes(stack, slot, multimap)
	}

	fun shouldPassInteraction(stack: ItemStack): Boolean {
		return BreakageHelper.shouldPassInteraction(stack)
	}
}
