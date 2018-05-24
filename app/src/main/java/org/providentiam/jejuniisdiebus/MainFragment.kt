package org.providentiam.jejuniisdiebus

import android.animation.PropertyValuesHolder
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.db.chart.animation.Animation
import com.db.chart.model.BarSet
import com.db.chart.renderer.AxisRenderer
import com.db.chart.tooltip.Tooltip
import com.db.chart.util.Tools
import kotlinx.android.synthetic.main.content_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupChart()
        setupSummary()
    }

    private fun setupSummary() {
        val fasting_count = 32
        val fasting_month_count = 12
        val fasting_goal = 36
        val fasting_average = 16.5

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DATE, -3)
        val fasting_last_date = Date(calendar.timeInMillis)

        val formatter = SimpleDateFormat("dd-MM-yyyy")

        val fasting_last_duration = 16
        val fasting_interval = 72

        val text = "<p>You have tracked $fasting_count fasting periods so far.<br><b>This month count is $fasting_month_count periods</b>.</p><p>Your goal is to fast for <b>at least $fasting_goal hours</b> and your actual <b>average is $fasting_average hours</b>.</p><p>Your last fast was on ${formatter.format(fasting_last_date)} ($fasting_last_duration hours).<br>It has been <b>$fasting_interval hours since the last fasting period</b></p>"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            summary_table.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            summary_table.text = Html.fromHtml(text)
        }
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
        val tip = Tooltip(this.context, R.layout.square_tooltip)

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
}