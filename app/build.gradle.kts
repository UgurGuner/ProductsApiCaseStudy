import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hiltPlugin)
    alias(libs.plugins.navigationSafeArgs)
    id("kotlin-parcelize")
}

android {
    namespace = "com.eugurguner.productsapicasestudy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.eugurguner.productsapicasestudy"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Room db schema location
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

        val secureProps = Properties()
        if (file("../secure.properties").exists()) {
            file("../secure.properties").let { secureProps.load(FileInputStream(it)) }
        }
        buildConfigField(
            "String",
            "API_URL",
            (secureProps.getProperty("API_URL") ?: "\"\"")
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.swiperefreshlayout)

    // Api Request and Fetch
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Room for local storage management
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // Dagger Hilt for dependency injection
    implementation(libs.hiltAndroid)
    implementation(libs.hiltNavFragment)
    ksp(libs.hiltCompiler)

    // Viewmodel management
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Image Loading
    implementation(libs.shimmer)
    implementation(libs.glide)

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.truthTest)
    testImplementation("app.cash.turbine:turbine:1.1.0")
    testImplementation(libs.bundles.unitTesting)
    testImplementation("io.mockk:mockk:1.13.12")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
}

ktlint {
    android.set(true)
    debug.set(true)
    version.set("1.2.1")
    ignoreFailures.set(false)
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.JSON)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN_GROUP_BY_FILE)
    }
}