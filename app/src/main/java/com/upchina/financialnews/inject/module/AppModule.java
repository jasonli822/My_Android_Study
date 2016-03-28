package com.upchina.financialnews.inject.module;

import com.upchina.financialnews.service.TopicService;
import com.upchina.financialnews.support.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Provides @Singleton public TopicService provideTopicService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.Url.TOPIC_RECOMMEND)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(TopicService.class);
    }
}
