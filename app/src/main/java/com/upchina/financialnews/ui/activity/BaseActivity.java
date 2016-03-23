package com.upchina.financialnews.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.upchina.financialnews.R;

public class BaseActivity extends RxAppCompatActivity {
    protected Toolbar mToolBar;
    protected int layoutResID = R.layout.activity_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(layoutResID);

        // 使用ToolBar替换ActionBar
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
    }
}
