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
    classpath(Deps.GradlePlugins.junit5)
  }
}

plugins {
  id("io.gitlab.arturbosch.detekt") version "1.8.0" apply false
  id("com.github.ben-manes.versions") version "0.28.0"
}

allprojects {

  repositories {
    google()
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    jcenter()
  }

  apply {
    from(rootProject.file("code-quality-tools/detekt.gradle"))
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
    // Disable build config generation for libraries
    extensions.findByType<com.android.build.gradle.LibraryExtension>()?.apply {
      libraryVariants.all {
        generateBuildConfigProvider?.let {
          it {
            enabled = false
          }
        }
      }
    }

    // Base extension for com.android.library and com.android.application
    extensions.findByType<com.android.build.gradle.BaseExtension>()?.apply {
      project.apply {
        from(file("$rootDir/code-quality-tools/jacoco.gradle"))
      }

      testOptions.unitTests.all(closureOf<Test> {
        // For JUnit 5
        useJUnitPlatform()

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
      } as Closure<Test>)

      compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
      }

      lintOptions {
        isWarningsAsErrors = true
        isAbortOnError = false
        isCheckAllWarnings = true
        isShowAll = true
        isExplainIssues = true
        lintConfig = rootProject.file("lint.xml")
        xmlReport = false
        htmlOutput = file("reports/${project.name}_lint_report.html")

        // App does not have deep linking
        disable("GoogleAppIndexingWarning")
        // Okio references java.nio that does not presented in Android SDK
        disable("InvalidPackage")
        // View binding issues for unused resources and ids
        disable("UnusedIds")
      }

      packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/LICENSE-notice.md")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
        exclude("META-INF/licenses/**")
        exclude("META-INF/*.kotlin_module")
        exclude("**/attach_hotspot_windows.dll")
      }
    }
  }
}

tasks.register(name = "clean", type = Delete::class) {
  delete(rootProject.buildDir)
}

tasks.register(name = "syncConfig", type = Exec::class) {
  commandLine("sh", "$rootDir/ci-config/upload-config.sh")
}