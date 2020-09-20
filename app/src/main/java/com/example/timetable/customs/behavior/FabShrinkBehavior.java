package com.example.timetable.customs.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FabShrinkBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {
    public FabShrinkBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton child, @NonNull View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton child, @NonNull View dependency) {
        float translationY = getFabTranslationYForSnackBar(parent, child);
        float percentComplete = -translationY / dependency.getHeight();
        float scaleFactor = 1 - percentComplete;

        if (scaleFactor > 1 || scaleFactor < 0) return false;

        child.setScaleX(scaleFactor);
        child.setScaleY(scaleFactor);

        if (scaleFactor < 0.1) child.setVisibility(View.GONE);
        else child.setVisibility(View.VISIBLE);
        return false;
    }

    private float getFabTranslationYForSnackBar(CoordinatorLayout parent, FloatingActionButton fab) {
        float minOffset = 0;
        final List<View> dependencies = parent.getDependencies(fab);
        for (int i = 0, z = dependencies.size(); i < z; i++) {
            final View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(minOffset,
                        ViewCompat.getTranslationY(view) - view.getHeight());
            }
        }

        return minOffset;
    }
}
