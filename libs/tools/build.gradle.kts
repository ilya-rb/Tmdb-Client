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

  implementation(project(Modules.Core.logger))
  implementation(Deps.Dagger.core)
  implementation(Deps.Kotlin.std)
  implementation(Deps.Kotlin.coroutines)
  implementation(Deps.Android.Firebase.remoteConfig)
  implementation(Deps.Android.AndroidX.workManager)
}