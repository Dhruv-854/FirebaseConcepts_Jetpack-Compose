package com.dhruv.realtimedb.di

import com.dhruv.realtimedb.firebaseAuth.repository.AuthRepository
import com.dhruv.realtimedb.firebaseAuth.repository.AuthRepositoryImpl
import com.dhruv.realtimedb.firebaseRealTimeDb.realtimeRepo.RealTimeDBRepository
import com.dhruv.realtimedb.firebaseRealTimeDb.realtimeRepo.RealTimeRepository
import com.dhruv.realtimedb.firestoredb.repository.FirestoreDbRepository
import com.dhruv.realtimedb.firestoredb.repository.FirestoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesRealtimeRepository(
        repo: RealTimeDBRepository,
    ): RealTimeRepository


    @Binds
    abstract fun providesFirestoreRepository(
        repo : FirestoreDbRepository
    ) : FirestoreRepository


    @Binds
    abstract fun providesFirebaseAuthRepository(
        repo : AuthRepositoryImpl
    ) : AuthRepository


}