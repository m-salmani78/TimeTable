package com.example.timetable.customs.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.example.timetable.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class TabBarBehavior extends CoordinatorLayout.Behavior<AppBarLayout> {
    private static final String TAG = "TabBarBehavior";
    private View collapsingToolbar;
    private View tabBar;
    private View toolbarText, toolbarImage;
    private int collapsedHeight=110, originalHeight = 220;
    private boolean viewSet = false;

    public TabBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child
            , @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        initViews(child);
        Log.i(TAG, "onStartNestedScroll: **original height=" + originalHeight + "\t**collapsed height=" + collapsedHeight);
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout
            , @NonNull AppBarLayout child, @NonNull View target, int dxConsumed
            , int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        initViews(child);

        if (dyConsumed > 0) {
            //scroll up
            if (collapsingToolbar.getLayoutParams().height > collapsedHeight) {
                int height = collapsingToolbar.getLayoutParams().height - dyConsumed;
                collapsingToolbar.getLayoutParams().height = Math.max(height, collapsedHeight);
                collapsingToolbar.requestLayout();

            }
        } else if (dyUnconsumed < 0) {
            //scroll down
            if (collapsingToolbar.getLayoutParams().height < originalHeight) {
                int height = collapsingToolbar.getLayoutParams().height - dyUnconsumed;
                collapsingToolbar.getLayoutParams().height = Math.min(height, originalHeight);
                collapsingToolbar.requestLayout();
            }
        }
        float progress = (float) (collapsingToolbar.getLayoutParams().height - originalHeight) / (collapsedHeight - originalHeight);

        toolbarText.setAlpha(1 - progress);
        toolbarText.setScaleX(1 - progress/4);
        toolbarText.setScaleY(1 - progress/4);

        toolbarImage.setAlpha(1 - progress);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) tabBar.getLayoutParams();
        int margin = originalHeight - collapsingToolbar.getLayoutParams().height;
        layoutParams.leftMargin = layoutParams.rightMargin = margin;
        tabBar.setLayoutParams(layoutParams);
    }

    private void initViews(AppBarLayout child) {
        if (viewSet) return;
        viewSet = true;
        Log.i(TAG, "initViews: **********");
        collapsingToolbar = child.findViewById(R.id.main_actionbar);
        tabBar = collapsingToolbar.findViewById(R.id.tabs);
        toolbarImage = collapsingToolbar.findViewById(R.id.main_toolbar_image);
        toolbarText = collapsingToolbar.findViewById(R.id.main_toolbar_txt);
        toolbarText.setPivotY(0);
        originalHeight = 210;
        View toolbar = collapsingToolbar.findViewById(R.id.main_toolbar);
        collapsedHeight = toolbar.getLayoutParams().height;
    }
}