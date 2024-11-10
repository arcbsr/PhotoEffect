package com.crop.phototocartooneffect.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class AnimationUtils {

    public static void shineEffectAnim(int width, View shine) {
        // Create a TranslateAnimation that moves the view horizontally
        TranslateAnimation animation = new TranslateAnimation(0, width + shine.getWidth(), 0, 0);
        animation.setDuration(1000); // Duration of the animation in milliseconds
        animation.setFillAfter(false); // The view doesn't stay in the end position after animation
        animation.setInterpolator(new AccelerateDecelerateInterpolator()); // Smooth in and out

// Set the animation to reverse and repeat infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse the animation back
        animation.setRepeatCount(Animation.INFINITE); // Repeat indefinitely

// Start the animation
        shine.startAnimation(animation);

    }

    public static void animateBounce(View view) {
        // Create a scale animation (shrink and expand)
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.9f, 1.0f, // Start and end values for the X axis scaling
                0.9f, 1.0f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling (center)
                Animation.RELATIVE_TO_SELF, 0.5f  // Pivot point of Y scaling (center)
        );

        scaleAnimation.setDuration(300); // Duration in milliseconds
        scaleAnimation.setInterpolator(new BounceInterpolator()); // Add a bounce effect
        view.startAnimation(scaleAnimation);
    }

    public static void neonEffectAnim(View view, View shine) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, view.getLayoutParams().width);
        animator.setDuration(2000); // Animation duration in milliseconds
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);

        animator.addUpdateListener(animation -> {
            // Get the animated value and set it as the x-position of the neon line
            float animatedValue = (float) animation.getAnimatedValue();
            shine.setTranslationX(animatedValue);
        });

        // Ensure the shine view is visible
        shine.setVisibility(View.VISIBLE);

        // Start the animation on the main thread
        view.post(() -> animator.start());
    }

    public static ValueAnimator startRevealAnimation(int mainImageWidth, View shiningLine, ImageView overlayImageView) {
        // Create a ValueAnimator that animates from 0 to the width of the main image
        ValueAnimator animator = ValueAnimator.ofInt(-20, mainImageWidth + 20);
        animator.setDuration(AppSettings.ANIMATION_DURATION); // Set the duration for the full reveal
        animator.setInterpolator(new CycleInterpolator(.5f));
        animator.setRepeatCount(ValueAnimator.INFINITE);
//        animator.setRepeatMode(ValueAnimator.REVERSE);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                shiningLine.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);

            }
        });
        animator.addUpdateListener(animation -> {
            // Get the animated value (current width of the reveal)
            int animatedValue = (int) animation.getAnimatedValue();

            // Move the shining line
            shiningLine.setTranslationX(animatedValue);

            // Clip the overlay image to reveal the main image up to the animated value
            overlayImageView.setClipBounds(new Rect(animatedValue - 5, 0, overlayImageView.getWidth(), overlayImageView.getHeight()));
            overlayImageView.invalidate();
        });

        // Start the animation
        animator.start();
        return animator;
    }


}
