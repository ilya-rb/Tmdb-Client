buildscript {

  repositories {
    google()
    maven("https://plugins.gradle.org/m2/")
    jcenter {
      content {
        // just allow to include kotlinx projects
        // detekt needs 'kotlinx-html' for the html report
        includeGroup("org.jetbrains.kotlinx")
      }
    }
  }

  dependencies {
    classpath(Deps.Android.Build.gradlePlugin)
    classpath(Deps.Android.Firebase.gradlePlugin)
    classpath(Deps.Kotlin.gradlePlugin)
    classpath(Deps.GradlePlugins.versionsCheck)
    classpath(Deps.GradlePlugins.jacoco)
    classpath(Deps.GradlePlugins.junit5)
  }
}

plugins {
  id("io.gitlab.arturbosch.detekt") version "1.8.0" apply false
  id("com.github.ben-manes.versions") version "0.29.0"
  id("tmdbclient")
}

allprojects {

  repositories {
    google()
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    jcenter()
  }
}

subprojects {
  apply {
    from(rootProject.file("code-quality-tools/detekt.gradle"))
  }
}

tasks.register(name = "clean", type = Delete::class) {
  delete(rootProject.buildDir)
}

tasks.register(name = "syncConfig", type = Exec::class) {
  commandLine("sh", "$rootDir/ci-config/upload-config.sh")
}