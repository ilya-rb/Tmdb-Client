plugins {
  kotlin("jvm")
}

apply(from = rootProject.file("gradle/configure-kotlin-sources.gradle"))

dependencies {
  implementation(Deps.Kotlin.std)
  implementation(Deps.Kotlin.coroutines)
}