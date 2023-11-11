package com.kai.padhelper.dagger

import com.kai.padhelper.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class])
interface PadSearchComponent {
    fun inject(activity: MainActivity)
}