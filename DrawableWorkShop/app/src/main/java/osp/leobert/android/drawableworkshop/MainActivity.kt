package osp.leobert.android.drawableworkshop

import android.graphics.Color
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import osp.leobert.android.drawableworkshop.databinding.ActivityMainBinding
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


    }
}