@file:Suppress("unused")

object Build {
  const val coreLibraryDesugaring = "com.android.tools:desugar_jdk_libs:1.0.9"

  val kotlinStandardFreeCompilerArgs = listOf(
    "-progressive",
    "-Xinline-classes",
    "-Xopt-in=kotlin.RequiresOptIn",
    "-Xopt-in=kotlinx.coroutines.FlowPreview",
    "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
    "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi",
    // Generate nullability assertions for non-null Java expressions
    "-Xstrict-java-nullability-assertions",
    "-Xskip-prerelease-check",
    "-Xallow-jvm-ir-dependencies"
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
    const val customTabs = ":libs:custom-tabs"
  }

  object Services {
    const val tmdb = ":services:tmdb"
    const val analytics = ":services:analytics"
  }
}

object Deps {

  object GradlePlugins {
    const val jacoco = "org.jacoco:org.jacoco.core:0.8.6"
    const val versionsCheck = "com.github.ben-manes:gradle-versions-plugin:0.33.0"
    const val junit5 = "de.mannodermaus.gradle.plugins:android-junit5:1.6.2.0"
    const val jetifierCheck = "com.github.plnice:canidropjetifier:0.5"
  }

  object Kotlin {
    const val kotlinVersion = "1.4.10"

    private const val kotlinCoroutinesVersion = "1.4.0"
    private const val kotlinSerializationVersion = "1.0.1"

    const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10"
    const val std = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
    const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutinesVersion"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutinesVersion"
    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinSerializationVersion"
    const val serializationCbor = "org.jetbrains.kotlinx:kotlinx-serialization-cbor:$kotlinSerializationVersion"
  }

  object Android {

    object Build {
      const val compileSdkVersion = 29
      const val targetSdkVersion = 29
      const val minSdkVersion = 21
      const val gradlePlugin = "com.android.tools.build:gradle:4.2.0-alpha15"
    }

    object AndroidX {
      private const val archComponentsVersion = "2.3.0-beta01"

      object ViewModel {
        const val core = "androidx.lifecycle:lifecycle-viewmodel-ktx:$archComponentsVersion"
      }

      object Lifecycle {
        const val ktx = "androidx.lifecycle:lifecycle-runtime-ktx:$archComponentsVersion"
      }

      object Room {
        private const val roomVersion = "2.3.0-alpha02"

        const val core = "androidx.room:room-ktx:$roomVersion"
        const val compiler = "androidx.room:room-compiler:$roomVersion"
      }

      const val activity = "androidx.activity:activity-ktx:1.2.0-beta01"
      const val appcompat = "androidx.appcompat:appcompat:1.3.0-alpha02"
      const val fragment = "androidx.fragment:fragment-ktx:1.3.0-beta01"
      const val navigation = "androidx.navigation:navigation-fragment:2.3.1"
      const val material = "com.google.android.material:material:1.3.0-alpha03"
      const val recyclerView = "androidx.recyclerview:recyclerview:1.2.0-alpha06"
      const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01"
      const val emoji = "androidx.emoji:emoji:1.2.0-alpha01"
      const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
      const val browserHelper = "com.google.androidbrowserhelper:androidbrowserhelper:2.1.0-alpha01"
    }

    object Compose {
      const val composeVersion = "1.0.0-alpha06"

      const val runtime = "androidx.compose.runtime:runtime:$composeVersion"
      const val foundation = "androidx.compose.foundation:foundation:$composeVersion"
      const val layout = "androidx.compose.foundation:foundation-layout:$composeVersion"
      const val ui = "androidx.compose.ui:ui:$composeVersion"
      const val material = "androidx.compose.material:material:$composeVersion"
      const val materialIconsExtended = "androidx.compose.material:material-icons-extended:$composeVersion"
      const val tooling = "androidx.ui:ui-tooling:$composeVersion"
    }

    object Firebase {
      const val gradlePlugin = "com.google.gms:google-services:4.3.4"
      const val core = "com.google.firebase:firebase-core:18.0.0"
      const val remoteConfig = "com.google.firebase:firebase-config:20.0.0"
    }
  }

  object Glide {
    private const val glideVersion = "4.11.0"

