package com.khoshnaw.simplemvi.di

import com.khoshnaw.db.movie.MovieLocalDataSourceImpl
import com.khoshnaw.gateway.localDataSource.MovieLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface LocalDataSourceModule {

    @Binds
    fun provide(movieRemoteDataSource: MovieLocalDataSourceImpl): MovieLocalDataSource

}
