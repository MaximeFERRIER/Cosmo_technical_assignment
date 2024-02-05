import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.hilt.plugin) apply false
}

subprojects {
    val compileSDKVersion = 34
    val targetSDKVersion = 34
    val minSDKVersion = 28
    val version = 1

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_17.name
        targetCompatibility = JavaVersion.VERSION_17.name
    }

    afterEvaluate {
        if(project.hasProperty("android")) {
            (project.property("android") as? BaseAppModuleExtension)?.apply {
                compileSdk = compileSDKVersion

                defaultConfig {
                    minSdk = minSDKVersion
                    targetSdk = targetSDKVersion
                    versionCode = version
                    versionName = "1.0"
                }
            }

            (project.property("android") as? LibraryExtension)?.apply {
                compileSdk = compileSDKVersion

                defaultConfig {
                    minSdk = minSDKVersion
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }

                buildTypes {
                    release {
                        isMinifyEnabled = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                        signingConfig = signingConfigs.getByName("debug")
                    }
                    debug {
                        isMinifyEnabled = false
                    }
                }
            }
        }
    }
}