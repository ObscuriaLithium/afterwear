package dev.obscuria.afterwear.common.helper

//? 1.20.1 {
import com.google.common.collect.ImmutableMultimap
import com.google.common.collect.Multimap
import dev.obscuria.afterwear.api.AfterwearApi.integrity
import dev.obscuria.afterwear.api.AfterwearApi.isDegradable
import dev.obscuria.afterwear.common.helper.DegradationHelperShared.DEGRADABLE
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.ItemStack
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
internal object DegradationHelper {

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
//?}
