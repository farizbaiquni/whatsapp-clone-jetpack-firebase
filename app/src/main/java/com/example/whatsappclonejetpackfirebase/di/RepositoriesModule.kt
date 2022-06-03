package com.example.whatsappclonejetpackfirebase.di

import com.example.whatsappclonejetpackfirebase.domain.repository.UserProfileRepository
import com.example.whatsappclonejetpackfirebase.network.repository.UserProfileRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {

    @Singleton
    @Provides
    fun provideUserProfileRepository(
        db: FirebaseFirestore
    ): UserProfileRepository {
        return UserProfileRepositoryImpl(db)
    }
}