package org.providentiam.jejuniisdiebus.utils

import android.graphics.drawable.Drawable
import android.support.design.widget.FloatingActionButton
import android.support.graphics.drawable.Animatable2Compat
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import org.providentiam.jejuniisdiebus.R

class FabHelper {
    companion object {
        fun setupFloatingsButtons(fab: FloatingActionButton, fabAction: () -> Unit) {
            fab.setOnClickListener { _ ->
                @Suppress("CAST_NEVER_SUCCEEDS")
                val anim = fab.drawable as AnimatedVectorDrawableCompat
                var callback: Animatable2Compat.AnimationCallback? = null
                callback = object : Animatable2Compat.AnimationCallback() {

                    override fun onAnimationEnd(drawable: Drawable?) {
                        super.onAnimationEnd(drawable)
                        fabAction()
                        anim.unregisterAnimationCallback(callback!!)
                    }
                }
                anim.registerAnimationCallback(callback)

                anim.start()
            }
        }

        fun resetAnimation(fab: FloatingActionButton, resId: Int) {
            val drawable = AnimatedVectorDrawableCompat.create(fab.context, resId);
            fab.setImageDrawable(drawable)
        }
    }
}