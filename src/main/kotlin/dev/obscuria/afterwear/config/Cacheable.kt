package dev.obscuria.afterwear.config

class Cacheable<T>(
	private val compute: () -> T
) {
	private var value: T? = null

	val isDirty: Boolean
		get() = value == null

	fun get(): T {
		return value ?: compute().also { value = it }
	}

	fun invalidate() {
		value = null
	}
}
