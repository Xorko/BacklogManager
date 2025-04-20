package com.xorko.backlogmanager.utils

/**
 * Returns the enum constant defined in this class for the given [name], or `null` if no such constant was found.
 */
inline fun <reified T : Enum<T>> enumValueOfOrNull(name: String): T? =
    try {
        enumValueOf<T>(name)
    } catch (_: IllegalArgumentException) {
        null
    }
