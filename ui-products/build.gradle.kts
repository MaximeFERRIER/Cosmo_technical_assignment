plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "fr.droidfactory.cosmo.ui.products"

    defaultConfig {

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    kapt {
        correctErrorTypes = true
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

}

dependencies {

    implementation(project(":sdk-bluetooth"))
    implementation(project(":sdk-core"))
    implementation(project(":sdk-designsystem"))
    implementation(project(":sdk-domain"))

    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.compose.navigation)
    implementation(libs.androidx.icons.core)
    implementation(libs.androidx.icons.extended)

    implementation(libs.hilt.android)
    implementation(libs.hilt.compose.navigation)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.rules)
    implementation(libs.core.ktx.test)
    kapt(libs.hilt.compiler)

    implementation(libs.coil.compose)

    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.kotlin.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}