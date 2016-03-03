package com.upchina.financialnews.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.upchina.financialnews.R;
import com.upchina.financialnews.adapter.NewsAdapter;
import com.upchina.financialnews.bean.News;
import com.upchina.financialnews.bean.Topic;
import com.upchina.financialnews.net.UpChinaRestClient;
import com.upchina.financialnews.support.Constants;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.khrystal.widget.KRecyclerView;


public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private KRecyclerView krecyclerView;
    private TextView mTvLoadEmpty;
    private TextView mTvLoadError;

    private List<News> newsList = new ArrayList<News>();
    private NewsAdapter mAdapter;
    private boolean isRefreshed = false;
    private String maxScore = "0";
    private Integer pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_main;

        super.onCreate(savedInstanceState);

        initView();

    }

    private void initView() {
        mTvLoadEmpty = (TextView) findViewById(R.id.tv_load_empty);
        mTvLoadError = (TextView) findViewById(R.id.tv_load_error);


        krecyclerView = (KRecyclerView) findViewById(R.id.news_list);

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

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
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

        String url = Constants.Url.TOPIC_RECOMMEND + maxScore + "/" + pageSize;
        UpChinaRestClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mTvLoadEmpty.setVisibility(View.GONE);
                mTvLoadError.setVisibility(View.GONE);

                Gson gson = new Gson();
                Topic topic = gson.fromJson(response.toString(), Topic.class);
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
                        double a =  Double.parseDouble(news.getPriority()) * 1000000;
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

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();

                krecyclerView.enableLoadMore();

                if (maxScore.equals("0")){
                    mTvLoadEmpty.setVisibility(View.GONE);
                    mTvLoadError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onStart() {
                super.onStart();

                if (maxScore.equals("0")) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    isRefreshed = false;
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();

                isRefreshed = true;
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    @Override
    public void onRefresh() {
        maxScore = "0";
        loadData();
    }
}
