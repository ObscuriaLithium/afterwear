package dev.obscuria.afterwear.common

import dev.obscuria.afterwear.Afterwear
import dev.obscuria.fragmentum.api.config.Configurable
import redempt.crunch.CompiledExpression
import redempt.crunch.Crunch
import redempt.crunch.functional.ExpressionEnv

data class Expression(
	private val compiled: CompiledExpression,
	private val expression: String
) {

	fun evaluate(vararg params: Double): Double {
		return runCatching {
			compiled.evaluate(*params)
		}.getOrElse {
			Afterwear.LOGGER.error("Failed to evaluate expression: $expression", it)
			return 0.0
		}
	}

	companion object {

		fun compile(input: Configurable<String>, vararg params: String): Expression {
			val environment = ExpressionEnv().setVariableNames(*params)
			return Expression(runCatching {
				Crunch.compileExpression(input.get(), environment)
			}.getOrElse {
				Afterwear.LOGGER.error("Failed to compile expression: ${input.get()}", it)
				Crunch.compileExpression("0", environment)
			}, input.get())
		}
	}
}
