plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.bellogate_caliphate.uwasocial.benchmark"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 29
        targetSdk = 37
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":core"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.benchmark.macro)
    implementation(libs.androidx.benchmark.micro)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.junit)
    implementation(libs.androidx.core.ktx)
}
