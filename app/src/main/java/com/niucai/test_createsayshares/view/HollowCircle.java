package com.niucai.test_createsayshares.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Qi on 2017/12/23.
 */

public class HollowCircle extends View {
    private final Paint paint;
    private final Context context;

    public HollowCircle(Context context) {
        this(context,null);
    }

    public HollowCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.paint = new Paint();
        this.paint.setAntiAlias(true); //消除锯齿
        this.paint.setStyle(Paint.Style.STROKE); //绘制空心圆
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        int center = getWidth()/2;
        paint.setStrokeWidth(2);
        paint.setColor(Color.RED);
        canvas.drawCircle(center,dip2px(context,100),150,paint);
        super.onDraw(canvas);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
