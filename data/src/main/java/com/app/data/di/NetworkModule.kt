package com.app.data.di

import com.app.data.network.RetrofitBuilder
import com.app.data.network.services.AuthorizationNetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityComponent::class)
class NetworkModule {

    @Provides
    fun provideRetrofitBuilder(): Retrofit {
        return RetrofitBuilder().createConnection()
    }

    @ActivityScoped
    @Provides
    fun provideAuthorizationService(retrofit: Retrofit): AuthorizationNetworkService {
        return retrofit.create(AuthorizationNetworkService::class.java)
    }

}