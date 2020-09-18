package com.example.timetable.customs;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

public class CustomValueAnim {

    public static void verticalTranslateAnim(final View view, final int startPoint, final int endPoint
            , TimeInterpolator interpolator, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startPoint, endPoint);

        final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int marginTop = (Integer) animation.getAnimatedValue();
                lp.setMargins(0, marginTop, 0, 8);
                view.setLayoutParams(lp);
                view.requestLayout();

            }
        });
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.start();
    }

    public static void verticalScaleAnim(final View view, final int startPoint, final int endPoint
            , TimeInterpolator interpolator, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startPoint, endPoint);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (Integer) animation.getAnimatedValue();
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
                lp.height = height;
                view.setLayoutParams(lp);
                view.requestLayout();
            }
        });
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.start();
    }

}
