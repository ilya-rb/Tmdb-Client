import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(Deps.Android.Build.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Deps.Android.Build.minSdkVersion)
        targetSdkVersion(Deps.Android.Build.targetSdkVersion)

        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        all {
            val config = file("$rootDir/api-config.properties")
            val properties = Properties().also { it.load(FileInputStream(config)) }

            buildConfigField("String", "API_URL", properties.getProperty("api.url"))
            buildConfigField("String", "API_KEY", properties.getProperty("api.key"))
        }

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {

    kapt(Deps.Dagger.compiler)

    implementation(project(Modules.Core.core))
    implementation(project(Modules.Core.storage))
    implementation(Deps.Dagger.core)

    testImplementation(project(Modules.Core.test))
    testImplementation(Deps.Test.AndroidX.extJunit)
    testImplementation(Deps.Retrofit.core)

    androidTestImplementation(project(Modules.Core.test))
    androidTestImplementation(Deps.Test.AndroidX.extJunit)
    androidTestImplementation(Deps.Test.AndroidX.runner)
}