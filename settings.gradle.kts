plugins {
  id("com.gradle.enterprise") version "3.1.1"
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}

include(
  ":app",
  ":services:tmdb",
  ":services:analytics",
  ":libs:ui",
  ":libs:image-loader",
  ":libs:util",
  ":libs:test",
  ":libs:logger",
  ":libs:tools",
  ":libs:buildconfig"
)