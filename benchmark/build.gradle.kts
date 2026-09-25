plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.bellogate_caliphate.uwasocial.benchmark"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 29
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":core"))
    implementation(libs.androidx.benchmark.macro)
    implementation(libs.androidx.benchmark.micro)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.junit)
}
