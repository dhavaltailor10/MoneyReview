package com.hashrail.moneyreview;

import android.app.Application;

/**
 * Created by new-3 on 7/6/2016.
 */
public class MyApplication extends Application {

    String cur;

    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }
}
