package com.caocong.service.rounderimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by caocong on 10/8/16.
 */
public class RoundImageView extends ImageView {
    private PorterDuffXfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private int mRadius;
    private static final int MIN_RADIUS = 100;
    private Canvas mImageCanvas, mRoundCanvas;
    private Bitmap mImageBitmap, mRoundBitmap;

    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                widthSize = MIN_RADIUS;
                break;
        }

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                heightSize = MIN_RADIUS;
                break;
        }
        widthSize = heightSize = Math.max(widthSize, heightSize);
        mRadius = widthSize / 2;
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        drawCirle2(canvas, bitmap);

    }

    /**
     * 先画圆形，再拼接图片
     * @param canvas
     * @param bitmap
     */
    private void drawCirle(Canvas canvas, Bitmap bitmap) {
        Paint paint = new Paint();
        //src,新画上去的图
        Bitmap src = bitmap;
        //dst,先画上去的图
        Bitmap mask = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        //在dst上做画,先画圆形，再把图片贴上去

        //Canvas是画布,这里指定了画布的区域,画到一个Bitmap中去
        Canvas cc = new Canvas(mask);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        cc.drawARGB(0, 0, 0, 0);
        cc.drawCircle(mRadius, mRadius, mRadius, paint);
        cc.drawBitmap(mask, 0, 0, paint);

        //把新图拼上去
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        cc.drawBitmap(src, 0, 0, paint);
        paint.setXfermode(null);

        //系统画布绘制最终结果
        canvas.drawBitmap(mask, 0, 0, null);
    }

    /**
     * 先画图片，再拼圆形
     * @param canvas
     * @param bitmap
     */
    private void drawCirle2(Canvas canvas, Bitmap bitmap) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        //src和dst
        Bitmap src = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Bitmap mask = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        //src
        Canvas cc = new Canvas(src);
        cc.drawBitmap(bitmap, 0, 0, null);

        //dst
        Canvas mCanvas = new Canvas(mask);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        mCanvas.drawARGB(0, 0, 0, 0);
        mCanvas.drawCircle(mRadius, mRadius, mRadius, paint);

        //dst拼接到src
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        cc.drawBitmap(mask, 0, 0, paint);

        //绘src
        canvas.drawBitmap(src, 0, 0, null);

    }
}
