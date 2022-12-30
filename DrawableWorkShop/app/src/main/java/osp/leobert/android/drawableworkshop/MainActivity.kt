package osp.leobert.android.drawableworkshop

import android.graphics.Color
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
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
        tvSpan.setCompoundDrawables(drawableStart, null, null, null)

        tvSpan.setOnClickListener {
//            drawable.callback = it //这种方式无效，Drawable和TextView之间无关联
            drawable.callback = object : Drawable.Callback {
                override fun invalidateDrawable(who: Drawable) {
                    it.invalidate()
                }

                override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
                    it.scheduleDrawable(who, what, `when`)
                }

                override fun unscheduleDrawable(who: Drawable, what: Runnable) {
                    it.unscheduleDrawable(who, what)
                }

            }
            drawable.start()
//            drawableStart.start()
        }

        val tvSpan2 = findViewById<TextView>(R.id.tv_span2)
        val infoBuilder = SpannableStringBuilder().append("Leobert")

        val madels = arrayListOf<String>("Lv.10", "持续创造", "笔耕不追", "夜以继日")
        val drawables: List<AnimLetterDrawable2> = madels.map { madel ->
            appendMadel(infoBuilder, madel).let { drawable ->
                drawable.callback = object : Drawable.Callback {
                    override fun invalidateDrawable(who: Drawable) {
                        tvSpan2.invalidate()
                    }

                    override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
                        tvSpan2.scheduleDrawable(who, what, `when`)
                    }

                    override fun unscheduleDrawable(who: Drawable, what: Runnable) {
                        tvSpan2.unscheduleDrawable(who, what)
                    }
                }
                drawable
            }
        }

        tvSpan2.text = infoBuilder
        tvSpan2.setOnClickListener {
            drawables.forEach {
                it.start()
            }
        }
        tvSpan2.movementMethod = LinkMovementMethod.getInstance()

        val tvSpan3 = findViewById<TextView>(R.id.tv_span3)
        tvSpan3.text = infoBuilder
        tvSpan3.setOnClickListener {
            drawables.forEach {
                it.start()
            }
        }
        tvSpan3.movementMethod = LinkMovementMethod.getInstance()
    }

    fun createADrawable(): AnimLetterDrawable2 {
        val drawable = AnimLetterDrawable2()
        drawable.textSize = 20f
        drawable.letters = "span"
        drawable.setBounds(0, 0, 100, 100)


        return drawable
    }

    fun appendMadel(builder: SpannableStringBuilder, madel: String): AnimLetterDrawable2 {
        val drawable = AnimLetterDrawable2()
        drawable.textSize = 20f
        drawable.letters = madel
        drawable.setBounds(0, 0, 100, 100)

        val imgSpan = ImageSpan(drawable)
        val ss = SpannableString(" *")
        ss.setSpan(imgSpan, 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        ss.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(this@MainActivity, madel, Toast.LENGTH_SHORT).show()
            }
        }, 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


        builder.append(ss)

        return drawable
    }
}