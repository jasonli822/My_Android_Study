package com.upchina.financialnews.support;

public class Constants {

    public static final Integer PAGE_SIZE = 10;

    public static final class Url {
        private static final String BASE_URL = "http://www.upchina.com/";
        // 推荐要闻，示例url: http://www.upchina.com/media/topic/recommend/1/2503359375/10
        public static final String TOPIC_RECOMMEND = "http://www.upchina.com/media/topic/recommend/1/";

        // 推荐要闻缩略图，示例 http://www.upchina.com/upload/recommend/2016-02-25/1456380291419.jpg
        private static final String BASE_UPLOAD_IMAGE_URL = BASE_URL  + "upload/";
        public static final String TOPIC_RECOMMEND_UPLOAD_IMAGE = BASE_UPLOAD_IMAGE_URL + "recommend";

        // 要闻详细页，示例url:http://www.upchina.com/topic_detail-12438.html
        public static final String TOPIC_RECOMMEND_DETAIL = BASE_URL + "topic_detail-";
        public static final String TOPIC_SUFFIX = ".html";
    }
}
