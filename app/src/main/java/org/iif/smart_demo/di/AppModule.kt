package org.iif.smart_demo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.iif.smart_proxy.data.ProxyManager
import org.iif.smart_proxy.data.outline.OutlineConfigImpl
import org.iif.smart_proxy.data.outline.OutlineProxyImpl
import org.iif.smart_proxy.domain.AppProxy
import org.iif.smart_demo.network.MediaApi
import org.iif.smart_demo.utils.Const
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
        val defaultConfig = OutlineConfigImpl.default(Const.DEFAULT_URL)
        return OutlineProxyImpl(defaultConfig)
    }

    @Provides
    @Singleton
    fun provideProxyManager(proxy: AppProxy): ProxyManager {
        return ProxyManager(proxy)
    }
    //endregion

    //region Retrofit
    @Provides
    @Singleton
    fun provideHttpClient(manager: ProxyManager): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        return OkHttpClient.Builder().addInterceptor(logging).build()
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