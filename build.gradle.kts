import groovy.lang.Closure
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
  }
}

plugins {
  id("io.gitlab.arturbosch.detekt") version "1.7.4" apply false
}

allprojects {

  repositories {
    google()
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    jcenter()
  }

  apply {
    plugin("com.github.ben-manes.versions")
    from(rootProject.file("code_quality_tools/detekt.gradle"))
  }

  tasks.withType<JavaCompile>().all {
    options.compilerArgs.addAll(Build.daggerJavaCompilerArgs)
  }

  tasks.withType<KotlinCompile>().all {
    kotlinOptions {
      jvmTarget = "1.8"
      @Suppress("SuspiciousCollectionReassignment")
      freeCompilerArgs += Build.kotlinStandardFreeCompilerArgs
    }
  }

  afterEvaluate {
    extensions.findByName("kapt")?.let {
      configure<org.jetbrains.kotlin.gradle.plugin.KaptExtension> {
        arguments {
          arg("dagger.formatGeneratedSource", "disabled")
        }
      }
    }
  }
}

subprojects {

  afterEvaluate {
    // Base extension for com.android.library and com.android.application
    extensions.findByType<com.android.build.gradle.BaseExtension>()?.apply {
      project.apply {
        from(file("$rootDir/code_quality_tools/jacoco.gradle"))
      }

      testOptions.unitTests.all(closureOf<Test> {
        // This allows to see tests execution progress
        // in the output on the CI.
        testLogging {
          exceptionFormat = TestExceptionFormat.FULL
          events = setOf(
            TestLogEvent.PASSED,
            TestLogEvent.SKIPPED,
            TestLogEvent.FAILED,
            TestLogEvent.STANDARD_ERROR,
            TestLogEvent.STANDARD_OUT
          )
        }
      } /* Is there a way to avoid the cast? */ as Closure<Test>)

      compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
      }

      lintOptions {
        isWarningsAsErrors = true
        isAbortOnError = false

        // App does not have deep linking
        disable("GoogleAppIndexingWarning")
        // Okio references java.nio that does not presented in Android SDK
        disable("InvalidPackage")
        // Warning for kotlin flows
        disable("UnsafeExperimentalUsageError", "UnsafeExperimentalUsageWarning")

        // View binding issues for unused resources and ids
        // disable('UnusedResources')
        // disable('UnusedIds')

        isCheckAllWarnings = true
        isShowAll = true
        isExplainIssues = true

        xmlReport = false

        htmlOutput = file("reports/${project.name}_lint_report.html")
      }
    }
  }
}

tasks.register(name = "clean", type = Delete::class) {
  delete(rootProject.buildDir)
}

tasks.register(name = "syncConfig", type = Exec::class) {
  commandLine("sh", "$rootDir/ci_config/upload_config.sh")
}