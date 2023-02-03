package com.hotmail.or_dvir.tack.di

import android.app.Application
import com.hotmail.or_dvir.tack.MyApplication
import com.hotmail.or_dvir.tack.database.repositories.NapWakeWindowRepository
import com.hotmail.or_dvir.tack.database.repositories.NapWakeWindowRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {
    @Binds
    @Singleton
    abstract fun bindNapWakeRepository(
        impl: NapWakeWindowRepositoryImpl
    ): NapWakeWindowRepository
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModuleHelper {
    @Provides
    @Singleton
    fun provideCoroutineScopeThatShouldNotBeCancelled(app: Application) =
        (app as MyApplication).scopeThatShouldNotBeCancelled

    @Provides
    // todo do i need this?
//    @Singleton
    fun provideCoroutineDispatcher() = Dispatchers.IO
}
