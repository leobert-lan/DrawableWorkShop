package osp.leobert.android.drawableworkshop.drawable

import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Animatable2
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import org.xmlpull.v1.XmlPullParser
import osp.leobert.android.drawableworkshop.R
import kotlin.math.min
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * <p><b>Package:</b> osp.leobert.android.drawableworkshop.drawable </p>
 * <p><b>Project:</b> DrawableWorkShop </p>
 * <p><b>Classname:</b> AnimLetterDrawable </p>
 * Created by leobert on 2021/1/29.
 */
class AnimLetterDrawable : Drawable(), Animatable2, Runnable {
    private class Size(val type: Int) : ReadWriteProperty<AnimLetterDrawable, Float?> {
        private var prop: Float? = null
        override fun getValue(thisRef: AnimLetterDrawable, property: KProperty<*>): Float? {
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

        override fun setValue(thisRef: AnimLetterDrawable, property: KProperty<*>, value: Float?) {
            prop = value
        }

    }

    private fun obtainAttributes(
        res: Resources,
        theme: Resources.Theme?, set: AttributeSet, attrs: IntArray
    ): TypedArray {
        return theme?.obtainStyledAttributes(set, attrs, 0, 0) ?: res.obtainAttributes(set, attrs)
    }

    val tag = "AnimLetterDrawable"

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

    private var frameIndex = 0

    private val totalFrames = 30 * 3 //3 second, 30frames per second

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

        val progress = frameIndex.toFloat() / totalFrames.toFloat()


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
            // TODO: 2021/2/1 consider padding for letters inner
            currentStartX += currentLength

        }
    }


    private val animationCallbacks: MutableSet<Animatable2.AnimationCallback> = linkedSetOf()

    private var mAnimating: Boolean = false

    private fun setFrame(frame: Int, unschedule: Boolean, animate: Boolean) {
        if (frame >= totalFrames) {
            return
        }
        mAnimating = animate
        frameIndex = frame

        if (unschedule || animate) {
            unscheduleSelf(this)
        }
        if (animate) {
            // Unscheduling may have clobbered these values; restore them
            frameIndex = frame

            scheduleSelf(this, SystemClock.uptimeMillis() + durationPerFrame)
        }
        invalidateSelf()
    }

    private fun nextFrame(unschedule: Boolean) {
        var nextFrame: Int = frameIndex + 1
        val isLastFrame = nextFrame + 1 == totalFrames
        if (nextFrame + 1 > totalFrames) {
            nextFrame = totalFrames - 1
        }

        setFrame(nextFrame, unschedule, !isLastFrame)
    }

    private val durationPerFrame = 3000 / totalFrames

    override fun start() {
        Log.d(tag, "start called")
        mAnimating = true

        if (!isRunning) {
            // Start from 0th frame.
            setFrame(
                frame = 0, unschedule = false, animate = false
            )
        } else {
            setFrame(
                frame = 0, unschedule = false, animate = true
            )
        }
    }

    override fun stop() {
        mAnimating = false

        if (isRunning) {
            frameIndex = 0
            //un-schedule it at first
            unscheduleSelf(this)

            setFrame(0, unschedule = true, animate = false)
        }
    }

    override fun isRunning(): Boolean {
        return mAnimating
    }

    override fun registerAnimationCallback(callback: Animatable2.AnimationCallback) {
        animationCallbacks.add(callback)
    }

    override fun unregisterAnimationCallback(callback: Animatable2.AnimationCallback): Boolean {
        return animationCallbacks.remove(callback)
    }

    override fun clearAnimationCallbacks() {
        animationCallbacks.clear()
    }

    override fun run() {
        Log.d(tag, "callback by schedule")
        if (isRunning) {
            nextFrame(false)
        } else {
            //safe call
            setFrame(0, unschedule = true, animate = false)
        }

    }

}