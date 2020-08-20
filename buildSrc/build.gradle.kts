repositories {
  mavenCentral()
  google()
  maven("https://dl.bintray.com/kotlin/kotlin-eap")
  maven("https://dl.bintray.com/kotlin/kotlin-dev")
  jcenter()
}

plugins {
  kotlin("jvm") version "1.4.0"
  `kotlin-dsl`
  `java-gradle-plugin`
}

kotlinDslPluginOptions {
  experimentalWarning.set(false)
}

gradlePlugin {
  plugins {
    create("TmdbClientPlugin") {
      id = "tmdbclient"
      implementationClass = "dev.illiarb.tmdbclient.gradle.TmdbClientPlugin"
    }
  }
}

object SharedBuildVersions {
  const val kotlinVersion = "1.4.0"
  const val agpVersion = "4.2.0-alpha07"
}

dependencies {
  // Explicitly declare all the kotlin bits to avoid mismatched versions
  implementation(kotlin("gradle-plugin", version = SharedBuildVersions.kotlinVersion))
  implementation(kotlin("stdlib", version = SharedBuildVersions.kotlinVersion))
  implementation("com.android.tools.build:gradle:${SharedBuildVersions.agpVersion}")
}