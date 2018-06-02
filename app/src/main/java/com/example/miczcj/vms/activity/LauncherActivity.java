package com.example.miczcj.vms.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.miczcj.vms.QDMainActivity;


/**
 * @author cginechen
 * @date 2016-12-08
 */

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            Log.i("TAG","执行了吗？");
            finish();
            return;
        }
        Log.i("TAG","没有执行");
        Intent intent = new Intent(this, QDMainActivity.class);
        startActivity(intent);
        finish();
    }
}
