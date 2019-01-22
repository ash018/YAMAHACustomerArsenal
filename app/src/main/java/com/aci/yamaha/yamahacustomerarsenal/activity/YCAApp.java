package com.aci.yamaha.yamahacustomerarsenal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aci.yamaha.yamahacustomerarsenal.utility.AppPreferences;

public class YCAApp extends AppCompatActivity {
    AppPreferences cpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cpref = new AppPreferences(this);
        Intent intent = null;

        if (cpref.getLoggedIn() == true) {
            intent = new Intent(YCAApp.this, NavActivity.class);
        } else {
            intent = new Intent(YCAApp.this, LoginActivity.class);
        }

        startActivity(intent);
        YCAApp.this.finish();
    }
}
