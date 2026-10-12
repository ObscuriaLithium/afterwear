package dev.obscuria.afterwear

import dev.obscuria.afterwear.config.CommonConfig
import dev.obscuria.fragmentum.Fragmentum
import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Afterwear {

	const val MOD_ID: String = /*$ mod_id*/ "afterwear";
	const val MOD_VERSION: String = /*$ mod_version*/ "1.0.0";
	const val MOD_FRIENDLY_NAME: String = /*$ mod_name*/ "Afterwear";
	val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

	fun id(path: String): ResourceLocation {
		return Fragmentum.id(MOD_ID, path)
	}

	fun onInitialize() {
		CommonConfig.init()
	}
}
