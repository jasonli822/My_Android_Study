package com.upchina.financialnews.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.upchina.financialnews.R;
import com.upchina.financialnews.adapter.NewsAdapter;
import com.upchina.financialnews.bean.News;
import com.upchina.financialnews.bean.Topic;
import com.upchina.financialnews.net.UpChinaRestClient;
import com.upchina.financialnews.support.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private List<News> newsList = new ArrayList<News>();
    private NewsAdapter mAdapter;
    private boolean isRefreshed = false;
    private Integer maxScore = 0;
    private Integer pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_main;

        super.onCreate(savedInstanceState);

        initView();

    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.news_list);
        mRecyclerView.setHasFixedSize(true);

        // 设置布局管理器
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layout);

        // 设置Adapter
        mAdapter = new NewsAdapter(newsList);
        mRecyclerView.setAdapter(mAdapter);

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
            doRefresh();
        }
    }

    private void doRefresh() {

        String url = Constants.Url.TOPIC_RECOMMEND + maxScore + "/" + pageSize;
        UpChinaRestClient.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new Gson();
                Topic topic = gson.fromJson(response.toString(), Topic.class);
                List<News> list = topic.getList();
                if (list != null && list.size() > 0) {
                    mAdapter.updateNewsListAndNotify(list);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStart() {
                super.onStart();

                isRefreshed = false;
                mSwipeRefreshLayout.setRefreshing(true);
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
        doRefresh();
    }
}
