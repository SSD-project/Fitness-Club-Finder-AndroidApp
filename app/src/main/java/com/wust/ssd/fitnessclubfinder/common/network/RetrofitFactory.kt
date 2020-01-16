/*
 * GeoSave - app for easy sharing and collaborating on GPS related data
 * Copyright (C) 2018  Jakub Dorda
 *
 * Software under GPLv3 licence - full copyright notice available at:
 * https://github.com/jakdor/geosave/blob/master/README.md
 */
package com.wust.ssd.fitnessclubfinder.common.network

import com.wust.ssd.fitnessclubfinder.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Factory for Retrofit instances
 */
//Made by jakdor
class RetrofitFactory {

    private lateinit var retrofit: Retrofit

    private val retrofitBuilder = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())

    private val okHttpClientBuilder = OkHttpClient.Builder()
        .readTimeout(45, TimeUnit.SECONDS)
        .writeTimeout(45, TimeUnit.SECONDS)
        .connectTimeout(45, TimeUnit.SECONDS)

    private val httpLoggingInterceptor = HttpLoggingInterceptor()

    /**
     * No authorization header
     * @param apiUrl base API url
     * @param serviceClass retrofit config interface
     * @param <S> serviceClass type
     * @return retrofit instance
    </S> */
    fun <S> createService(apiUrl: String, serviceClass: Class<S>): S {
        addLogger()
        retrofitBuilder.baseUrl(apiUrl).client(okHttpClientBuilder.build())
        retrofit = retrofitBuilder.build()
        return retrofit.create(serviceClass)
    }

    /**
     * Add logger interceptor to OkHttp config in debug build
     */
    private fun addLogger(){
        if(BuildConfig.DEBUG){
            if(!okHttpClientBuilder.interceptors().contains(httpLoggingInterceptor)){
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
            }
        }
    }

}