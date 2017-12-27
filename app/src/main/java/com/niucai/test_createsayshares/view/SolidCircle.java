package com.niucai.test_createsayshares.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Qi on 2017/12/23.
 */

public class SolidCircle extends View {

    private Paint paint;
    private Context context;

    public SolidCircle(Context context) {
        super(context);
        this.context = context;
    }

    public SolidCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint = new Paint(); //设置一个笔刷大小是3的黄色的画笔
        this.paint.setAntiAlias(true); //消除锯齿
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = getWidth()/2;
        paint.setColor(0xFFFF000D);
        canvas.drawCircle(center, dip2px(context,100), 100, paint);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
