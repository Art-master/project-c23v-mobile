package com.app.data.network

import com.app.data.BuildConfig
import com.app.data.configs.Config
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitBuilder @Inject constructor() {

    private val LOG_TAG = "NetworkLog"

    private val readTimeout = 50L to TimeUnit.SECONDS
    private val connectTimeout = 5L to TimeUnit.SECONDS

    fun createConnection(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Config.Network.BASE_URL)
            .client(buildHttpClient())
            //.addCallAdapterFactory(getRxAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun buildHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .readTimeout(readTimeout.first, readTimeout.second)
            .connectTimeout(connectTimeout.first, connectTimeout.second)
        //.addInterceptor(Interceptors.OfflineCacheInterceptor())
        //.addNetworkInterceptor(Interceptors.NetworkCacheInterceptor())
        //.cache(Interceptors.provideCache())

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(interceptor)
        }

        return builder.build()
    }
}