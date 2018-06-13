package org.providentiam.jejuniisdiebus

import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v4.app.Fragment
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewSwitcher
import com.github.clans.fab.FloatingActionMenu
import kotlinx.android.synthetic.main.content_running.*
import org.providentiam.jejuniisdiebus.utils.FabHelper
import org.providentiam.jejuniisdiebus.utils.FabHelperApi23


class RunningFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_running, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fasting_goal = 36 * 60L * 60L

        goal_chronometer.setFactory(factory)

        chronometer.start()
        chronometer.setOnChronometerTickListener {
            val value = fasting_goal - getSecondsFromDurationString(chronometer.text.toString())
            val hours = value / 3600
            val minutes =  (value - (hours * 3600)) / 60
            goal_chronometer?.setText("$hours:$minutes to reach goal")
        }

        fabClock.setOnClickListener { _ ->
            fabAction()
        }
    }

    private fun fabAction() {
        val mainFragment = MainFragment()
        val enterTransition = TransitionInflater.from(this.activity).inflateTransition(R.transition.content_main_in)
        chronometer.stop()

        enterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition?) {
                mainFragment.setupView()
            }

            override fun onTransitionStart(transition: Transition?) {
                val fab_menu = mainFragment.activity!!.findViewById<FloatingActionMenu>(R.id.fab_menu)
                fab_menu.visibility = View.GONE
            }

            override fun onTransitionPause(transition: Transition?) {
            }

            override fun onTransitionResume(transition: Transition?) {
            }

            override fun onTransitionCancel(transition: Transition?) {
            }
        })

        mainFragment.enterTransition = enterTransition
        mainFragment.exitTransition = TransitionInflater.from(this.activity).inflateTransition(R.transition.content_main_out)

        fragmentManager!!.beginTransaction()
                .replace(R.id.scene_root, mainFragment)

                .runOnCommit({
                    val fab = mainFragment.activity!!.findViewById<FloatingActionButton>(R.id.fab)
                    fab.show()

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (fab.drawable is AnimatedVectorDrawableCompat) {
                            FabHelper.resetAnimation(fab, R.drawable.fasting_to_plus)
                        } else {
                            FabHelperApi23.resetAnimation(fab)
                        }
                    } else {
                        FabHelper.resetAnimation(fab, R.drawable.fasting_to_plus)
                    }
                })
                .commit()
    }

    private fun getSecondsFromDurationString(value: String): Long {

        val parts = value.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (parts.size < 2 || parts.size > 3)
            return 0

        var seconds = 0
        var minutes = 0
        var hours = 0

        if (parts.size == 2) {
            seconds = Integer.parseInt(parts[1])
            minutes = Integer.parseInt(parts[0])
        } else if (parts.size == 3) {
            seconds = Integer.parseInt(parts[2])
            minutes = Integer.parseInt(parts[1])
            hours = Integer.parseInt(parts[0])
        }

        return seconds + minutes * 60L + hours * 3600L
    }

    private val factory = ViewSwitcher.ViewFactory {
        val t = TextView(activity)
        t.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        t.setTextAppearance(activity, android.R.style.TextAppearance_Small
        )
        t
    }
}