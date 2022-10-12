package osp.leobert.android.drawableworkshop.drawable

import android.animation.ValueAnimator
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.animation.LinearInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import org.xmlpull.v1.XmlPullParser
import osp.leobert.android.drawableworkshop.R
import kotlin.math.min
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * <p><b>Package:</b> osp.leobert.android.drawableworkshop.drawable </p>
 * <p><b>Project:</b> DrawableWorkShop </p>
 * <p><b>Classname:</b> AnimLetterDrawable2 </p>
 * Created by leobert on 2022/9/27.
 */
class AnimLetterDrawable2 : Drawable(), Animatable {
    private class Size(val type: Int) : ReadWriteProperty<AnimLetterDrawable2, Float?> {
        private var prop: Float? = null
        override fun getValue(thisRef: AnimLetterDrawable2, property: KProperty<*>): Float? {
            return prop ?: thisRef.run {
                val rect = Rect()
                this.paint.getTextBounds(this.letters, 0, this.letters.length, rect)
                val s = when (type) {
                    0 -> rect.width()
                    else -> rect.height()
                }.toFloat()
                prop = s
                prop
            }
        }

        override fun setValue(thisRef: AnimLetterDrawable2, property: KProperty<*>, value: Float?) {
            prop = value
        }

    }

    private fun obtainAttributes(
        res: Resources,
        theme: Resources.Theme?, set: AttributeSet, attrs: IntArray
    ): TypedArray {
        return theme?.obtainStyledAttributes(set, attrs, 0, 0) ?: res.obtainAttributes(set, attrs)
    }

    private val tag = "AnimLetterDrawable"

    var letters: String = "A"
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

    private val totalFrames = 30 * 3 //3 second, 30frames per second

    private val valueAnimator = ValueAnimator.ofInt(totalFrames).apply {
        duration = 3000L

        this.interpolator = LinearInterpolator()

        addUpdateListener {
            setFrame(it.animatedValue as Int)
        }
    }

    private var frameIndex = 0

    private val originalLetterLocations = SparseArray<PointF>()

    private val finalLetterLocations = SparseArray<PointF>()

    private var width by Size(0)
    private var height by Size(1)

    private val paint = Paint().apply {
        textSize = 60f
        color = Color.CYAN
    }

    override fun getIntrinsicHeight(): Int {
        return height?.toInt() ?: -1
    }

    override fun getIntrinsicWidth(): Int {
        return width?.toInt() ?: -1
    }

    override fun draw(canvas: Canvas) {
        Log.d(tag, "on draw,$letters , $height,$frameIndex")

        val progress = if (totalFrames > 1) {
            frameIndex.toFloat() / (totalFrames - 1).toFloat()
        } else {
            1f
        }

        paint.alpha = min(255, (255 * progress).toInt() + 100)

        for (i in letters.indices) {
            val endPoint: PointF = finalLetterLocations.get(i)
            val startPoint: PointF = originalLetterLocations.get(i)
            val x: Float = startPoint.x + (endPoint.x - startPoint.x) * progress
            val y: Float = startPoint.y + (endPoint.y - startPoint.y) * progress
            canvas.drawText(letters[i].toString(), x, y, paint)
        }

    }

    override fun setAlpha(alpha: Int) {
        //ignore
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        //ignore
    }

    @Deprecated("Deprecated in Java", ReplaceWith("PixelFormat.TRANSLUCENT", "android.graphics.PixelFormat"))
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
        val a: TypedArray = obtainAttributes(r, theme, attrs, R.styleable.anim_letter_drawable)
        letters = a.getString(R.styleable.anim_letter_drawable_android_text) ?: "A"

        textSize = a.getDimension(R.styleable.anim_letter_drawable_android_textSize, 60f)
        color = a.getColor(R.styleable.anim_letter_drawable_android_color, Color.CYAN)

        a.recycle()

        paint.color = color
        paint.textSize = textSize
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        Log.d(tag, "onBoundsChange, $bounds")

        height = bounds.height().toFloat()
        width = bounds.width().toFloat()

        calcLetterStartEndLocations()

        invalidateSelf()
    }

    private fun calcLetterStartEndLocations() {
        originalLetterLocations.clear()
        finalLetterLocations.clear()

        val height = this.height ?: throw IllegalStateException("height cannot be null")
        val width = this.width ?: throw IllegalStateException("width cannot be null")

        val centerY: Float = height / 2f + paint.textSize / 2

        val totalLength = paint.measureText(letters)
        val startX = (width - totalLength) / 2

        var currentStartX = startX

        for (i in letters.indices) {
            val str: String = letters[i].toString()
            val currentLength: Float = paint.measureText(str)


            originalLetterLocations.put(
                i, PointF(
                    Math.random().toFloat() * width, Math.random()
                        .toFloat() * height
                )
            )

            finalLetterLocations.put(i, PointF(currentStartX, centerY))
            currentStartX += currentLength

        }
    }

    private fun setFrame(frame: Int) {
        if (frame >= totalFrames) {
            return
        }
        frameIndex = frame
        invalidateSelf()
    }

    override fun start() {
        Log.d(tag, "start called")
        valueAnimator.start()
    }

    override fun stop() {
        valueAnimator.cancel()
        setFrame(0)
    }

    override fun isRunning(): Boolean {
        return valueAnimator.isRunning
    }

}