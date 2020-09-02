package dev.illiarb.tmdbclient.gradle

import Build
import Deps
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import groovy.lang.Closure
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("unused")
class TmdbClientPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    target.subprojects {
      configureJvm()
    }
  }
}

private fun Project.configureJvm() {
  configureAndroid()
  configureKotlin()
  configureJava()
}

private fun Project.configureKotlin() {
  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
      jvmTarget = "1.8"
      @Suppress("SuspiciousCollectionReassignment")
      freeCompilerArgs += Build.kotlinStandardFreeCompilerArgs
    }
  }

  plugins.withId("org.jetbrains.kotlin.kapt") {
    extensions.getByType<KaptExtension>().apply {
      correctErrorTypes = true
      mapDiagnosticLocations = true

      arguments {
        arg("dagger.formatGeneratedSource", "disabled")
        arg("dagger.experimentalDaggerErrorMessages", "enabled")
        arg("room.schemaLocation", "$projectDir/schemas")
        arg("room.incremental", true)
        arg("moshi.generated", "javax.annotation.Generated")
      }
    }
  }
}

private fun Project.configureJava() {
  tasks.withType<JavaCompile>().all {
    options.compilerArgs.addAll(Build.daggerJavaCompilerArgs)
  }

  plugins.withType<JavaBasePlugin> {
    extensions.getByType<JavaPluginExtension>().apply {
      sourceCompatibility = JavaVersion.VERSION_1_8
      targetCompatibility = JavaVersion.VERSION_1_8
    }
  }
}

private fun Project.configureAndroid() {
  plugins.matching { it is AppPlugin || it is LibraryPlugin }.whenPluginAdded {
    configure<BaseExtension> {
      apply(from = file("$rootDir/code-quality-tools/jacoco.gradle"))

      setCompileSdkVersion(Deps.Android.Build.compileSdkVersion)

      defaultConfig {
        minSdkVersion(Deps.Android.Build.minSdkVersion)
        targetSdkVersion(Deps.Android.Build.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
      }

      buildTypes {
        getByName("release") {
          isMinifyEnabled = false
          proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
      }

      composeOptions {
        kotlinCompilerVersion = Deps.Kotlin.kotlinVersion
        kotlinCompilerExtensionVersion = "1.0.0-alpha01"
      }

      compileOptions {
        isCoreLibraryDesugaringEnabled = true
      }

      configureTestOptions()

      configureAndroidLint(this)

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

      dependencies.add("coreLibraryDesugaring", Build.coreLibraryDesugaring)
    }
  }

  plugins.withType<LibraryPlugin> {
    configure<LibraryExtension> {
      // Disable build config generation for libraries
      libraryVariants.all {
        generateBuildConfigProvider?.orNull?.let {
          it.enabled = false
        }
      }
    }
  }
}

private fun Project.configureAndroidLint(androidExtension: BaseExtension) {
  androidExtension.lintOptions {
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
}

private fun BaseExtension.configureTestOptions() {
  @Suppress("UNCHECKED_CAST")
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
}