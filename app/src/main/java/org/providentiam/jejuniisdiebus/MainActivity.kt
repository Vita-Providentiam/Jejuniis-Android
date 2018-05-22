package org.providentiam.jejuniisdiebus

import android.animation.PropertyValuesHolder
import android.content.Intent
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.db.chart.animation.Animation
import com.db.chart.model.BarSet
import com.db.chart.renderer.AxisRenderer
import com.db.chart.tooltip.Tooltip
import com.db.chart.util.Tools
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.providentiam.jejuniisdiebus.utils.FabHelper
import org.providentiam.jejuniisdiebus.utils.FabHelperApi23


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
        setupChart()
    }

    private fun setupChart() {
        val labelsX = arrayOf("na", "na", "Sun", "Tue", "Wed", "Thu", "Sun")
        val values = floatArrayOf(0f, 0f, 38f, 17f, 17f, 21f, 16f)

        val barSet = BarSet(labelsX, values)
        barSet.color = Color.parseColor("#48a999")
        chart.addData(barSet)

        val thresholdPaint = Paint()
        thresholdPaint.color = Color.parseColor("#0079ae")
        thresholdPaint.style = Paint.Style.STROKE
        thresholdPaint.isAntiAlias = true
        thresholdPaint.strokeWidth = Tools.fromDpToPx(.75f)
        thresholdPaint.pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
        chart.setLabelThreshold( 2, 2, thresholdPaint)
        chart.setValueThreshold( 36f, 36f, thresholdPaint)

        val order = intArrayOf(2, 3, 4, 5, 6, 0, 1)

        val chartOneAction = Runnable(this::showTooltip)

        chart.setXLabels(AxisRenderer.LabelPosition.OUTSIDE)
                .setYLabels(AxisRenderer.LabelPosition.OUTSIDE)
                .setStep(18)
                .show(Animation().inSequence(.5f, order).withEndAction(chartOneAction))
    }

    private fun showTooltip() {
        // Tooltip
        val tip = Tooltip(this, R.layout.square_tooltip)

        tip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)).duration = 200

        tip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0f),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f)).duration = 200

        tip.setVerticalAlignment(Tooltip.Alignment.TOP_TOP)
        tip.setDimensions(Tools.fromDpToPx(18f).toInt(), Tools.fromDpToPx(18f).toInt())
        tip.setMargins(0, Tools.fromDpToPx(5f).toInt(), 0, 0)
        tip.prepare(chart.getEntriesArea(0)[2], 0f)

        chart.showTooltip(tip, true)
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
        fab.visibility = View.GONE
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
