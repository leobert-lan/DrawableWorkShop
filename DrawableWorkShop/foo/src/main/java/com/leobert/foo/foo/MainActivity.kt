package com.leobert.foo.foo

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.leobert.foo.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        Log.e("foo", "onCreate")
        //
        //com.leobert.foo.foo.MainActivity
//        Intent()
        val btn = findViewById<Button>(R.id.btn)
        var index = 0
        var s = System.currentTimeMillis()

        val action: Runnable = object : Runnable {
            override fun run() {
                if(index == 0) {
                    s = System.currentTimeMillis()
                }
                Log.e("lmsg", "$index, offset time ${System.currentTimeMillis() - s - index * 30}")
                index++
                if (index < 100) {
                    btn.postOnAnimationDelayed(this, 30L - 10L /*hide api:android.view.Choreographer#subtractFrameDelay*/)
                } else {
                    Log.e("lmsg", "finish, total time ${System.currentTimeMillis() - s}")

                }
            }
        }

        btn.setOnClickListener {
            index = 0
            s = System.currentTimeMillis()
            it.postOnAnimationDelayed(action, 0L)
        }

        val btn2 = findViewById<Button>(R.id.btn2)
        btn2.setOnClickListener {
            testAnimator()
        }
    }

    fun testAnimator() {
        val animator = ValueAnimator.ofInt(100)
        animator.duration = 3000L
        animator.interpolator = LinearInterpolator()
        val s = System.currentTimeMillis()
        animator.addUpdateListener {
            val index = it.animatedValue as Int
            Log.e("lmsg", "${it.animatedValue}, offset time ${System.currentTimeMillis() - s - index * 30}")
        }
        animator.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("foo", "onDestroy")
    }
}