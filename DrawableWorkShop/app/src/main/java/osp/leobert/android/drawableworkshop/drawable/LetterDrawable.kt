package osp.leobert.android.drawableworkshop.drawable

import android.content.res.Resources
import android.content.res.Resources.Theme
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import osp.leobert.android.drawableworkshop.R
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * <p><b>Package:</b> osp.leobert.android.drawableworkshop.drawable </p>
 * <p><b>Project:</b> DrawableWorkShop </p>
 * <p><b>Classname:</b> LetterDrawable </p>
 * Created by leobert on 2021/1/29.
 */
class LetterDrawable : Drawable() {

    private class Size(val type: Int) : ReadWriteProperty<LetterDrawable, Float?> {
        private var prop: Float? = null
        override fun getValue(thisRef: LetterDrawable, property: KProperty<*>): Float? {
            return prop ?: thisRef.run {
                val rect = Rect()
                this.paint.getTextBounds(this.letter, 0, this.letter.length, rect)
                val s = when (type) {
                    0 -> rect.width()
                    else -> rect.height()
                }.toFloat()
                prop = s
                prop
            }
        }

        override fun setValue(thisRef: LetterDrawable, property: KProperty<*>, value: Float?) {
            prop = value
        }

    }

    private fun obtainAttributes(
        res: Resources,
        theme: Theme?, set: AttributeSet, attrs: IntArray
    ): TypedArray {
        return theme?.obtainStyledAttributes(set, attrs, 0, 0) ?: res.obtainAttributes(set, attrs)
    }

    val tag = "LetterDrawable"

    var letter: String = "A"
        set(value) {
            field = value
            width = null
            height = null
            invalidateSelf()
        }

    var color: Int = Color.CYAN
        set(value) {
            field = value
            paint.color = value
            invalidateSelf()
        }

    var textSize: Float = 60f
        set(value) {
            field = value
            width = null
            height = null
            paint.textSize = value
            invalidateSelf()
        }

    private val paint = Paint().apply {
        textSize = 60f
        color = Color.CYAN
    }

    private var width by Size(0)
    private var height by Size(1)

    override fun getIntrinsicHeight(): Int {
        return height?.toInt() ?: -1
    }

    override fun getIntrinsicWidth(): Int {
        return width?.toInt() ?: -1
    }

    override fun draw(canvas: Canvas) {
        Log.d(tag, "on draw,$letter , $height")
        canvas.drawText(letter, 0f, height ?: 60f, paint)
    }

    override fun setAlpha(alpha: Int) {
        //ignore
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        //ignore
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun inflate(
        r: Resources,
        parser: XmlPullParser,
        attrs: AttributeSet,
        theme: Resources.Theme?
    ) {
        super.inflate(r, parser, attrs, theme)
        val a: TypedArray = obtainAttributes(r, theme, attrs, R.styleable.letter_drawable)
        letter = a.getString(R.styleable.letter_drawable_android_text) ?: "A"

        textSize = a.getDimension(R.styleable.letter_drawable_android_textSize, 60f)
        color = a.getColor(R.styleable.letter_drawable_color, Color.CYAN)

        a.recycle()

        paint.color = color
        paint.textSize = textSize
    }
}