package com.change.towerfarm.api

import com.change.towerfarm.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitManager : BaseRetrofitManager(), IRetrofitManager {

    private const val TIMEOUT_SECONDS = 60L

    private var apiClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
    }.build()

    private val retrofitService = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(apiClient)
        .build()
        .create(IApiService::class.java)

    /*************** Api Method ******************/

//    override suspend fun getAppConfig(): Result<AppConfigModel> = suspendCoroutine {
//        exclusiveRun(retrofitService.getAppConfig(), ApiCallBack(it))
//    }

    /***************** end region ****************/

}