package com.example.tenantfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

       findViewById(R.id.textviewsignup).setOnClickListener(this);


    }

       @Override
            public void onClick(View view) {
                if (view.getId() == R.id.textviewsignup)
                    startActivity(new Intent(this, RegisterActivity.class));
             }
        }
