import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.regex.Pattern

// region Environment
val flavorDimension = "env"
val dev = "dev"
val staging = "staging"
val prod = "prod"
val environments = setOf(dev, staging, prod)
// endregion

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.spotless)
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

    buildFeatures {
        buildConfig = true
        resValues = true
    }
    flavorDimensions += listOf(flavorDimension)

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
    implementation(projects.composeApp)
    debugImplementation(libs.compose.uiTooling)
    debugImplementation(libs.compose.uiToolingPreview)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.android)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.kotlinx.coroutines.android)
}

spotless {
    kotlin {
        ktlint()
    }
}
