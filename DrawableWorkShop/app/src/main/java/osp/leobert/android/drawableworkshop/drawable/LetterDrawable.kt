package osp.leobert.android.drawableworkshop.drawable

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log

/**
 * <p><b>Package:</b> osp.leobert.android.drawableworkshop.drawable </p>
 * <p><b>Project:</b> DrawableWorkShop </p>
 * <p><b>Classname:</b> LetterDrawable </p>
 * Created by leobert on 2021/1/29.
 */
class LetterDrawable : Drawable() {
    val tag = "LetterDrawable"

    var letter: Char = 'A'

    val paint = Paint().apply {
        textSize = 60f
        color = Color.CYAN
    }

    override fun draw(canvas: Canvas) {
        Log.d(tag, "on draw")
        canvas.drawText(letter.toString(), 60f, 60f, paint)
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
}