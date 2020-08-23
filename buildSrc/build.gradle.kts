repositories {
  mavenCentral()
  google()
  maven("https://dl.bintray.com/kotlin/kotlin-eap")
  maven("https://dl.bintray.com/kotlin/kotlin-dev")
  jcenter()
}

plugins {
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
  const val kotlinVersion = "1.3.72"
  const val agpVersion = "4.2.0-alpha07"
}

dependencies {
  // Explicitly declare all the kotlin bits to avoid mismatched versions
  implementation(kotlin("gradle-plugin", version = SharedBuildVersions.kotlinVersion))
  implementation(kotlin("stdlib", version = SharedBuildVersions.kotlinVersion))
  implementation(kotlin("stdlib-common", version = SharedBuildVersions.kotlinVersion))
  implementation(kotlin("stdlib-jdk7", version = SharedBuildVersions.kotlinVersion))
  implementation(kotlin("stdlib-jdk8", version = SharedBuildVersions.kotlinVersion))
  implementation(kotlin("reflect", version = SharedBuildVersions.kotlinVersion))

  implementation("com.android.tools.build:gradle:${SharedBuildVersions.agpVersion}")
}