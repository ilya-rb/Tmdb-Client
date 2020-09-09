# <img src="https://github.com/ilya-rb/Tmdb-Client/blob/master/art/tmdb_logo.svg" height="80">
# TMDB browser [work in-progress 🚧 👷🔧 🚧 ]
### It's a work-in-progress movie Android app that lets you explore movies using [TMDB API](https://www.themoviedb.org/)

[![CircleCI](https://circleci.com/gh/ilya-rb/Tmdb-Client/tree/master.svg?style=svg)](https://circleci.com/gh/ilya-rb/Tmdb-Client/tree/master)
[![codecov](https://codecov.io/gh/ilya-rb/Tmdb-Client/branch/master/graph/badge.svg)](https://codecov.io/gh/ilya-rb/Tmdb-Client)
[![codebeat badge](https://codebeat.co/badges/0771fe58-3231-435b-bc9c-7bdd2d11a599)](https://codebeat.co/projects/github-com-ilya-rb-tmdb-client-master)

## Why:
Simple playground for testing various Android stuff(libraries, frameworks, architectures, etc..).

## Some of the technologies used:
- Entirely written in [Kotlin](https://kotlinlang.org/)
- [Jetpack libraries](https://developer.android.com/jetpack) (View model, Navigation, Lifecycle, etc..)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room](https://developer.android.com/jetpack/androidx/releases/room)
- Standard Square stuff: ([Retrofit](https://square.github.io/retrofit/), [OkHttp](https://square.github.io/okhttp/), [Moshi](https://github.com/square/moshi))
- [Firebase remote config](https://firebase.google.com/docs/remote-config) for feature flags
- [Binary preferences](https://github.com/yandextaxitech/binaryprefs) as a file cache
- [Adapter delegates](https://github.com/sockeqwe/AdapterDelegates) for RecyclerView 
- [Glide](https://github.com/bumptech/glide)
- [LeakCanary](https://github.com/square/leakcanary), [Flipper](https://fbflipper.com/), [Hyperion](https://github.com/hyperion-project/hyperion) debug drawer
- [JUnit5](https://junit.org/junit5/) for unit and intergration tests
- [Kaspresso](https://github.com/KasperskyLab/Kaspresso) for automated tests
- [Jacoco](https://www.eclemma.org/jacoco/) for tests coverage

## Setup
1. To build the project you need to remove `.example` suffix in the `api-config.properties.example` and `keystore.properties.example` files.

2. For building APK you need to specify path to debug and release `.jks` keystores.

3. Also you need to supply TMDB API key. Instructions on how to get key is [here](https://developers.themoviedb.org/3/getting-started/introduction)

4. And the last one is you need to create a Firebase project in order to get `google-services.json` for `google-services` gradle plugin. After that it should be placed in `app/src/debug` for debug and `app/src/release` for the release build respectively.
For debug builds `google-services.json` file package name should contain .debug prefix like this:
```"
android_client_info": {
  "package_name": "package.name.debug"
}
```

## Code style:
This project uses [Detekt](https://github.com/detekt/detekt) for static analysis and formatting style can be found in `code-style/tmdb_code_style.xml` file

## Some screenshots:
![1](https://github.com/ilya-rb/Tmdb-Client/blob/master/art/1.png)
![3](https://github.com/ilya-rb/Tmdb-Client/blob/master/art/3.png)
![4](https://github.com/ilya-rb/Tmdb-Client/blob/master/art/4.png)
![5](https://github.com/ilya-rb/Tmdb-Client/blob/master/art/5.png)
![6](https://github.com/ilya-rb/Tmdb-Client/blob/master/art/6.png)
![7](https://github.com/ilya-rb/Tmdb-Client/blob/master/art/7.png)

**Movie details screen**
|Regular                                                            |Jetpack Compose
|-------------------------------------------------------------------|-------------------------------------------------------------------------|
|![7](https://github.com/ilya-rb/Tmdb-Client/blob/master/art/2.png) |![8](https://github.com/ilya-rb/Tmdb-Client/blob/master/art/2_compose.png)

## Navigation graph
![NavGraph](https://github.com/ilya-rb/Tmdb-Client/blob/master/art/nav_graph.png)
