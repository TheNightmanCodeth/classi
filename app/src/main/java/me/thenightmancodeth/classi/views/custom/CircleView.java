package me.thenightmancodeth.classi.views.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import me.thenightmancodeth.classi.R;

/**
 * Created by thenightman on 1/8/17.
 **/

public class CircleView extends TextView {
    private int backgroundColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
    private int textColor;
    private Paint circlePaint;

    public CircleView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);

        TypedArray a = ctx.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CircleView,
                0, 0);

        try {
            textColor = a.getColor(R.styleable.CircleView_text_color, ContextCompat.getColor(getContext(), R.color.colorAccent));
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(backgroundColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int h = this.getHeight();
        int w = this.getWidth();

        int diameter = ((h > w) ? h : w);
        int radius = diameter/2;

        this.setHeight(diameter);
        this.setWidth(diameter);

        canvas.drawCircle(diameter/2, diameter/2, radius, circlePaint);

        super.onDraw(canvas);
    }

    public void setBackgroundColor(int bgColor) {
        circlePaint.setColor(bgColor);
        invalidate();
        requestLayout();
    }
}
