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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    kapt(Deps.Dagger.compiler)

    implementation(Deps.Dagger.core)

    api(Deps.Android.AndroidX.material)
    api(Deps.Android.AndroidX.swipeRefreshLayout)
    api(Deps.Android.AndroidX.emoji)
    api(Deps.Android.AndroidX.constraintLayout)
    api(Deps.AdapterDelegates.core)
    api(Deps.AdapterDelegates.dsl)
    api(Deps.Android.AndroidX.Lifecycle.ktx)
    api(Deps.Android.AndroidX.Lifecycle.liveData)
    api(Deps.Android.AndroidX.ViewModel.core)
    api(Deps.Android.AndroidX.ViewModel.ext)
    api(Deps.Android.AndroidX.ViewModel.ktx)
}