    const val core = "com.github.bumptech.glide:glide:$glideVersion"
    const val compiler = "com.github.bumptech.glide:compiler:$glideVersion"
  }

  object Retrofit {
    private const val retrofitVersion = "2.9.0"

    const val core = "com.squareup.retrofit2:retrofit:$retrofitVersion"
    const val okHttp = "com.squareup.okhttp3:okhttp:4.10.0-RC1"
    const val converterMoshi = "com.squareup.retrofit2:converter-moshi:$retrofitVersion"
    const val coroutinesAdapter =
      "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2"
  }

  object Moshi {
    private const val moshiVersion = "1.10.0"

    const val kotlin = "com.squareup.moshi:moshi-kotlin:$moshiVersion"
    const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion"
    const val adapters = "com.squareup.moshi:moshi-adapters:$moshiVersion"
  }

  object AdapterDelegates {
    private const val adapterDelegatesVersion = "4.3.0"

    const val core = "com.hannesdorfmann:adapterdelegates4:$adapterDelegatesVersion"
    const val dsl =
      "com.hannesdorfmann:adapterdelegates4-kotlin-dsl-viewbinding:$adapterDelegatesVersion"
  }

  object Dagger {
    private const val daggerVersion = "2.29.1"

    const val core = "com.google.dagger:dagger:$daggerVersion"
    const val compiler = "com.google.dagger:dagger-compiler:$daggerVersion"
  }

  object Tools {

    object Debug {

      object Flipper {
        private const val flipperVersion = "0.63.0"

        const val flipper = "com.facebook.flipper:flipper:$flipperVersion"
        const val flipperNetwork = "com.facebook.flipper:flipper-network-plugin:$flipperVersion"
        const val soLoader = "com.facebook.soloader:soloader:0.9.0"
      }

      object LeakCanary {
        private const val leakCanaryVersion = "2.5"

        const val android = "com.squareup.leakcanary:leakcanary-android:$leakCanaryVersion"
        const val objectWatcher =
          "com.squareup.leakcanary:leakcanary-object-watcher-android:$leakCanaryVersion"
      }

      object Hyperion {
        private const val hyperionVersion = "0.9.29"

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
    const val viewBindingPropertyDelegate = "com.github.kirich1409:ViewBindingPropertyDelegate:1.2.2"
    const val youtubePlayer = "com.pierfrancescosoffritti.androidyoutubeplayer:core:10.0.5"
  }

  object Test {

    const val junit = "junit:junit:4.13.1"
    const val truth = "com.google.truth:truth:1.1"
    const val kaspresso = "com.kaspersky.android-components:kaspresso:1.2.0"
    const val kakao = "com.agoda.kakao:kakao:2.3.4"
    const val mockk = "io.mockk:mockk:1.10.2"

    object JUnit5 {
      private const val jUnitVersion = "5.7.0"
      private const val androidTestVersion = "1.2.0"

      const val jupiterApi = "org.junit.jupiter:junit-jupiter-api:$jUnitVersion"
      const val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:$jUnitVersion"
      const val jupiterParams = "org.junit.jupiter:junit-jupiter-params:$jUnitVersion"
      const val junitVintageEngine = "org.junit.vintage:junit-vintage-engine:$jUnitVersion"

      // TODO: Use when it upgrades to AndroidX
      const val androidTestCore = "de.mannodermaus.junit5:android-test-core:$androidTestVersion"
      const val androidTestRunner = "de.mannodermaus.junit5:android-test-runner:$androidTestVersion"
    }

    object AndroidX {
      private const val espressoVersion = "3.4.0-alpha02"

      const val core = "androidx.arch.core:core-testing:2.1.0"
      const val rules = "androidx.test:rules:1.3.1-alpha02"
      const val runner = "androidx.test:runner:1.3.1-alpha02"
      const val espresso = "androidx.test.espresso:espresso-core:$espressoVersion"
      const val espressoIntents = "androidx.test.espresso:espresso-intents:$espressoVersion"
      const val extJunit = "androidx.test.ext:junit-ktx:1.1.3-alpha02"
      const val uiAutomator = "androidx.test.uiautomator:uiautomator:2.2.0"
      const val benchmark = "androidx.benchmark:benchmark-junit4:1.1.0-alpha01"
    }
  }
}