package com.fatur_atir_cahya.nyucidimana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.fatur_atir_cahya.nyucidimana.database.LaundromatManager;
import com.fatur_atir_cahya.nyucidimana.database.SessionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        SessionManager sessionManager = new SessionManager(this);
        LaundromatManager laundromatManager = new LaundromatManager(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(sessionManager.hasToken()) {
                    if(sessionManager.getRole().equals("1")) {
                        if(laundromatManager.hasLaundromat()) {
                            intent = new Intent(getApplicationContext(), UserDashboardActivity.class);
                        } else {
                            intent = new Intent(getApplicationContext(), RegisterLaundromatActivity.class);
                        }
                    } else {
                        intent = new Intent(getApplicationContext(), UserDashboardActivity.class);
                    }
                } else {
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                }

                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}