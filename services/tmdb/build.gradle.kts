plugins {
  id("com.android.library")
  id("de.mannodermaus.android-junit5")
  kotlin("android")
  kotlin("kapt")
  kotlin("plugin.serialization") version "1.4.10"
}

apply(from = rootProject.file("gradle/configure-kotlin-sources.gradle"))

dependencies {

  kapt(Deps.Dagger.compiler)
  kapt(Deps.Moshi.codegen)
  kapt(Deps.Android.AndroidX.Room.compiler)

  implementation(project(Modules.Core.tools))
  implementation(project(Modules.Core.util))
  implementation(project(Modules.Core.logger))
  implementation(project(Modules.Core.buildConfig))

  implementation(Deps.Kotlin.serialization)
  implementation(Deps.Kotlin.serializationCbor)
  implementation(Deps.Dagger.core)
  implementation(Deps.Android.Firebase.core)
  implementation(Deps.Android.Firebase.remoteConfig)
  implementation(Deps.Android.AndroidX.Room.core)
  implementation(Deps.Misc.binaryPrefs)
  implementation(Deps.Retrofit.core)
  implementation(Deps.Retrofit.converterMoshi)
  implementation(Deps.Retrofit.coroutinesAdapter)
  implementation(Deps.Moshi.kotlin)
  implementation(Deps.Moshi.adapters)
  implementation(Deps.Misc.javax)

  debugImplementation(Deps.Tools.Debug.Flipper.flipper)
  debugImplementation(Deps.Tools.Debug.Flipper.flipperNetwork)

  testImplementation(project(Modules.Core.test))
  testImplementation(Deps.Retrofit.core)
  testImplementation(Deps.Retrofit.okHttp)

  testRuntimeOnly(Deps.Test.JUnit5.jupiterEngine)
  testRuntimeOnly(Deps.Test.JUnit5.junitVintageEngine)

  androidTestImplementation(project(Modules.Core.test))
  androidTestImplementation(Deps.Test.JUnit5.jupiterApi)
  androidTestImplementation(Deps.Test.AndroidX.extJunit)
  androidTestImplementation(Deps.Test.AndroidX.runner)
  androidTestImplementation(Deps.Test.truth)
}