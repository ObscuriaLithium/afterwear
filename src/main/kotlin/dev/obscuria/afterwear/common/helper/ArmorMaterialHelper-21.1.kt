package dev.obscuria.afterwear.common.helper

//? 1.21.1 {
/*import dev.obscuria.fragmentum.Fragmentum
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.ArmorMaterial

object ArmorMaterialHelper {

	fun listValues(): List<String> {
		return BuiltInRegistries.ARMOR_MATERIAL.keySet().map { $$"$$${it}" }
	}

	fun valueOf(rule: String): ArmorMaterial? {
		return runCatching {
			val id = Fragmentum.parseId(rule.removePrefix("$"))
			BuiltInRegistries.ARMOR_MATERIAL.get(id)
		}.getOrNull()
	}
}
*///?}
