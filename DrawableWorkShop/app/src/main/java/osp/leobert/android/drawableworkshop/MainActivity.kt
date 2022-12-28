package osp.leobert.android.drawableworkshop

import android.graphics.Color
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import osp.leobert.android.drawableworkshop.databinding.ActivityMainBinding
import osp.leobert.android.drawableworkshop.drawable.AnimLetterDrawable2
import osp.leobert.android.drawableworkshop.drawable.LetterDrawable

class MainActivity : AppCompatActivity() {
    val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val letterDrawable: LetterDrawable? = binding.testView.background as LetterDrawable?
        //ContextCompat.getDrawable(this, R.drawable.letter_drawable) as LetterDrawable?

//        binding.testView2.background = letterDrawable

        binding.btnChangeColor.setOnClickListener {
            letterDrawable?.let {
                Log.d(tag, "has callback: ${it.callback == null}")
                it.color = Color.MAGENTA
                it.invalidateSelf()
            }
        }

        binding.btnStartAnim.setOnClickListener { _ ->

            binding.viewAnim.background.let {
                if (it is Animatable) {
                    it.start()
                }
            }
        }

        binding.btnStopAnim.setOnClickListener {
            binding.viewAnim.background.let {
                if (it is Animatable) {
                    it.stop()
                }
            }
        }


        val tvSpan = findViewById<TextView>(R.id.tv_span)

        val drawable = createADrawable()
        val imgSpan = ImageSpan(drawable)

        val ss = SpannableString("ImageSpan *")
        ss.setSpan(imgSpan, 10, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvSpan.text = ss

        val drawableStart = createADrawable()
        tvSpan.setCompoundDrawables(drawableStart,null,null,null)

        tvSpan.setOnClickListener {
            drawable.start()
            drawableStart.start()
        }


    }

    fun createADrawable(): AnimLetterDrawable2 {
        val drawable = AnimLetterDrawable2()
        drawable.textSize = 20f
        drawable.letters = "span"
        drawable.setBounds(0, 0, 100, 100)
        return drawable
    }
}