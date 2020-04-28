@file:Suppress("unused")

object Build {
  val kotlinStandardFreeCompilerArgs = listOf(
    "-Xinline-classes",
    "-Xuse-experimental=kotlinx.coroutines.FlowPreview",
    "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
    "-Xuse-experimental=kotlin.RequiresOptIn"
  )

  val daggerJavaCompilerArgs = listOf(
    "-Adagger.formatGeneratedSource=disabled",
    "-Adagger.gradle.incremental=enabled"
  )
}

object Modules {

  const val app = ":app"

  object Core {
    const val ui = ":libs:ui"
    const val uiImage = ":libs:image-loader"
    const val test = ":libs:test"
    const val tools = ":libs:tools"
    const val util = ":libs:util"
    const val logger = ":libs:logger"
    const val buildConfig = ":libs:buildconfig"
  }

  object Services {
    const val tmdb = ":services:tmdb"
    const val analytics = ":services:analytics"
  }
}

object Deps {

  object GradlePlugins {
    const val jacoco = "org.jacoco:org.jacoco.core:0.8.5"
    const val versionsCheck = "com.github.ben-manes:gradle-versions-plugin:0.27.0"
    const val junit5 = "de.mannodermaus.gradle.plugins:android-junit5:1.6.0.0"
  }

  object Kotlin {
    private const val kotlinVersion = "1.3.72"

    const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val std = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
    const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.4"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.2"
  }

  object Android {

    object Build {
      const val compileSdkVersion = 29
      const val targetSdkVersion = 29
      const val minSdkVersion = 21
      const val gradlePlugin = "com.android.tools.build:gradle:4.0.0-beta05"
    }

    object AndroidX {
      private const val archComponentsVersion = "2.2.0"

      object ViewModel {
        const val core = "androidx.lifecycle:lifecycle-viewmodel:$archComponentsVersion"
        const val ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$archComponentsVersion"
        const val ext = "androidx.lifecycle:lifecycle-extensions:$archComponentsVersion"
      }

      object Lifecycle {
        const val ktx = "androidx.lifecycle:lifecycle-runtime-ktx:$archComponentsVersion"
        const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$archComponentsVersion"
      }

      const val navigation = "androidx.navigation:navigation-fragment:2.3.0-alpha04"
      const val workManager = "androidx.work:work-runtime-ktx:2.3.4"
      const val material = "com.google.android.material:material:1.2.0-alpha05"
      const val recyclerView = "androidx.recyclerview:recyclerview:1.2.0-alpha02"
      const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.0.0"
      const val emoji = "androidx.emoji:emoji:1.1.0-alpha01"
      const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta4"
    }

    object Compose {
      private const val composeVersion = "0.1.0-dev07"

      const val tooling = "androidx.ui:ui-tooling:$composeVersion"
      const val layout = "androidx.ui:ui-layout:$composeVersion"
      const val material = "androidx.ui:ui-material:$composeVersion"
      const val foundation = "androidx.ui:ui-foundation:$composeVersion"
    }

    object Firebase {
      const val gradlePlugin = "com.google.gms:google-services:4.3.3"
      const val core = "com.google.firebase:firebase-core:17.2.3"
      const val remoteConfig = "com.google.firebase:firebase-config:19.1.2"
    }
  }

  object Glide {
    private const val glideVersion = "4.11.0"

    const val core = "com.github.bumptech.glide:glide:$glideVersion"
    const val compiler = "com.github.bumptech.glide:compiler:$glideVersion"
  }

  object Retrofit {
    private const val retrofitVersion = "2.7.1"

    const val core = "com.squareup.retrofit2:retrofit:$retrofitVersion"
    const val okHttp = "com.squareup.okhttp3:okhttp:4.5.0"
    const val converterGson = "com.squareup.retrofit2:converter-gson:$retrofitVersion"
    const val coroutinesAdapter =
      "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2"
  }

  object Moshi {
    private const val moshiVersion = "1.9.2"

    const val core = "com.squareup.moshi:moshi:$moshiVersion"
    const val kotlin = "com.squareup.moshi:moshi-kotlin:$moshiVersion"
    const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion"
  }

  object AdapterDelegates {
    private const val adapterDelegatesVersion = "4.3.0"

    const val core = "com.hannesdorfmann:adapterdelegates4:$adapterDelegatesVersion"
    const val dsl =
      "com.hannesdorfmann:adapterdelegates4-kotlin-dsl-viewbinding:$adapterDelegatesVersion"
  }

  object Dagger {
    private const val daggerVersion = "2.26"

    const val core = "com.google.dagger:dagger:$daggerVersion"
    const val compiler = "com.google.dagger:dagger-compiler:$daggerVersion"
  }

  object Tools {

    object Debug {

      object Flipper {
        private const val flipperVersion = "0.34.0"

        const val flipper = "com.facebook.flipper:flipper:$flipperVersion"
        const val flipperNetwork = "com.facebook.flipper:flipper-network-plugin:$flipperVersion"
        const val soLoader = "com.facebook.soloader:soloader:0.8.2"
      }

      object LeakCanary {
        private const val leakCanaryVersion = "2.2"

        const val android = "com.squareup.leakcanary:leakcanary-android:$leakCanaryVersion"
        const val objectWatcher =
          "com.squareup.leakcanary:leakcanary-object-watcher-android:$leakCanaryVersion"
      }

      object Hyperion {
        private const val hyperionVersion = "0.9.27"

        const val core = "com.willowtreeapps.hyperion:hyperion-core:$hyperionVersion"
        const val geigerCounter =
          "com.willowtreeapps.hyperion:hyperion-geiger-counter:$hyperionVersion"
      }
    }
  }

  object Misc {
    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val lottie = "com.airbnb.android:lottie:3.0.7"
    const val binaryPrefs = "com.github.yandextaxitech:binaryprefs:1.0.1"
    const val javax = "javax.inject:javax.inject:1"
  }

  object Test {

    const val junit = "junit:junit:4.13"
    const val truth = "com.google.truth:truth:1.0.1"
    const val kaspresso = "com.kaspersky.android-components:kaspresso:1.1.0"
    const val kakao = "com.agoda.kakao:kakao:2.3.0"
    const val mockk = "io.mockk:mockk:1.10.0"

    object JUnit5 {
      private const val jUnitVersion = "5.6.0"
      private const val androidTestVersion = "1.0.0"

      const val jupiterApi = "org.junit.jupiter:junit-jupiter-api:$jUnitVersion"
      const val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:$jUnitVersion"
      const val jupiterParams = "org.junit.jupiter:junit-jupiter-params:$jUnitVersion"
      const val junitVintageEngine = "org.junit.vintage:junit-vintage-engine:$jUnitVersion"

      const val androidTestCore = "de.mannodermaus.junit5:android-test-core:$androidTestVersion"
      const val androidTestRunner = "de.mannodermaus.junit5:android-test-runner:$androidTestVersion"
    }

    object AndroidX {
      private const val espressoVersion = "3.3.0-alpha04"

      const val core = "androidx.arch.core:core-testing:2.0.1"
      const val rules = "androidx.test:rules:1.2.0"
      const val runner = "androidx.test:runner:1.3.0-alpha04"
      const val espresso = "androidx.test.espresso:espresso-core:$espressoVersion"
      const val espressoIntents = "androidx.test.espresso:espresso-intents:$espressoVersion"
      const val extJunit = "androidx.test.ext:junit:1.1.2-alpha04"
      const val uiAutomator = "androidx.test.uiautomator:uiautomator:2.2.0"
    }
  }
}