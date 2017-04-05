package me.thenightmancodeth.classi.ui.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet

import me.thenightmancodeth.classi.R

/**
 * Created by thenightman on 1/8/17.
 */

class CircleView(ctx: Context, attrs: AttributeSet) : android.support.v7.widget.AppCompatTextView(ctx, attrs) {
    private val backgroundColor = ContextCompat.getColor(context, R.color.colorAccent)
    private var color: Int = 0
    private var circlePaint: Paint? = null

    init {

        val a = ctx.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CircleView,
                0, 0)

        try {
            color = a.getColor(R.styleable.CircleView_text_color, ContextCompat.getColor(context, R.color.colorAccent))
        } finally {
            a.recycle()
        }

        init()
    }

    private fun init() {
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.color = color

        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        circlePaint!!.style = Paint.Style.FILL
        circlePaint!!.color = backgroundColor
    }

    override fun onDraw(canvas: Canvas) {
        val h = this.height
        val w = this.width

        val diameter = if (h > w) h else w
        val radius = diameter / 2

        this.height = diameter
        this.width = diameter

        canvas.drawCircle((diameter / 2).toFloat(), (diameter / 2).toFloat(), radius.toFloat(), circlePaint!!)

        super.onDraw(canvas)
    }

    override fun setBackgroundColor(bgColor: Int) {
        circlePaint!!.color = bgColor
        invalidate()
        requestLayout()
    }
}
