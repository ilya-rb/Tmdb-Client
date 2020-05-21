package com.illiarb.tmdbclient.libs.buildconfig

interface BuildConfig {
  val isDebug: Boolean
  val sdkInt: Int
  val applicationId: String
  val buildType: String
  val versionCode: Long
  val versionName: String
}