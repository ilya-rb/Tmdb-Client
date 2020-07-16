plugins {
  id("com.android.library")
  kotlin("android")
}

apply(from = rootProject.file("gradle/configure-kotlin-sources.gradle"))

dependencies {
  implementation(Deps.Glide.core)
  implementation(Deps.Kotlin.std)
}