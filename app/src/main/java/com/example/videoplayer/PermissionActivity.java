package com.example.videoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PermissionActivity extends AppCompatActivity {
    Button allow_btn;
    public static final int STORAGE_PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        allow_btn = findViewById(R.id.allow_per_btn);
        if(ContextCompat.checkSelfPermission(PermissionActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(PermissionActivity.this, MainActivity.class);
            startActivity(intent);
        }else {
            allow_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(PermissionActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_REQUEST_CODE);
                    if(ContextCompat.checkSelfPermission(PermissionActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(PermissionActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}