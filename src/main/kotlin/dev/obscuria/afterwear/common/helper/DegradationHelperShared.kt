package dev.obscuria.afterwear.common.helper

import dev.obscuria.afterwear.Afterwear.id
import dev.obscuria.afterwear.api.AfterwearApi.degradation
import dev.obscuria.afterwear.api.AfterwearApi.integrity
import dev.obscuria.afterwear.api.AfterwearApi.isDegradable
import dev.obscuria.afterwear.config.CommonConfig
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.util.Mth
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.item.ItemStack
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
internal object DegradationHelperShared {

	val DEGRADABLE: TagKey<Attribute> = TagKey.create(Registries.ATTRIBUTE, id("degradable"))

	fun isDegradable(stack: ItemStack): Boolean {
		return CommonConfig.ENABLE_DEGRADATION.get()
			&& !CommonConfig.Cache.DEGRADATION_BLACKLIST.get().test(stack)
			&& CommonConfig.Cache.DEGRADATION_WHITELIST.get().test(stack)
	}

	fun getDegradation(stack: ItemStack): Float {
		val maxDamage = stack.maxDamage.toFloat()
		if (maxDamage <= 0f) return 0f
		val damageValue = stack.damageValue.toFloat()
		return Mth.clampedMap(damageValue, 0f, maxDamage, 0f, 1f)
	}

	fun getIntegrity(stack: ItemStack): Float {
		return CommonConfig
			.Cache.INTEGRITY_EXPRESSION.get()
			.evaluate(stack.degradation.toDouble()).toFloat()
	}

	fun degradeDestroySpeed(stack: ItemStack, original: Float): Float {
		return if (!stack.isDegradable) original
		else original * stack.integrity
	}
}
