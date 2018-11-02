package com.sorcererxw.matthiasheiderichphotography.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

import com.sorcererxw.matthiasheiderichphotography.services.WallpaperChangeService

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/14
 */

class ShortcutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val action = intent.action
        val intent = Intent(this, WallpaperChangeService::class.java)
        startService(intent)
        //        Toast.makeText(this, action, Toast.LENGTH_SHORT).show();
        finish()
    }
}
