package dev.obscuria.afterwear.common

import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.FishingRodItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ShearsItem
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.TieredItem
import net.minecraft.world.item.TridentItem
import java.util.function.Predicate

enum class ItemCategory(
	val predicate: Predicate<ItemStack>
) : Predicate<ItemStack> by predicate {
	ARMOR({
		it.item is ArmorItem
	}),
	WEAPONS({
		it.item is SwordItem
			|| it.item is TridentItem
			//? 1.21.1
			//|| it.item is net.minecraft.world.item.MaceItem
	}),
	TOOLS({
		it.item is TieredItem
			|| it.item is ShearsItem
			|| it.item is FishingRodItem
	})
}
