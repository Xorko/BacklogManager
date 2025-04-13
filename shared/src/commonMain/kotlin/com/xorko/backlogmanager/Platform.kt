package com.xorko.backlogmanager

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform