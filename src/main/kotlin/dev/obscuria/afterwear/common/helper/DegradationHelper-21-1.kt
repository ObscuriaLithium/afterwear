package dev.obscuria.afterwear.common.helper

//? 1.21.1 {
/*import dev.obscuria.afterwear.api.AfterwearApi.integrity
import dev.obscuria.afterwear.api.AfterwearApi.isDegradable
import net.minecraft.core.Holder
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.ItemStack
import org.jetbrains.annotations.ApiStatus
import java.util.function.BiConsumer

@ApiStatus.Internal
internal object DegradationHelper {

	fun forEachModifierDegraded(
		stack: ItemStack,
		consumer: BiConsumer<Holder<Attribute>, AttributeModifier>
	): BiConsumer<Holder<Attribute>, AttributeModifier> {
		if (!stack.isDegradable) return consumer
		return BiConsumer { attribute, modifier ->
			if (attribute.`is`(DegradationHelperShared.DEGRADABLE)) {
				val degradedModifier = modifier.scale(stack.integrity.toDouble())
				consumer.accept(attribute, degradedModifier)
			} else {
				consumer.accept(attribute, modifier)
			}
		}
	}

	private fun AttributeModifier.scale(factor: Double): AttributeModifier {
		return AttributeModifier(id, amount * factor, operation)
	}
}
*///?}
