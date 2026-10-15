package dev.obscuria.afterwear.config

import dev.obscuria.afterwear.Afterwear
import dev.obscuria.afterwear.common.ItemCategory
import dev.obscuria.afterwear.common.helper.ArmorMaterialHelper
import dev.obscuria.fragmentum.api.config.screen.ConfigScreenFactory
import net.minecraft.client.gui.screens.Screen
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Tiers
import java.util.*

object ConfigScreen {

	fun create(parent: Screen): Screen {
		// @formatter:off
		ConfigScreenFactory.create(Afterwear.MOD_ID).apply {
			return createScreen()
				.title(Component.literal("Afterwear Options"))
				.category(category("degradation")
					.option(checkBoxOption(CommonConfig.ENABLE_DEGRADATION))
					.option(stringFieldOption(CommonConfig.INTEGRITY_FORMULA).anyReloadHint())
					.option(dropdownStringListOption(CommonConfig.DEGRADATION_ATTRIBUTES, ::listAttributes).anyReloadHint())
					.option(dropdownStringListOption(CommonConfig.DEGRADATION_WHITELIST, ::listItemRules).anyReloadHint())
					.option(dropdownStringListOption(CommonConfig.DEGRADATION_BLACKLIST,::listItemRules).anyReloadHint()))
				.category(category("breakage")
					.option(checkBoxOption(CommonConfig.ENABLE_BREAKAGE_REWORK))
					.option(checkBoxOption(CommonConfig.PASS_INTERACTIONS))
					.option(dropdownStringListOption(CommonConfig.BREAKAGE_WHITELIST,::listItemRules).anyReloadHint())
					.option(dropdownStringListOption(CommonConfig.BREAKAGE_BLACKLIST,::listItemRules).anyReloadHint()))
				.category(category("repair")
					.option(checkBoxOption(CommonConfig.ENABLE_REPAIR_REWORK))
					.option(stringFieldOption(CommonConfig.REPAIR_COST_FORMULA).anyReloadHint()))
				.build(parent)
		}
		// @formatter:on
	}

	private fun listAttributes(): List<String> {
		return BuiltInRegistries.ATTRIBUTE.keySet().map(ResourceLocation::toString)
	}

	private fun listItemRules(): List<String> {
		return (ItemCategory.entries.map { "@${it.name.lowercase(Locale.ROOT)}" }
			+ ArmorMaterialHelper.listValues()
			+ Tiers.entries.map { "%${it.name.lowercase(Locale.ROOT)}" }
			+ BuiltInRegistries.ITEM.tagNames.map { "#${it.location}" }.toList()
			+ BuiltInRegistries.ITEM.keySet().map(ResourceLocation::toString))
	}
}
