package dev.obscuria.afterwear.common

import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.TieredItem
import java.util.function.Predicate

enum class ItemCategory(
	val predicate: Predicate<ItemStack>
) : Predicate<ItemStack> by predicate {
	ARMOR({ it.item is ArmorItem }),
	WEAPONS({ it.item is SwordItem }),
	TOOLS({ it.item is TieredItem })
}
