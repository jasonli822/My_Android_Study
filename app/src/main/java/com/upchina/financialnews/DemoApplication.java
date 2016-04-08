package com.upchina.financialnews;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.upchina.financialnews.inject.component.AppComponent;
import com.upchina.financialnews.inject.component.DaggerAppComponent;
import com.upchina.financialnews.inject.module.AppModule;

public class DemoApplication extends Application {
    private static DemoApplication applicationContext;

    private AppComponent appComponent;

    /**
     * 内存泄露检测
     */
    private RefWatcher refWatcher;
    public static RefWatcher getRefWatcher(Context context) {
        DemoApplication application = (DemoApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = this;

        initImageLoader(getApplicationContext());

        appComponent = DaggerAppComponent.builder().appModule(new AppModule()).build();

        // 内存泄露检测
        refWatcher = LeakCanary.install(this);
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .denyCacheImageMultipleSizesInMemory()
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();

        ImageLoader.getInstance().init(config);
    }


    public static DemoApplication getInstance() {
        return applicationContext;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
