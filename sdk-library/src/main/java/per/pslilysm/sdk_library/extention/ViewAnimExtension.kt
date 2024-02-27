package per.pslilysm.sdk_library.extention

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation

/**
 * Extension for view animation
 *
 * @author pslilysm
 * Created on 2023/06/29 15:29
 * @since 2.2.0
 */

private const val DEFAULT_ANIM_DURATION = 280L


/**
 * Rotate view infinity
 *
 * @param duration the time (in milliseconds) spend when rotate 1 times,
 * @param animationListener the animation listener to be notified
 */
fun View.rotateInfinity(duration: Long = DEFAULT_ANIM_DURATION, animationListener: AnimationListener? = null) {
    val rotate = RotateAnimation(
        0f, 360f,
        Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f
    )

    rotate.duration = duration
    rotate.setAnimationListener(animationListener)
    rotate.interpolator = LinearInterpolator()
    rotate.repeatCount = Animation.INFINITE
    rotate.repeatMode = Animation.RESTART
    startAnimation(rotate)
}

/**
 * Show view with alpha animation
 *
 * @param duration how long this animation should last. the duration cannot be negative.
 * @param animationListener the animation listener to be notified
 */
fun View.showWithAlpha(duration: Long = DEFAULT_ANIM_DURATION, animationListener: AnimationListener? = null) {
    if (visibility != View.VISIBLE) {
        val alphaAnim = AlphaAnimation(0f, 1f)
        alphaAnim.duration = duration
        alphaAnim.fillAfter = true
        alphaAnim.setAnimationListener(animationListener)
        startAnimation(alphaAnim)
    }
}

/**
 * Hide view with alpha animation
 *
 * @param duration how long this animation should last. the duration cannot be negative.
 * @param animationListener the animation listener to be notified
 */
fun View.hideViewWithAlpha(duration: Long = DEFAULT_ANIM_DURATION, animationListener: AnimationListener? = null) {
    if (visibility == View.VISIBLE) {
        val alphaAnim = AlphaAnimation(1f, 0f)
        alphaAnim.duration = duration
        //            alphaAnim.setFillAfter(true);
        alphaAnim.setAnimationListener(animationListener)
        startAnimation(alphaAnim)
    }
}

/**
 * Show view with translate animation from bot to top
 *
 * @param duration how long this animation should last. the duration cannot be negative.
 * @param animationListener the animation listener to be notified
 */
fun View.showViewWithTranslateFromBottomToTop(duration: Long = DEFAULT_ANIM_DURATION, animationListener: AnimationListener? = null) {
    if (visibility != View.VISIBLE) {
        val transAnim = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f
        )
        transAnim.duration = duration
        transAnim.fillAfter = true
        transAnim.setAnimationListener(animationListener)
        startAnimation(transAnim)
    }
}

/**
 * Hide view with translate animation from top to bot
 *
 * @param duration how long this animation should last. the duration cannot be negative.
 * @param animationListener the animation listener to be notified
 */
fun View.hideViewWithTranslateFromTopToBottom(duration: Long = DEFAULT_ANIM_DURATION, animationListener: AnimationListener? = null) {
    if (visibility == View.VISIBLE) {
        val transAnim = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f,
        )
        transAnim.duration = duration
        transAnim.setAnimationListener(animationListener)
        startAnimation(transAnim)
    }
}

/**
 * Translate view from left to right
 *
 * @param duration how long this animation should last. the duration cannot be negative.
 * @param animationListener the animation listener to be notified
 */
fun View.translateFromLeftToRight(duration: Long = DEFAULT_ANIM_DURATION, animationListener: AnimationListener? = null) {
    val transAnim = TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f,
        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
    )
    transAnim.duration = duration
    transAnim.fillAfter = true
    transAnim.setAnimationListener(animationListener)
    startAnimation(transAnim)
}

/**
 * Translate view from right to left
 *
 * @param duration how long this animation should last. the duration cannot be negative.
 * @param animationListener the animation listener to be notified
 */
fun View.translateFromRightToLeft(duration: Long = DEFAULT_ANIM_DURATION, animationListener: AnimationListener? = null) {
    val transAnim = TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
    )
    transAnim.duration = duration
    transAnim.fillAfter = true
    transAnim.setAnimationListener(animationListener)
    startAnimation(transAnim)
}

/**
 * Hide view with translate animation from right to left
 *
 * @param duration how long this animation should last. the duration cannot be negative.
 * @param animationListener the animation listener to be notified
 */
fun View.hideViewWithTranslateFromRightToLeft(duration: Long = DEFAULT_ANIM_DURATION, animationListener: AnimationListener? = null) {
    if (visibility == View.VISIBLE) {
        val transAnim = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1f,
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
        )
        transAnim.duration = duration
        transAnim.setAnimationListener(animationListener)
        startAnimation(transAnim)
    }
}

/**
 * Show view with translate animation from right to left
 *
 * @param duration how long this animation should last. the duration cannot be negative.
 * @param animationListener the animation listener to be notified
 */
fun View.showViewWithTranslateFromRightToLeft(duration: Long = DEFAULT_ANIM_DURATION, animationListener: AnimationListener? = null) {
    if (visibility != View.VISIBLE) {
        val transAnim = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
        )
        transAnim.duration = duration
        transAnim.setAnimationListener(animationListener)
        startAnimation(transAnim)
    }
}

/**
 * Hide view with translate animation from left to right
 *
 * @param duration how long this animation should last. the duration cannot be negative.
 * @param animationListener the animation listener to be notified
 */
fun View.hideViewWithTranslateFromLeftToRight(duration: Long = DEFAULT_ANIM_DURATION, animationListener: AnimationListener? = null) {
    if (visibility == View.VISIBLE) {
        val transAnim = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
        )
        transAnim.duration = duration
        transAnim.setAnimationListener(animationListener)
        startAnimation(transAnim)
    }
}

/**
 * Show view with translate animation from left to right
 *
 * @param duration how long this animation should last. the duration cannot be negative.
 * @param animationListener the animation listener to be notified
 */
fun View.showViewWithTranslateFromLeftToRight(duration: Long = DEFAULT_ANIM_DURATION, animationListener: AnimationListener? = null) {
    if (visibility != View.VISIBLE) {
        val transAnim = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
        )
        transAnim.duration = duration
        transAnim.setAnimationListener(animationListener)
        startAnimation(transAnim)
    }
}