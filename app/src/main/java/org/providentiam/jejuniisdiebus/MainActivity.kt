package org.providentiam.jejuniisdiebus

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.providentiam.jejuniisdiebus.utils.FabHelper
import org.providentiam.jejuniisdiebus.utils.FabHelperApi23

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var broadCastReceiver: MiBandBroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (fab.drawable is AnimatedVectorDrawableCompat) {
                FabHelper.setupFloatingsButtons(fab, ::fabAction)
            } else {
                FabHelperApi23.setupFloatingsButtons(fab, ::fabAction)
            }
        } else {
            FabHelper.setupFloatingsButtons(fab, ::fabAction)
        }

        setupNavigation()

        Handler().postDelayed({
            val mainFragment = MainFragment()
            val transition :  TransitionSet? = TransitionInflater.from(this).inflateTransition(R.transition.content_main_in) as TransitionSet?
            mainFragment.enterTransition = transition
            mainFragment.exitTransition = TransitionInflater.from(this).inflateTransition(R.transition.content_main_out)

            transition?.addListener(object  : Transition.TransitionListener {
                override fun onTransitionEnd(transition: Transition) {
                    mainFragment.setupView()
                }

                override fun onTransitionResume(transition: Transition) {
                }

                override fun onTransitionPause(transition: Transition) {
                }

                override fun onTransitionCancel(transition: Transition) {
                }

                override fun onTransitionStart(transition: Transition) {
                }

            })

            supportFragmentManager.beginTransaction()
                    .replace(R.id.scene_root, mainFragment)
                    .commit()
        }, 1200)


    }

    override fun onPause() {
        super.onPause()

        unregisterReceiver(broadCastReceiver!!)
    }

    override fun onResume() {
        super.onResume()

        sendBroadcast(Intent("com.mc.miband.connectToBand"))
        sendBroadcast(Intent("com.mc.miband.heartRateRead"))
        sendBroadcast(Intent("com.mc.miband.stepsRead"))
        sendBroadcast(Intent("com.mc.miband.batteryStatRead"))

        if (broadCastReceiver == null) {
            broadCastReceiver = MiBandBroadcastReceiver()
        }

        val filter = IntentFilter()
        filter.addAction("com.mc.miband.buttonPressed1")
        filter.addAction("com.mc.miband.buttonPressed2")
        filter.addAction("com.mc.miband.buttonPressed3")

        filter.addAction("com.mc.miband.lift")
        filter.addAction("com.mc.miband.lift2")
        filter.addAction("com.mc.miband.lift3")
        filter.addAction("com.mc.miband.lift3")

        filter.addAction("com.mc.miband.connected")
        filter.addAction("com.mc.miband.disconnected")
        filter.addAction("com.mc.miband.tasker.notWearing")

        filter.addAction("com.mc.miband.heartRateGot")
        filter.addAction("com.mc.miband.stepsGot")
        filter.addAction("com.mc.miband.batteryStatGot")

        registerReceiver(broadCastReceiver!!, filter)
    }

    private fun setupNavigation() {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun fabAction() {
        fab_menu.visibility = View.VISIBLE
        fab.hide()

        val runningFragment = RunningFragment()
        runningFragment.enterTransition = TransitionInflater.from(this).inflateTransition(R.transition.content_running_in)
        runningFragment.exitTransition = TransitionInflater.from(this).inflateTransition(R.transition.content_running_out)

        supportFragmentManager.beginTransaction()
            .replace(R.id.scene_root, runningFragment)
            .commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings ->{
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_status -> {
                // Handle the status action
            }
            R.id.nav_plan -> {

            }
            R.id.nav_logbook -> {

            }
            R.id.nav_chart -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_settings -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
