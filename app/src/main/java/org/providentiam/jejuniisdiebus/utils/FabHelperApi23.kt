package org.providentiam.jejuniisdiebus.utils

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton

@RequiresApi(Build.VERSION_CODES.M)
class FabHelperApi23 {
    companion object {
        fun setupFloatingsButtons(fab: FloatingActionButton, fabAction: () -> Unit) {
            fab.setOnClickListener { _ ->
                @Suppress("CAST_NEVER_SUCCEEDS")
                val anim = fab.drawable as AnimatedVectorDrawable
                var callback: Animatable2.AnimationCallback? = null
                callback = object : Animatable2.AnimationCallback() {

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
    }
}