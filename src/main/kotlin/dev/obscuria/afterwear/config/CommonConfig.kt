package dev.obscuria.afterwear.config

import dev.obscuria.afterwear.Afterwear
import dev.obscuria.afterwear.common.helper.DegradationHelper
import dev.obscuria.afterwear.common.Expression
import dev.obscuria.afterwear.common.ItemRule
import dev.obscuria.fragmentum.api.common.tags.TagPostProcessors
import dev.obscuria.fragmentum.api.config.ConfigBuilder
import dev.obscuria.fragmentum.api.config.Configurable

object CommonConfig {

	val ENABLE_DEGRADATION: Configurable<Boolean>
	val INTEGRITY_FORMULA: Configurable<String>
	val DEGRADATION_ATTRIBUTES: Configurable<List<String>>
	val DEGRADATION_WHITELIST: Configurable<List<String>>
	val DEGRADATION_BLACKLIST: Configurable<List<String>>

	val ENABLE_BREAKAGE_REWORK: Configurable<Boolean>
	val PASS_INTERACTIONS: Configurable<Boolean>
	val BREAKAGE_WHITELIST: Configurable<List<String>>
	val BREAKAGE_BLACKLIST: Configurable<List<String>>

	val ENABLE_REPAIR_REWORK: Configurable<Boolean>
	val REPAIR_COST_FORMULA: Configurable<String>

	internal fun init() {}

	init {
		// @formatter:off
		val builder = ConfigBuilder.create("obscuria/afterwear-common.toml")

		builder.comment(
			"=============[ Afterwear Common Config ]============",
			" Please prefer editing the config through the in-game ",
			" configuration menu (available via the mods list), as",
			" it includes many hints and quality-of-life improvements!",
			"===================================================="
		)

		builder.pushCategory("Degradation")
		ENABLE_DEGRADATION = builder.define("enable_degradation", true)
		INTEGRITY_FORMULA = builder.defineString("integrity_formula", "1 - D^3")
		DEGRADATION_ATTRIBUTES = builder.defineList("degradation_attributes", listOf("minecraft:generic.attack_damage", "minecraft:generic.armor", "minecraft:generic.armor_toughness"))
		DEGRADATION_WHITELIST = builder.defineList("degradation_whitelist", listOf("@armor", "@weapons", "@tools", "minecraft:trident"))
		DEGRADATION_BLACKLIST = builder.defineList("degradation_blacklist", emptyList())
		builder.popCategory()

		builder.pushCategory("Breakage")
		ENABLE_BREAKAGE_REWORK = builder.define("enable_breakage_rework", true)
		PASS_INTERACTIONS = builder.define("pass_interactions", true)
		BREAKAGE_WHITELIST = builder.defineList("breakage_whitelist", listOf("@armor", "@weapons", "@tools", "minecraft:trident", "minecraft:shears", "minecraft:fishing_rod"))
		BREAKAGE_BLACKLIST = builder.defineList("breakage_blacklist", listOf("%wood", "%stone", $$"$leather"))
		builder.popCategory()

		builder.pushCategory("Repair")
		ENABLE_REPAIR_REWORK = builder.define("enable_repair_rework", true)
		REPAIR_COST_FORMULA = builder.defineString("repair_cost_formula", "M + E")
		builder.popCategory()

		builder.buildCommon(Afterwear.MOD_ID)
		// @formatter:on

		TagPostProcessors.register(DegradationHelper.DEGRADABLE, DEGRADATION_ATTRIBUTES)
	}

	object Cache {

		val INTEGRITY_EXPRESSION: Cacheable<Expression> = Cacheable {
			Expression.compile(INTEGRITY_FORMULA, "D")
		}
		val DEGRADATION_WHITELIST: Cacheable<ItemRule> = Cacheable {
			ItemRule.parse(CommonConfig.DEGRADATION_WHITELIST.get())
		}
		val DEGRADATION_BLACKLIST: Cacheable<ItemRule> = Cacheable {
			ItemRule.parse(CommonConfig.DEGRADATION_BLACKLIST.get())
		}

		val BREAKAGE_WHITELIST: Cacheable<ItemRule> = Cacheable {
			ItemRule.parse(CommonConfig.BREAKAGE_WHITELIST.get())
		}
		val BREAKAGE_BLACKLIST: Cacheable<ItemRule> = Cacheable {
			ItemRule.parse(CommonConfig.BREAKAGE_BLACKLIST.get())
		}

		val REPAIR_COST_EXPRESSION: Cacheable<Expression> = Cacheable {
			Expression.compile(REPAIR_COST_FORMULA, "M", "E")
		}

		fun invalidate() {
			INTEGRITY_EXPRESSION.invalidate()
			DEGRADATION_WHITELIST.invalidate()
			DEGRADATION_BLACKLIST.invalidate()
			BREAKAGE_BLACKLIST.invalidate()
			BREAKAGE_WHITELIST.invalidate()
			REPAIR_COST_EXPRESSION.invalidate()
		}
	}
}
