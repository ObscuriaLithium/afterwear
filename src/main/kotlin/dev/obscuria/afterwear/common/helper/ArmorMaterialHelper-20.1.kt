package dev.obscuria.afterwear.common.helper

//? 1.20.1 {
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.ArmorMaterials
import java.util.Locale

object ArmorMaterialHelper {

	fun listValues(): List<String> {
		return ArmorMaterials.entries.map {
			$$"$$${it.name.lowercase(Locale.ROOT)}"
		}
	}

	fun valueOf(input: String): ArmorMaterial? {
		return runCatching {
			val sanitizedInput = input.removePrefix("$").removePrefix("minecraft:")
			ArmorMaterials.valueOf(sanitizedInput.uppercase(Locale.ROOT))
		}.getOrNull()
	}
}
//?}
