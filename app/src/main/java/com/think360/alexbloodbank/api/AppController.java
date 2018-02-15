package com.think360.alexbloodbank.api;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.think360.alexbloodbank.api.interfaces.AppComponent;
import com.think360.alexbloodbank.api.interfaces.DaggerAppComponent;


public class AppController extends Application {

    private static SharedPreferences sharedPreferencesBloodDonation;
    private AppComponent component;

  private RxBus bus;
    @Override
    public void onCreate() {
        super.onCreate();

       bus = new RxBus();
       component = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .httpModule(new HttpModule())
                .build();
        sharedPreferencesBloodDonation = getSharedPreferences("think360BloodDonation", MODE_PRIVATE);




    }

  public RxBus bus() {
       return bus;
   }



    public AppComponent getComponent() {
        return component;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static SharedPreferences getSharedPref() {
        return sharedPreferencesBloodDonation;
    }

}

