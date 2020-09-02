import java.io.FileInputStream
import java.util.Properties

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
}

apply(from = rootProject.file("gradle/configure-kotlin-sources.gradle"))

android {
  defaultConfig {
    applicationId = "com.illiarb.tmdbclient"
    vectorDrawables.useSupportLibrary = true
    testInstrumentationRunner = "com.illiarb.tmdbclient.functional.AppRunner"
  }

  buildFeatures {
    buildConfig = true
    compose = true
  }

  signingConfigs {
    val propsFile = rootProject.file("keystore.properties")
    if (propsFile.exists()) {
      create("release") {
        val props = Properties().also { it.load(FileInputStream(propsFile)) }

        storeFile = file(props.getProperty("releaseStoreFile"))
        storePassword = props.getProperty("releaseStorePassword")
        keyAlias = props.getProperty("releaseKeyAlias")
        keyPassword = props.getProperty("releaseKeyPassword")
      }
    } else {
      create("release").initWith(getByName("debug"))
    }
  }

  buildTypes {
    all {
      val config = file("$rootDir/api-config.properties")
      val properties = Properties().also { it.load(FileInputStream(config)) }
      buildConfigField("String", "API_URL", properties.getProperty("api.url"))
      buildConfigField("String", "API_KEY", properties.getProperty("api.key"))
    }

    getByName("debug") {
      applicationIdSuffix = ".debug"
    }

    getByName("release") {
      isMinifyEnabled = true
      isDebuggable = true
      isZipAlignEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
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
  implementation(project(Modules.Core.customTabs))
  implementation(project(Modules.Services.tmdb))
  implementation(project(Modules.Services.analytics))

  implementation(Deps.Kotlin.std)
  implementation(Deps.Kotlin.coroutines)

  implementation(Deps.Dagger.core)

  implementation(Deps.Misc.timber)
  implementation(Deps.Misc.viewBindingPropertyDelegate)
  implementation(Deps.Misc.binaryPrefs)
  implementation(Deps.Misc.youtubePlayer)

  implementation(Deps.Android.Firebase.core)
  implementation(Deps.Android.AndroidX.navigation)
  implementation(Deps.Android.AndroidX.Room.core)

  implementation(Deps.Retrofit.core)
  implementation(Deps.Moshi.core)

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
  androidTestImplementation(Deps.Test.AndroidX.benchmark)
  androidTestImplementation(Deps.Test.kakao)
  androidTestImplementation(Deps.Test.kaspresso)
}

apply {
  plugin("com.google.gms.google-services")
}