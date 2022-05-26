package com.example.whatsappclonejetpackfirebase.di

import com.example.whatsappclonejetpackfirebase.domain.repositories.UserProfileRepository
import com.example.whatsappclonejetpackfirebase.domain.repositories.UserProfileRepositoryImpl
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