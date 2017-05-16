package com.example.athma_000.gdir;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 * Created by Eminosoft on 1/5/2017.
 */

public class EnableMultiDex extends MultiDexApplication {
    private static EnableMultiDex enableMultiDex;
    public static Context context;

    public EnableMultiDex(){
        enableMultiDex=this;
    }

    public static EnableMultiDex getEnableMultiDexApp() {
        return enableMultiDex;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

    }
}
