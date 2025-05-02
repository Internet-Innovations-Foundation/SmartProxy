package org.iif.smart_demo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.iif.smart_demo.network.MediaApi
import org.iif.smart_demo.utils.Const
import org.iif.smart_proxy.data.ProxyManager
import org.iif.smart_proxy.data.outline.smart.SmartOutlineConfigImpl
import org.iif.smart_proxy.data.outline.smart.SmartOutlineProxyImpl
import org.iif.smart_proxy.domain.AppProxy
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    //region Proxy
    @Provides
    @Singleton
    fun provideOutlineProxy(): AppProxy {
        val defaultConfig = SmartOutlineConfigImpl.default(Const.DEFAULT_URL)
        return SmartOutlineProxyImpl(defaultConfig)
    }

    @Provides
    @Singleton
    fun provideProxyManager(proxy: AppProxy): ProxyManager = runBlocking {
        val proxyManager = ProxyManager(proxy)
        proxyManager.start()
        proxyManager
    }
//endregion

    //region Retrofit
    @Provides
    @Singleton
    fun provideHttpClient(manager: ProxyManager): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(Const.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): MediaApi {
        return retrofit.create(MediaApi::class.java)
    }
//endregion
}