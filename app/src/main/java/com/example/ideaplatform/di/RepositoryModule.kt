package com.example.ideaplatform.di

import com.example.ideaplatform.data.repository.ItemRepositoryImpl
import com.example.ideaplatform.domain.repository.ItemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindItemRepository(
        itemRepository: ItemRepositoryImpl
    ): ItemRepository
}