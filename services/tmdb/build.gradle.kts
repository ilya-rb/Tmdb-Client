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

  implementation(project(Modules.Core.tools))
  implementation(project(Modules.Core.util))
  implementation(Deps.Dagger.core)
  implementation(Deps.Android.Firebase.core)
  implementation(Deps.Android.AndroidX.workManager)
  implementation(Deps.Android.Firebase.remoteConfig)
  implementation(Deps.Misc.binaryPrefs)
  implementation(Deps.Retrofit.core)
  implementation(Deps.Retrofit.converterGson)
  implementation(Deps.Retrofit.coroutinesAdapter)
  implementation(Deps.Retrofit.okHttpLogger)

  testImplementation(project(Modules.Core.test))
  testImplementation(Deps.Test.AndroidX.extJunit)
  testImplementation(Deps.Retrofit.core)

  androidTestImplementation(project(Modules.Core.test))
  androidTestImplementation(Deps.Test.AndroidX.extJunit)
  androidTestImplementation(Deps.Test.AndroidX.runner)
}