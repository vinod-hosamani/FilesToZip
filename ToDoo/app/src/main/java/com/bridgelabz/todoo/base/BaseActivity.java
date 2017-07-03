package com.bridgelabz.todoo.base;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by bridgeit on 27/3/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public abstract void initView();
    public  abstract void setOnClickListener();


    public abstract void enterFromBottomAnimation();

    public abstract void exitToBottomAnimation();

}
