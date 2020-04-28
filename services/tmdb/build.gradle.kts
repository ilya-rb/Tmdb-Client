import java.io.FileInputStream
import java.util.Properties

plugins {
  id("com.android.library")
  id("de.mannodermaus.android-junit5")
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

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    testInstrumentationRunnerArgument("runnerBuilder", "de.mannodermaus.junit5.AndroidJUnit5Builder")
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

  packagingOptions {
    exclude("META-INF/LICENSE*")
  }
}

dependencies {

  kapt(Deps.Dagger.compiler)
  kapt(Deps.Moshi.codegen)

  implementation(project(Modules.Core.tools))
  implementation(project(Modules.Core.util))
  implementation(project(Modules.Core.logger))

  implementation(Deps.Dagger.core)
  implementation(Deps.Android.Firebase.core)
  implementation(Deps.Android.AndroidX.workManager)
  implementation(Deps.Android.Firebase.remoteConfig)
  implementation(Deps.Misc.binaryPrefs)
  implementation(Deps.Retrofit.core)
  implementation(Deps.Retrofit.converterGson)
  implementation(Deps.Retrofit.coroutinesAdapter)
  implementation(Deps.Moshi.core)
  implementation(Deps.Moshi.kotlin)

  debugImplementation(Deps.Tools.Debug.Flipper.flipper)
  debugImplementation(Deps.Tools.Debug.Flipper.flipperNetwork)

  testImplementation(project(Modules.Core.test))
  testImplementation(Deps.Retrofit.core)
  testImplementation(Deps.Retrofit.okHttp)

  testRuntimeOnly(Deps.Test.JUnit5.jupiterEngine)
  testRuntimeOnly(Deps.Test.JUnit5.junitVintageEngine)

  androidTestImplementation(project(Modules.Core.test))
  androidTestImplementation(Deps.Test.JUnit5.jupiterApi)
  androidTestImplementation(Deps.Test.JUnit5.androidTestCore)
  androidTestImplementation(Deps.Test.AndroidX.extJunit)
  androidTestImplementation(Deps.Test.AndroidX.runner)
  androidTestImplementation(Deps.Test.truth)

  androidTestRuntimeOnly(Deps.Test.JUnit5.androidTestRunner)
}