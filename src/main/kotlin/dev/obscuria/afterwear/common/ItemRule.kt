package dev.obscuria.afterwear.common

import dev.obscuria.afterwear.common.helper.ArmorMaterialHelper
import dev.obscuria.fragmentum.Fragmentum
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.*
import java.util.Locale
import java.util.function.Predicate

interface ItemRule : Predicate<ItemStack> {

	companion object {

		fun parse(input: List<String>): ItemRule {
			return CompositeRule(input.map { parse(it) })
		}

		fun parse(input: String): ItemRule = runCatching {
			when (input.firstOrNull()) {
				'@' -> CategoryRule(ItemCategory.valueOf(input.substring(1).uppercase(Locale.ROOT)))
				'%' -> TierRule(Tiers.valueOf(input.substring(1).uppercase(Locale.ROOT)))
				'$' -> MaterialRule(ArmorMaterialHelper.valueOf(input))
				'#' -> TagRule(TagKey.create(Registries.ITEM, Fragmentum.parseId(input.substring(1))))
				else -> DirectRule(BuiltInRegistries.ITEM.get(Fragmentum.parseId(input)))
			}
		}.getOrNull() ?: FallbackRule
	}

	private data class DirectRule(val item: Item) : ItemRule {
		override fun test(stack: ItemStack): Boolean {
			return stack.`is`(item)
		}
	}

	private data class TagRule(val tagKey: TagKey<Item>) : ItemRule {
		override fun test(stack: ItemStack): Boolean {
			return stack.`is`(tagKey)
		}
	}

	private data class MaterialRule(val material: ArmorMaterial?) : ItemRule {
		override fun test(stack: ItemStack): Boolean {
			return stack.item.let { it is ArmorItem && it.material == material }
		}
	}

	private data class TierRule(val tier: Tier) : ItemRule {
		override fun test(stack: ItemStack): Boolean {
			return stack.item.let { it is TieredItem && it.tier == tier }
		}
	}

	private data class CategoryRule(val category: ItemCategory) : ItemRule {
		override fun test(stack: ItemStack): Boolean {
			return category.test(stack)
		}
	}

	private data class CompositeRule(val rules: List<ItemRule>) : ItemRule {
		override fun test(stack: ItemStack): Boolean {
			return rules.any { it.test(stack) }
		}
	}

	private object FallbackRule : ItemRule {
		override fun test(stack: ItemStack): Boolean {
			return false
		}
	}
}
