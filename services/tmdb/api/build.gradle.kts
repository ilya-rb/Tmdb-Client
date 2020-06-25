plugins {
  id("com.android.library")
  kotlin("android")
  kotlin("kapt")
  kotlin("plugin.serialization") version "1.3.70"
}

apply {
  from(rootProject.file("gradle/configure-kotlin-sources.gradle"))
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

  implementation(project(Modules.Core.util))

  implementation(Deps.Kotlin.std)
  implementation(Deps.Kotlin.coroutines)
  implementation(Deps.Kotlin.serialization)
  implementation(Deps.Dagger.core)
}