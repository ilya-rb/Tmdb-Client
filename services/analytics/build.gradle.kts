plugins {
  id("com.android.library")
  kotlin("android")
  kotlin("kapt")
}

apply(from = rootProject.file("gradle/configure-kotlin-sources.gradle"))

dependencies {
  kapt(Deps.Dagger.compiler)

  implementation(project(Modules.Core.logger))
  implementation(project(Modules.Core.buildConfig))
  implementation(Deps.Kotlin.std)
  implementation(Deps.Dagger.core)
  implementation(Deps.Android.Firebase.core)
}