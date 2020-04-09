plugins {
    id("com.android.library")
    kotlin("android")
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
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {

    api(Deps.Kotlin.std)
    api(Deps.Kotlin.coroutines)
    api(Deps.Android.AndroidX.workManager)

    implementation(Deps.Misc.javax)
}