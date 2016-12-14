package com.sorcererxw.matthiasheiderichphotography.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sorcererxw.matthiasheiderichphotography.services.WallpaperChangeService;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/14
 */

public class ShortcutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String action = getIntent().getAction();
        Intent intent =new Intent(this, WallpaperChangeService.class);
        startService(intent);
//        Toast.makeText(this, action, Toast.LENGTH_SHORT).show();
        finish();
    }
}
