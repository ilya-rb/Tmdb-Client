plugins {
  id("com.android.library")
  kotlin("android")
}

apply(from = rootProject.file("gradle/configure-kotlin-sources.gradle"))

dependencies {
  api(Deps.Android.AndroidX.browserHelper)
  
  implementation(project(Modules.Core.logger))

  implementation(Deps.Android.AndroidX.activity)
  implementation(Deps.Android.AndroidX.appcompat)
  implementation(Deps.Kotlin.std)
}