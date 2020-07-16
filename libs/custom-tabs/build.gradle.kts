plugins {
  id("com.android.library")
  kotlin("android")
}

apply(from = rootProject.file("gradle/configure-kotlin-sources.gradle"))

dependencies {
  api(Deps.Android.AndroidX.browserHelper)

  implementation(Deps.Kotlin.std)
  implementation(project(Modules.Core.logger))
}