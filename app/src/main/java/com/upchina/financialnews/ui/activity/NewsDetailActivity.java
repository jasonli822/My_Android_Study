package com.upchina.financialnews.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.upchina.financialnews.R;
import com.upchina.financialnews.bean.News;
import com.upchina.financialnews.support.Constants;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsDetailActivity extends BaseActivity {
    @Bind(R.id.wv_news)
    WebView mWvNews;

    @Bind(R.id.cpb_loading)
    ContentLoadingProgressBar mCpbLoading;

    @Bind(R.id.iv_header)
    ImageView mImageView;

    @Bind(R.id.tv_source)
    TextView mTvSourece;

    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Bind(R.id.nested_view)
    NestedScrollView mNestedScrollView;

    @Bind(R.id.tv_load_empty)
    TextView mTvLoadEmpty;

    @Bind(R.id.tv_load_error)
    TextView mTvLoadError;

    private String url;
    private News mNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_news_detail;
        super.onCreate(savedInstanceState);

        // 增加一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 接收url值
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");
        mNews = (News) bundle.getSerializable("news");
        Log.i("获取到的url值为", url);

        initView();
        loadData();
    }

    private void initView() {
        ButterKnife.bind(NewsDetailActivity.this);

        mNestedScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mWvNews.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mWvNews.getSettings().setLoadsImagesAutomatically(true);
        // 设置缓存模式
        mWvNews.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        mWvNews.getSettings().setDomStorageEnabled(true);
        // 为可折叠toolbar设置标题
        mCollapsingToolbarLayout.setTitle(getString(R.string.app_name));
    }

    private void loadData() {

        mWvNews.loadUrl(url);

        // 显示头部图片
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.noimage)
                .showImageOnFail(R.drawable.noimage)
                .showImageForEmptyUri(R.drawable.noimage)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        String thumbnailUrl = Constants.Url.TOPIC_RECOMMEND_UPLOAD_IMAGE + mNews.getThumbnail();
        ImageLoader.getInstance().displayImage(thumbnailUrl, mImageView, displayImageOptions, new SimpleImageLoadingListener() {
            final List<String> displayImages = Collections.synchronizedList(new LinkedList<String>());

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage != null) {
                    ImageView imageView = (ImageView) view;
                    boolean firstDisplay = !displayImages.contains(imageUri);
                    if (firstDisplay) {
                        FadeInBitmapDisplayer.animate(imageView, 500);
                        displayImages.add(imageUri);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
