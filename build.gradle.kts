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
    classpath(Deps.GradlePlugins.jetifierCheck)
  }
}

plugins {
  id("io.gitlab.arturbosch.detekt") version "1.12.0"
  id("com.github.ben-manes.versions") version "0.29.0"
  id("com.github.plnice.canidropjetifier") version "0.5"
  id("tmdbclient")
}

allprojects {

  repositories {
    google()
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    jcenter()
  }

  configurations.all {
    resolutionStrategy.eachDependency {
      when {
        requested.name.startsWith("kotlin-stdlib") -> {
          useTarget(
            "${requested.group}:${requested.name.replace("jre", "jdk")}:${requested.version}"
          )
        }
        else -> when (requested.group) {
          "org.jetbrains.kotlin" -> useVersion(Deps.Kotlin.kotlinVersion)
        }
      }
    }
  }
}

subprojects {
  apply {
    from(rootProject.file("code-quality-tools/detekt.gradle"))
  }
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
  this.jvmTarget = "1.8"
}

tasks.register(name = "clean", type = Delete::class) {
  delete(rootProject.buildDir)
}

tasks.register(name = "syncConfig", type = Exec::class) {
  commandLine("sh", "$rootDir/ci-config/upload-config.sh")
}