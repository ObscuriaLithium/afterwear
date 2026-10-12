package dev.obscuria.afterwear.common.helper

import dev.obscuria.afterwear.config.CommonConfig
import net.minecraft.world.item.ItemStack
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
internal object RepairHelper {

	fun shouldOverrideRepair(): Boolean {
		return CommonConfig.ENABLE_REPAIR_REWORK.get()
	}

	fun getRepairCost(stack: ItemStack, materialCount: Int): Int {
		return CommonConfig.Cache.REPAIR_COST_EXPRESSION.get().evaluate(
			materialCount.toDouble(),
			stack.enchantmentTags.size.toDouble()
		).toInt()
	}
}
