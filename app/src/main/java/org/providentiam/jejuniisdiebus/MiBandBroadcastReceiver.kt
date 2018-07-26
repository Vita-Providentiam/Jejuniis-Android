package org.providentiam.jejuniisdiebus

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MiBandBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        when (intent?.action) {
            "com.mc.miband.buttonPressed1" -> Toast.makeText(context, "Button pressed", Toast.LENGTH_SHORT).show()
            "com.mc.miband.buttonPressed2" -> Toast.makeText(context, "Double tap", Toast.LENGTH_SHORT).show()
            "com.mc.miband.buttonPressed3" -> Toast.makeText(context, "Triple tap", Toast.LENGTH_SHORT).show()

            "com.mc.miband.lift" -> Toast.makeText(context, "Band lift", Toast.LENGTH_SHORT).show()
            "com.mc.miband.lift2" -> Toast.makeText(context, "Band lift x2", Toast.LENGTH_SHORT).show()
            "com.mc.miband.lift3" -> Toast.makeText(context, "Band lift x3", Toast.LENGTH_SHORT).show()

            "com.mc.miband.connected" -> Toast.makeText(context, "Band dconnected", Toast.LENGTH_SHORT).show()
            "com.mc.miband.disconnected" -> Toast.makeText(context, "Band disconnected", Toast.LENGTH_SHORT).show()
            "com.mc.miband.tasker.notWearing" -> Toast.makeText(context, "Not wearing Band", Toast.LENGTH_SHORT).show()

            "com.mc.miband.heartRateGot" -> Toast.makeText(context, "Got heart rate ${intent?.extras.getInt("value")}", Toast.LENGTH_SHORT).show()
            "com.mc.miband.stepsGot" -> Toast.makeText(context, "Got steps ${intent?.extras.getInt("value")}", Toast.LENGTH_SHORT).show()
            "com.mc.miband.batteryStatGot" -> Toast.makeText(context, "Got battery charge ${intent?.extras.getInt("value")}", Toast.LENGTH_SHORT).show()
        }
    }
}