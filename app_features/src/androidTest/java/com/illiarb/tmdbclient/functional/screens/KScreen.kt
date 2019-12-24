package com.illiarb.tmdbclient.functional.screens

import com.agoda.kakao.screen.Screen

abstract class KScreen<out T : KScreen<T>> : Screen<T>()