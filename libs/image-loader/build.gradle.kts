plugins {
  id("com.android.library")
  kotlin("android")
}

apply(from = rootProject.file("gradle/configure-kotlin-sources.gradle"))

android {

  buildFeatures {
    compose = true
  }
}

dependencies {
  implementation(Deps.Glide.core)
  implementation(Deps.Kotlin.std)
  implementation(Deps.Android.Compose.foundation)
}