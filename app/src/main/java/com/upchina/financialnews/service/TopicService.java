package com.upchina.financialnews.service;


import com.upchina.financialnews.bean.Topic;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TopicService {
    @GET("{maxScore}/{pageSize}")
    public Call<Topic> getTopicRecommend(@Path("maxScore") String maxScore, @Path("pageSize") int pageSize);
}
