import java.io.FileInputStream
import java.util.Properties

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
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

    vectorDrawables.useSupportLibrary = true

    testInstrumentationRunner = "com.illiarb.tmdbexplorer.functional.AppRunner"
  }

  buildTypes {
    all {
      val config = file("$rootDir/api-config.properties")
      val properties = Properties().also { it.load(FileInputStream(config)) }
      buildConfigField("String", "API_URL", properties.getProperty("api.url"))
      buildConfigField("String", "API_KEY", properties.getProperty("api.key"))
    }

    getByName("release") {
      isMinifyEnabled = true
      isDebuggable = false
      isZipAlignEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("debug")
    }
  }

  buildFeatures {
    viewBinding = true
  }

  signingConfigs {
    getByName("debug") {
      val propsFile = rootProject.file("keystore.properties")
      if (propsFile.exists()) {
        val props = Properties().also { it.load(FileInputStream(propsFile)) }

        storeFile = file(props.getProperty("storeFile"))
        storePassword = props.getProperty("storePassword")
        keyAlias = props.getProperty("keyAlias")
        keyPassword = props.getProperty("keyPassword")
      }
    }
  }
}

dependencies {

  kapt(Deps.Dagger.compiler)

  implementation(project(Modules.Core.ui))
  implementation(project(Modules.Core.uiImage))
  implementation(project(Modules.Core.tools))
  implementation(project(Modules.Core.util))
  implementation(project(Modules.Core.logger))
  implementation(project(Modules.Core.buildConfig))
  implementation(project(Modules.Services.tmdb))
  implementation(project(Modules.Services.analytics))

  implementation(Deps.Dagger.core)
  implementation(Deps.Misc.timber)
  implementation(Deps.Android.Firebase.core)
  implementation(Deps.Android.AndroidX.navigation)
  implementation(Deps.Android.AndroidX.workManager)

  // Play core lib for downloading dynamic features
  //implementation deps.playCore

  debugImplementation(Deps.Tools.Debug.LeakCanary.android)
  debugImplementation(Deps.Tools.Debug.LeakCanary.objectWatcher)
  debugImplementation(Deps.Tools.Debug.Flipper.flipper)
  debugImplementation(Deps.Tools.Debug.Flipper.flipperNetwork)
  debugImplementation(Deps.Tools.Debug.Flipper.soLoader)
  debugImplementation(Deps.Tools.Debug.Hyperion.core)
  debugImplementation(Deps.Tools.Debug.Hyperion.geigerCounter)

  androidTestImplementation(project(Modules.Core.test))
  androidTestImplementation(Deps.Test.AndroidX.espressoIntents)
  androidTestImplementation(Deps.Test.AndroidX.runner)
  androidTestImplementation(Deps.Test.AndroidX.uiAutomator)
  androidTestImplementation(Deps.Test.AndroidX.extJunit)
  androidTestImplementation(Deps.Test.kakao)
  androidTestImplementation(Deps.Test.kaspresso)
}

apply {
  plugin("com.google.gms.google-services")
}