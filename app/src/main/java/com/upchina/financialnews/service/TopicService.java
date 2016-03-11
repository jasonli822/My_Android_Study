package com.upchina.financialnews.service;


import com.upchina.financialnews.bean.Topic;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface TopicService {
    /**
     * Retrofit 传统的 Callback 形式的 API
    @GET("{maxScore}/{pageSize}")
    public Call<Topic> getTopicRecommend(@Path("maxScore") String maxScore, @Path("pageSize") int pageSize);
    */

    /**
     * Retrofit 支持 RxJava 整合
     * @param maxScore
     * @param pageSize
     * @return
     */
    @GET("{maxScore}/{pageSize}")
    public Observable<Topic> getTopicRecommend(@Path("maxScore") String maxScore, @Path("pageSize") int pageSize);

}
