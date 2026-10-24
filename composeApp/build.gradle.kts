import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.internal.utils.localPropertiesFile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties
import java.util.regex.Pattern

// region Environment
val flavorDimension = "env"
val dev = "dev"
val staging = "staging"
val production = "production"
val environments = setOf(dev, staging, production)
// endregion

// region Build config keys
val apiBaseUrlKey = "API_BASE_URL"
val supabaseUrlKey = "SUPABASE_URL"
val supabaseKeyKey = "SUPABASE_KEY"
// endregion

// region Local properties key
val localPropertiesApiBaseUrl = "api.baseUrl"
val localPropertiesSupabaseUrlKey = "supabase.url"
val localPropertiesSupabaseKeyKey = "supabase.key"
// endregion

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.spotless)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    compilerOptions {
        optIn.add("expect-actual-classes")
    }

    android {
        namespace = "com.xorko.backlogmanager.compose"
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        androidResources {
            enable = true
        }

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }

    listOf(
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
            implementation(libs.compose.uiTooling)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.material)
            implementation(libs.androidx.constraintlayout)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.components.resources)
            implementation(libs.material.icons)
            implementation(libs.androidx.navigation3.ui)
            implementation(libs.androidx.lifecycle.viewmodel.navigation3)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(projects.shared)
            implementation(libs.bundles.koin)
            implementation(libs.bundles.ktor.client)
            implementation(libs.bundles.coil)
            implementation(libs.bundles.arrow)
            implementation(libs.bundles.supabase)
            implementation(project.dependencies.platform(libs.supabase.bom))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.kermit)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
    androidRuntimeClasspath(libs.compose.uiToolingPreview)

    // Room
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)

    // Arrow optics
    add("kspAndroid", libs.arrow.optics.kspPlugin)
    add("kspIosSimulatorArm64", libs.arrow.optics.kspPlugin)
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

    val localProperties = Properties()
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use {
            localProperties.load(it)
        }
    }

    defaultConfigs {
        buildConfigField(BOOLEAN, "IS_DEBUG", "false")
        buildConfigField(STRING, flavorDimension, "none")
        buildConfigField(
            STRING,
            apiBaseUrlKey,
            localProperties[localPropertiesApiBaseUrl] as? String
                ?: run {
                    logger.warn("Missing $localPropertiesApiBaseUrl in local.properties")
                    ""
                },
        )
        buildConfigField(
            STRING,
            supabaseUrlKey,
            localProperties[localPropertiesSupabaseUrlKey] as? String
                ?: run {
                    logger.warn(("Missing $localPropertiesSupabaseUrlKey in local.properties"))
                    ""
                },
        )
        buildConfigField(
            STRING,
            supabaseKeyKey,
            localProperties[localPropertiesSupabaseKeyKey] as? String
                ?: run {
                    logger.warn("Missing $localPropertiesSupabaseUrlKey in local.properties")
                    ""
                },
        )
    }

    defaultConfigs(dev) {
        buildConfigField(BOOLEAN, "IS_DEBUG", "true")
        buildConfigField(STRING, flavorDimension, dev)
        buildConfigField(STRING, supabaseKeyKey, "")
    }

    defaultConfigs(staging) {
        buildConfigField(STRING, flavorDimension, staging)
    }

    defaultConfigs(production) {
        buildConfigField(STRING, flavorDimension, production)
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
