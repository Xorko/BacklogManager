package com.xorko.backlogmanager.core.data.configuration

enum class BuildType {
    DEBUG,
    RELEASE,
    ;

    fun isDebug() = this == DEBUG

    fun isRelease() = this == RELEASE
}

expect val currentBuildType: BuildType
