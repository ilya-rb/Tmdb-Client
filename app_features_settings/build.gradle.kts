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

  implementation(project(Modules.Core.core))
  implementation(project(Modules.Core.ui))

  implementation(Deps.Android.Compose.tooling)
  implementation(Deps.Android.Compose.layout)
  implementation(Deps.Android.Compose.material)
  implementation(Deps.Android.Compose.foundation)

  testImplementation(project(Modules.Core.test))
}