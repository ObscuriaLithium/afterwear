package dev.obscuria.afterwear.common.helper

//? 1.20.1 {
import dev.obscuria.afterwear.api.AfterwearApi.isBreakable
import dev.obscuria.afterwear.api.AfterwearApi.isBroken
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
internal object BreakageHelper {

	fun <T : LivingEntity> hurtAndBreak(stack: ItemStack, entity: T, amount: Int): Boolean {
		if (!stack.isBreakable) return false
		if (stack.isBroken) return true
		if (!stack.hurt(amount, entity.random, entity as? ServerPlayer)) return true
		entity.level().playSound(null, entity.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.NEUTRAL)
		return true
	}
}
//?}
