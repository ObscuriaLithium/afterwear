package dev.obscuria.afterwear.common.helper

import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import dev.obscuria.afterwear.Afterwear.id
import dev.obscuria.afterwear.api.AfterwearApi.degradation
import dev.obscuria.afterwear.api.AfterwearApi.integrity
import dev.obscuria.afterwear.api.AfterwearApi.isDegradable
import dev.obscuria.afterwear.config.CommonConfig
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.util.Mth
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.ItemStack
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
internal object DegradationHelper {

	val DEGRADABLE: TagKey<Attribute> = TagKey.create(Registries.ATTRIBUTE, id("degradable"))

	fun isDegradable(stack: ItemStack): Boolean {
		return CommonConfig.ENABLE_DEGRADATION.get()
			&& !CommonConfig.Cache.DEGRADATION_BLACKLIST.get().test(stack)
			&& CommonConfig.Cache.DEGRADATION_WHITELIST.get().test(stack)
	}

	fun getDegradation(stack: ItemStack): Float {
		val damageValue = stack.damageValue.toFloat()
		val maxDamage = stack.maxDamage.toFloat()
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

	fun degradeAttributes(
		stack: ItemStack,
		slot: EquipmentSlot,
		multimap: Multimap<Attribute, AttributeModifier>
	): Multimap<Attribute, AttributeModifier> {
		if (!stack.isDegradable) return multimap
		return multimap.scale(stack.integrity.toDouble())
	}

	private fun Multimap<Attribute, AttributeModifier>.scale(
		factor: Double
	): Multimap<Attribute, AttributeModifier> {
		if (this.isEmpty || factor == 1.0) return this
		val scaled = ImmutableMultimap.builder<Attribute, AttributeModifier>()
		this.forEach { attribute, modifier ->
			val holder = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute)
			val degradable = holder.`is`(DEGRADABLE)
			scaled.put(attribute, if (degradable) modifier.scale(factor) else modifier)
		}
		return scaled.build()
	}

	private fun AttributeModifier.scale(factor: Double): AttributeModifier {
		return AttributeModifier(this.id, this.name, this.amount * factor, this.operation)
	}
}
