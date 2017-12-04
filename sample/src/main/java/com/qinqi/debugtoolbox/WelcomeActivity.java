package com.qinqi.debugtoolbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by qinqi on 2016/11/23.
 */

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
