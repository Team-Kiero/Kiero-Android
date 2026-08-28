package com.kiero.data.config.model

@JvmInline
value class AppVersion(
    val value: String,
) : Comparable<AppVersion> {
    private val components: List<String>
        get() = value.substringBefore('-')
            .split('.')
            .map { component ->
                component.takeIf { it.all(Char::isDigit) }
                    ?.trimStart('0')
                    ?.ifEmpty { "0" }
                    ?: "0"
            }

    override fun compareTo(other: AppVersion): Int {
        val otherComponents = other.components
        val componentCount = maxOf(components.size, otherComponents.size)

        for (index in 0 until componentCount) {
            val comparison = compareNumericComponents(
                components.getOrElse(index) { "0" },
                otherComponents.getOrElse(index) { "0" },
            )
            if (comparison != 0) return comparison
        }

        return 0
    }

    fun isValid(): Boolean = value.substringBefore('-')
        .split('.')
        .all { it.isNotEmpty() && it.all(Char::isDigit) }

    private fun compareNumericComponents(first: String, second: String): Int = when {
        first.length != second.length -> first.length.compareTo(second.length)
        else -> first.compareTo(second)
    }
}
