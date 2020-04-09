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
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {

    kapt(Deps.Dagger.compiler)

    implementation(project(Modules.Core.core))

    implementation(Deps.Dagger.core)
    implementation(Deps.Android.Firebase.core)
    implementation(Deps.Android.Firebase.remoteConfig)

    // Caching
    api(Deps.Misc.binaryPrefs)

    // Network
    api(Deps.Retrofit.core)
    api(Deps.Retrofit.converterGson)
    api(Deps.Retrofit.coroutinesAdapter)
    api(Deps.Retrofit.okHttpLogger)

    testImplementation(project(Modules.Core.test))
}