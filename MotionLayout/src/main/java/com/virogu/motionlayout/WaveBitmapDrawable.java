package com.virogu.motionlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WaveBitmapDrawable extends Drawable implements Animatable {
    //控件中的完整正弦波的个数
    private static final float WAVE_SIZE = 2.5f;
    //正弦波的振幅
    private static final int MAX_WAVE_HEIGHT = 20;

    private int mViewWidth;
    private int mViewHeight;
    private Resources mResources;

    private Paint mShapePaint;

    private Drawable mBackgroundDrawable = null;
    private boolean mBitmapInvalid = true;

    private Paint mWavePaint;
    private Path mWavePath;

    private float mWaveHOffset = 0;
    private float mWaveVOffset = 0;

    private float mProgress = 0f;

    //水平动画
    private ValueAnimator mWaveHAnimator;
    //垂直动画
    private ValueAnimator mWaveVAnimator;

    private float mCornerRadius = 0;
    private boolean isStart = false;
    private boolean mReverse = false;

    public WaveBitmapDrawable(@NonNull Resources resources) {
        mResources = resources;

        mShapePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShapePaint.setColor(Color.WHITE);

        mWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWavePaint.setColor(Color.BLACK);
        mWavePaint.setStyle(Paint.Style.FILL);
        mWavePaint.setAlpha(150);
        mWavePath = new Path();

        //水平动画
        mWaveHAnimator = ValueAnimator.ofFloat(0, 1);
        mWaveHAnimator.setDuration(1000);
        mWaveHAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mWaveHAnimator.setInterpolator(new LinearInterpolator());
        mWaveHAnimator.addUpdateListener(animation -> {
            mWaveHOffset = (float) animation.getAnimatedValue();
            invalidateSelf();
        });

        mWaveVAnimator = ValueAnimator.ofFloat(0, 1);
        mWaveVAnimator.setDuration(1000);
        mWaveVAnimator.setInterpolator(new AccelerateInterpolator());
        mWaveVAnimator.addUpdateListener(animation -> mWaveVOffset = (float) animation.getAnimatedValue());
        mWaveVAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (isStart && !mWaveHAnimator.isStarted()) {
                    mWaveHAnimator.start();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isStart && mWaveHAnimator.isStarted()) {
                    mWaveHAnimator.cancel();
                }
            }
        });
    }

    /**
     * 是否反向覆盖，默认不反向
     */
    public void setReverse(boolean reverse) {
        mReverse = reverse;
    }

    public void setBackground(Bitmap bitmap) {
        if (bitmap != null) {
            mBackgroundDrawable = new BitmapDrawable(mResources, bitmap);
        } else {
            mBackgroundDrawable = null;
        }
        mBitmapInvalid = true;
    }

    public void setBackground(Drawable drawable) {
        mBackgroundDrawable = drawable;
        mBitmapInvalid = true;
    }


    public void setBackgroundColor(int color) {
        mBackgroundDrawable = new ColorDrawable(color);
        mBitmapInvalid = true;
    }

    public void setWaveColor(int color) {
        mWavePaint.setColor(color);
    }

    public void setWaveAlpha(int alpha) {
        mWavePaint.setAlpha(alpha);
    }

    public void setCornerRadius(int cornerRadius) {
        mCornerRadius = dip2px(cornerRadius);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mViewWidth = bounds.width();
        mViewHeight = bounds.height();
        mBitmapInvalid = true;
    }

    private void updateDrawable() {
        if (mBackgroundDrawable != null) {
            mBackgroundDrawable.setBounds(0, 0, mViewWidth, mViewHeight);
        }
    }

    @Override
    public final void draw(@NonNull Canvas canvas) {
        if (mBitmapInvalid) {
            updateDrawable();
            mBitmapInvalid = false;
        }

        int shapeCount = canvas.saveLayer(0, 0, mViewWidth, mViewHeight, null);
        if (mCornerRadius > 0) {
            canvas.drawRoundRect(0, 0, mViewWidth, mViewHeight, mCornerRadius, mCornerRadius, mShapePaint);
            mShapePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            int contentCount = canvas.saveLayer(0, 0, mViewWidth, mViewHeight, mShapePaint);
            drawContent(canvas);
            canvas.restoreToCount(contentCount);
            mShapePaint.setXfermode(null);
        } else {
            drawContent(canvas);
        }
        canvas.restoreToCount(shapeCount);
    }

    private void drawContent(Canvas canvas) {
        if (mBackgroundDrawable != null) {
            mBackgroundDrawable.draw(canvas);
        } else {
            //如果不画背景，需要先将画布置透明，否则波浪未覆盖区域，将会呈现白色
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }

        //画波浪
        mWavePath.rewind();

        int waveWidth = (int) (mViewWidth / WAVE_SIZE);
        int waveDepth = (int) ((1 - mProgress) * mViewHeight);

        mWavePath.moveTo((mWaveHOffset - 1) * waveWidth, waveDepth);

        float maxOffset = mWaveVOffset * MAX_WAVE_HEIGHT;
        int minOffset = Math.min(waveDepth, mViewHeight - waveDepth);
        float offset = Math.min(maxOffset, minOffset);
        for (int i = 0; i < WAVE_SIZE + 1; i++) {
            mWavePath.rCubicTo(waveWidth >> 1, -offset, waveWidth >> 1, offset, waveWidth, 0);
        }
        if (mReverse) {
            mWavePath.lineTo(mViewWidth, 0);
            mWavePath.lineTo(0, 0);
        } else {
            mWavePath.lineTo(mViewWidth, mViewHeight);
            mWavePath.lineTo(0, mViewHeight);
        }
        mWavePath.close();

        mWavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawPath(mWavePath, mWavePaint);
        mWavePaint.setXfermode(null);
    }

    @Override
    public void setAlpha(int alpha) {
        mShapePaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mShapePaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    @Override
    public void start() {
        if (!isStart) {
            isStart = true;
            mWaveVAnimator.start();
        }
    }

    @Override
    public void stop() {
        if (isStart) {
            isStart = false;
            mWaveVAnimator.reverse();
        }
    }

    @Override
    public boolean isRunning() {
        return mWaveVAnimator.isStarted() || mWaveHAnimator.isStarted();
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(@FloatRange(from = 0, to = 1) float progress) {
        mProgress = progress;
        if (!isRunning()) {
            invalidateSelf();
        }
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, mResources.getDisplayMetrics());
    }
}