plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.spotless)
    application
}

group = "com.xorko.backlogmanager"
version = "1.0.0"
application {
    mainClass.set("com.xorko.backlogmanager.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.ktor.client)
    implementation(libs.koin.core)
    implementation(libs.koin.ktor3)
    testImplementation(libs.kotlin.test.junit)
}

spotless {
    kotlin {
        ktlint()
    }
}
