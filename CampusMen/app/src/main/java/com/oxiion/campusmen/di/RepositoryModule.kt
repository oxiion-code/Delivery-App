package com.oxiion.campusmen.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oxiion.campusmen.domain.repositories.CampusMenRepository
import com.oxiion.campusmen.domain.usecases.CampusMenRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindAuthRepository(impl: CampusMenRepositoryImpl): CampusMenRepository
}


@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context
}