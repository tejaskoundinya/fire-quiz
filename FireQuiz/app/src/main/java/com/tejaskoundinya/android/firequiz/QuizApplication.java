package com.tejaskoundinya.android.firequiz;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Tejas Koundinya on 09-01-2016.
 */
public class QuizApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
