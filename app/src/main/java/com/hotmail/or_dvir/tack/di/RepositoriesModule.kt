package com.hotmail.or_dvir.tack.di

import com.hotmail.or_dvir.tack.database.repositories.NapWakeWindowRepository
import com.hotmail.or_dvir.tack.database.repositories.NapWakeWindowRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {
    @Binds
    @Singleton
    abstract fun bindNapWakeRepository(
        impl: NapWakeWindowRepositoryImpl
    ): NapWakeWindowRepository
}
