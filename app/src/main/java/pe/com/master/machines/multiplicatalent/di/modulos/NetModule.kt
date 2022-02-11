package pe.com.master.machines.multiplicatalent.di.modulos

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pe.com.master.machines.multiplicatalent.BuildConfig
import pe.com.master.machines.multiplicatalent.data.remote.Api
import pe.com.master.machines.multiplicatalent.data.repository.DataSourceRemote
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetModule {

    private val TAG = NetModule::class.java.simpleName
    private var RETROFIT: Retrofit? = null

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor { message: String ->
            if (!message.contains("Expires") &&
                !message.contains("Cache") &&
                !message.contains("Pragma") &&
                !message.contains("Content") &&
                !message.contains("Server") &&
                !message.contains("X-")
            ) Log.w(TAG, message)
        }
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideCacheFile(context: Context): File = File(context.cacheDir, "okHttpCache")

    @Provides
    @Singleton
    fun provideCache(file: File): Cache = Cache(file, 10 * 1000 * 1000)

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(provideHttpLoggingInterceptor())
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .cache(cache)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val url = BuildConfig.BASE_URL
        return RETROFIT ?: synchronized(this) {
            val retrofit by lazy {
                Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(provideGson()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .baseUrl(url)
                    .build()
            }
            RETROFIT = retrofit
            retrofit
        }
    }


    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

    @Provides
    @Singleton
    fun provideDataSourceRemote(api: Api): DataSourceRemote = DataSourceRemote(api)
}