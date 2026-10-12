package dev.obscuria.afterwear.common.helper

import dev.obscuria.afterwear.api.AfterwearApi.integrity
import dev.obscuria.afterwear.api.AfterwearApi.isBreakable
import dev.obscuria.afterwear.api.AfterwearApi.isBroken
import dev.obscuria.afterwear.api.AfterwearApi.isDegradable
import dev.obscuria.afterwear.config.CommonConfig
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import org.jetbrains.annotations.ApiStatus
import kotlin.math.roundToInt

@ApiStatus.Internal
internal object BreakageHelper {

	fun isBreakable(stack: ItemStack): Boolean {
		return CommonConfig.ENABLE_BREAKAGE_REWORK.get()
			&& !CommonConfig.Cache.BREAKAGE_BLACKLIST.get().test(stack)
			&& CommonConfig.Cache.BREAKAGE_WHITELIST.get().test(stack)
	}

	fun isBroken(stack: ItemStack): Boolean {
		return stack.isBreakable && stack.damageValue >= stack.maxDamage
	}

	fun shouldPassInteraction(stack: ItemStack): Boolean {
		return CommonConfig.PASS_INTERACTIONS.get() && stack.isBroken
	}

	fun <T : LivingEntity> hurtAndBreak(stack: ItemStack, entity: T, amount: Int): Boolean {
		if (!stack.isBreakable) return false
		if (stack.isBroken) return true
		if (!stack.hurt(amount, entity.random, entity as? ServerPlayer)) return true
		entity.level().playSound(null, entity.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.NEUTRAL)
		return true
	}

	fun modifyTooltip(stack: ItemStack, original: MutableList<Component>): List<Component> {
		if (!stack.isDegradable) return original
		val integrity = (stack.integrity * 100).roundToInt()
		original.add(
			1, when {
				integrity >= 75 -> tr("pristine", integrity, ChatFormatting.DARK_GREEN)
				integrity >= 25 -> tr("worn", integrity, ChatFormatting.GOLD)
				integrity > 0 -> tr("damaged", integrity, ChatFormatting.RED)
				else -> tr("broken", integrity, ChatFormatting.RED)
			}
		)
		return original
	}

	private fun tr(key: String, integrity: Int, style: ChatFormatting): Component {
		return Component.translatable("integrity.afterwear.$key", integrity).withStyle(style)
	}
}
