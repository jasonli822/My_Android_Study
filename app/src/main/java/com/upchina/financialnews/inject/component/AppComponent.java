package com.upchina.financialnews.inject.component;


import com.upchina.financialnews.inject.module.AppModule;
import com.upchina.financialnews.ui.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(dependencies = AppModule.class)
public interface AppComponent {
    void inject(MainActivity activity);
}
