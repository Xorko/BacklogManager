import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.regex.Pattern

val flavorDimension = "env"
val dev = "dev"
val staging = "staging"
val prod = "prod"
val environments = setOf(dev, staging, prod)
val androidPlatformName = "android"
val iosPlatformName = "ios"
val apiBaseUrlKey = "API_BASE_URL"

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.spotless)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(projects.shared)
            implementation(libs.bundles.koin)
            implementation(libs.bundles.ktor.client)
            implementation(libs.bundles.coil)
            implementation(libs.bundles.arrow)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
        }
    }
}

android {
    namespace = "com.xorko.backlogmanager"
    compileSdk =
        libs.versions.android.compileSdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = "com.xorko.backlogmanager"
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.android.targetSdk
                .get()
                .toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    flavorDimensions(flavorDimension)
    productFlavors {
        create(dev) {
            dimension = flavorDimension
            isDefault = true
            applicationIdSuffix = ".$dev"
            resValue("string", "app_name", "[$dev] BacklogManager")
        }
        create(staging) {
            dimension = flavorDimension
            applicationIdSuffix = ".$staging"
            resValue("string", "app_name", "[$staging] BacklogManager")
        }
        create(prod) {
            dimension = flavorDimension
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)

    // Room
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)

    // Arrow optics
    add("kspAndroid", libs.arrow.optics.kspPlugin)
    add("kspIosSimulatorArm64", libs.arrow.optics.kspPlugin)
    add("kspIosX64", libs.arrow.optics.kspPlugin)
    add("kspIosArm64", libs.arrow.optics.kspPlugin)
}

room {
    schemaDirectory("$projectDir/schemas")
}

spotless {
    kotlin {
        ktlint()
            .customRuleSets(
                listOf(
                    libs.ktlint.ruleset.compose
                        .get()
                        .toString(),
                ),
            )
    }
}

project.extra.set("buildkonfig.flavor", currentBuildVariant())

buildkonfig {
    packageName = "com.xorko.backlogmanager"

    defaultConfigs {
        buildConfigField(STRING, flavorDimension, "none")
    }

    defaultConfigs(dev) {
        buildConfigField(STRING, flavorDimension, dev)
    }
    targetConfigs(dev) {
        create(androidPlatformName) {
            buildConfigField(STRING, apiBaseUrlKey, "http://10.0.2.2")
        }
        create(iosPlatformName) {
            buildConfigField(STRING, apiBaseUrlKey, "http://127.0.0.1")
        }
    }

    defaultConfigs(staging) {
        buildConfigField(STRING, flavorDimension, staging)
    }
    targetConfigs(staging) {
        create(androidPlatformName) {
            buildConfigField(STRING, apiBaseUrlKey, "")
        }
        create(iosPlatformName) {
            buildConfigField(STRING, apiBaseUrlKey, "")
        }
    }

    defaultConfigs(prod) {
        buildConfigField(STRING, flavorDimension, prod)
    }
    targetConfigs(prod) {
        create(androidPlatformName) {
            buildConfigField(STRING, apiBaseUrlKey, "")
        }
        create(iosPlatformName) {
            buildConfigField(STRING, apiBaseUrlKey, "")
        }
    }
}

fun Project.getAndroidBuildVariantOrNull(): String? {
    val taskRequestsStr = gradle.startParameter.taskRequests.toString()
    val pattern: Pattern =
        if (taskRequestsStr.contains("assemble")) {
            Pattern.compile("assemble(\\w+)(Release|Debug)")
        } else {
            Pattern.compile("bundle(\\w+)(Release|Debug)")
        }

    val matcher = pattern.matcher(taskRequestsStr)
    val variant = if (matcher.find()) matcher.group(1).lowercase() else null
    return if (variant in environments) {
        variant
    } else {
        null
    }
}

private fun Project.currentBuildVariant(): String =
    getAndroidBuildVariantOrNull()
        ?: System
            .getenv()["VARIANT"]
            .toString()
            .takeIf { it in environments } ?: dev
