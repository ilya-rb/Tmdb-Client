plugins {
  id("com.android.library")
  kotlin("android")
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

  implementation(project(Modules.Services.tmdb))
  implementation(project(Modules.Services.analytics))
  implementation(project(Modules.Core.logger))
  implementation(project(Modules.Core.util))
  implementation(project(Modules.Core.tools))

  implementation(Deps.Android.AndroidX.workManager)

  api(Deps.Test.junit)
  api(Deps.Test.Mockito.core)
  api(Deps.Test.Mockito.inline)
  api(Deps.Test.Mockito.kotlin)
  api(Deps.Kotlin.reflect)
  api(Deps.Kotlin.coroutinesTest)
  api(Deps.Test.AndroidX.core)
  api(Deps.Test.AndroidX.rules)
}