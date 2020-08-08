package com.illiarb.tmdbclient.libs.ui.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import kotlin.reflect.KClass

@Module
interface FragmentModule {

  @Binds
  fun bindFragmentFactory(factory: DaggerFragmentFactory): FragmentFactory
}

@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentKey(val value: KClass<out Fragment>)