package dev.obscuria.afterwear.common.helper

//? 1.21.1 {
/*import dev.obscuria.afterwear.api.AfterwearApi.isBreakable
import dev.obscuria.afterwear.api.AfterwearApi.isBroken
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentHelper
import org.jetbrains.annotations.ApiStatus
import java.util.function.Consumer

@ApiStatus.Internal
internal object BreakageHelper {

	fun hurtAndBreak(
		stack: ItemStack,
		entity: LivingEntity?,
		level: ServerLevel,
		amount: Int,
		consumer: Consumer<Item>
	): Boolean {
		if (!stack.isBreakable) return false
		if (stack.isBroken) return true
		if (entity == null || !entity.hasInfiniteMaterials()) {

			if (amount > 0) {
				val amount = EnchantmentHelper.processDurabilityChange(level, stack, amount)
				if (amount <= 0) return true
			}

			val damage: Int = stack.damageValue + amount

			if (entity is ServerPlayer && amount != 0) {
				CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(entity, stack, damage)
			}

			stack.damageValue = damage
			if (damage >= stack.maxDamage) {
				consumer.accept(stack.item)
			}
		}
		return true
	}
}
*///?}
