package com.leobert.foo.foo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.leobert.foo.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        Log.e("foo","onCreate")
        //
        //com.leobert.foo.foo.MainActivity
//        Intent()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("foo","onDestroy")
    }
}