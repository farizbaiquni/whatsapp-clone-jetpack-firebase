package com.example.whatsappclonejetpackfirebase.di

import android.content.Context
import com.example.whatsappclonejetpackfirebase.domain.ContactRepository
import com.example.whatsappclonejetpackfirebase.utils.PermissionHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Singleton
    @Provides
    fun provideContactRepository(@ApplicationContext context: Context): ContactRepository {
        return ContactRepository(context)
    }

    @Singleton
    @Provides
    fun providePermissionHelper(): PermissionHelper {
        return PermissionHelper()
    }


}
