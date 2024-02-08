plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("kotlinx-serialization")
}

android {
    namespace = "fr.droidfactory.cosmo.sdk.data"

    buildFeatures.buildConfig = true

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "HOST", project.property("host.cosmo") as String)
    }

    kapt {
        correctErrorTypes = true
    }
    ksp {
        arg("room.schemaLocation", "$projectDir/roomSchemas")
    }

}

dependencies {

    implementation(project(":sdk-core"))
    implementation(libs.androidx.core.ktx)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.retrofit)
    implementation(libs.okhttp.interceptor)
    implementation(libs.converter)

    implementation(libs.room.runtime)
    implementation(libs.room.coroutine)
    ksp(libs.room.compiler)

    implementation(libs.kotlin.serialization.json)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.junit)
    testImplementation(libs.okhttp.mockwebserver)
    testImplementation(libs.mockito)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.core.ktx.test)
    testImplementation(libs.room.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}