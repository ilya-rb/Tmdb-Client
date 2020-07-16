plugins {
  id("com.android.library")
  kotlin("android")
}

apply(from = rootProject.file("gradle/configure-kotlin-sources.gradle"))

dependencies {
  implementation(project(Modules.Services.tmdb))
  implementation(project(Modules.Services.analytics))
  implementation(project(Modules.Core.logger))
  implementation(project(Modules.Core.util))
  implementation(project(Modules.Core.tools))

  debugImplementation(Deps.Tools.Debug.Flipper.flipper)
  debugImplementation(Deps.Tools.Debug.Flipper.flipperNetwork)

  api(Deps.Kotlin.reflect)
  api(Deps.Kotlin.coroutinesTest)
  api(Deps.Test.AndroidX.core)
  api(Deps.Test.AndroidX.rules)
  api(Deps.Test.AndroidX.extJunit)
  api(Deps.Test.JUnit5.jupiterApi)
  api(Deps.Test.JUnit5.jupiterParams)
  api(Deps.Test.truth)
  api(Deps.Test.mockk)
  api(Deps.Test.junit)
}