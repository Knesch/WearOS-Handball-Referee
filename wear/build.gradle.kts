import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use {
        localProperties.load(it)
    }
}

android {
    namespace = "de.knesch.handball.referee"
    compileSdk = 36

    defaultConfig {
        applicationId = "de.knesch.handball.referee"
        minSdk = 30
        targetSdk = 36
        versionCode = 7
        versionName = "1.3.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            ndk {
                debugSymbolLevel = "FULL"
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    lint {
        checkReleaseBuilds = false
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(platform(libs.compose.bom))
    implementation(libs.activity.compose)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling)
    implementation(libs.ui.tooling)
    implementation(libs.core.splashscreen)
    implementation(libs.protolayout)
    implementation(libs.protolayout.material3)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.watchface.complications.data.source.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.compose.navigation)
    implementation(libs.play.services.wearable)
    implementation(libs.androidx.fragment.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)

}