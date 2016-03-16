package com.upchina.financialnews.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.upchina.financialnews.R;
import com.upchina.financialnews.adapter.NewsAdapter;
import com.upchina.financialnews.bean.News;
import com.upchina.financialnews.bean.Topic;
import com.upchina.financialnews.service.TopicService;
import com.upchina.financialnews.support.Constants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.khrystal.widget.KRecyclerView;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.news_list)
    KRecyclerView krecyclerView;

    @Bind(R.id.tv_load_empty)
    TextView mTvLoadEmpty;

    @Bind(R.id.tv_load_error)
    TextView mTvLoadError;

    private List<News> newsList = new ArrayList<News>();
    private NewsAdapter mAdapter;
    private boolean isRefreshed = false;
    private String maxScore = "0";
    private int pageSize = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_main;
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        ButterKnife.bind(MainActivity.this);

        // 设置Adapter
        mAdapter = new NewsAdapter(newsList);
        krecyclerView.setAdapter(mAdapter, 1, LinearLayoutManager.VERTICAL);
        krecyclerView.setLoadDataLintener(new KRecyclerView.LoadDataListener() {
            @Override
            public void loadData(int page) {
                MainActivity.this.loadData();
            }
        });
        //make KRecyclerView know how many items in a page
        krecyclerView.setItemCount(10);
        //make KRecyclerView know loaddata need to judge network can available
        //Notice when isUseByNetWork(true) and network can't available
        //the loading footer can click to load more again
        //krecyclerView.isUseByNetWork(true);
        krecyclerView.mPtrFrameLayout.setEnabled(false);
        krecyclerView.enableLoadMore();

        // 设置手势滑动监听器
        mSwipeRefreshLayout.setOnRefreshListener(this);
        // 设置刷新时动画的颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_primary);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshIf(!isRefreshed);
    }

    private void refreshIf(boolean prerequisite) {
        if (prerequisite) {
            loadData();
        }
    }

    private void loadData() {

        if (maxScore.equals("0")) {
            mSwipeRefreshLayout.setRefreshing(true);
            isRefreshed = false;
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.Url.TOPIC_RECOMMEND)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        TopicService newsService = retrofit.create(TopicService.class);

        newsService.getTopicRecommend(maxScore, pageSize)
                .subscribeOn(Schedulers.io()) // 指定subscribe发生在IO线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 subscriber 的回调发生在主线程
                .subscribe(new Observer<Topic>() {
                    @Override
                    public void onCompleted() {
                        isRefreshed = true;
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        isRefreshed = true;
                        mSwipeRefreshLayout.setRefreshing(false);

                        Toast.makeText(getApplicationContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();

                        krecyclerView.enableLoadMore();

                        if (maxScore.equals("0")) {
                            mTvLoadEmpty.setVisibility(View.GONE);
                            mTvLoadError.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onNext(Topic topic) {
                        mTvLoadEmpty.setVisibility(View.GONE);
                        mTvLoadError.setVisibility(View.GONE);

                        List<News> list = topic.getList();
                        if (list != null && list.size() > 0) {
                            if (maxScore.equals("0")) { // 第一页
                                mAdapter.updateNewsListAndNotify(list);
                            } else {
                                mAdapter.addNewsListAndNofity(list);
                            }
                            if (list.size() < pageSize) {
                                krecyclerView.cantLoadMore();
                            } else {
                                krecyclerView.enableLoadMore();
                                News news = list.get(pageSize - 1);
                                double a = Double.parseDouble(news.getPriority()) * 1000000;
                                BigDecimal bigDecimal = new BigDecimal(a);
                                maxScore = bigDecimal.toString();
                            }
                        } else {
                            if (maxScore.equals("0")) {
                                krecyclerView.cantLoadMore();
                            } else {
                                krecyclerView.enableLoadMore();
                            }

                            mTvLoadEmpty.setVisibility(View.VISIBLE);
                            mTvLoadError.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        maxScore = "0";
        loadData();
    }
}
