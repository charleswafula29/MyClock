package com.andreasgift.myclock.Alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.andreasgift.myclock.Helper.Constants
import com.andreasgift.myclock.R
import kotlinx.android.synthetic.main.activity_alarm_notif.*

class AlarmNotifActivity : AppCompatActivity() {
    private val snoozeTiming = 600000L

    private var label: String? = null
    private lateinit var mMediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_notif)

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        intent.getStringExtra(Constants().ALARM_LABEL_KEY)?.let {
            label = it
            this.label_tv.setText(label)
        }
        playSound(soundUri)
    }

    fun dismissButton(view: View) {
        stopSound()
        finish()
    }

    fun snoozeButton(view: View) {
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val nextintent = Intent(this, AlarmReceiver::class.java)
        label?.let { intent.putExtra(Constants().ALARM_LABEL_KEY, label) }
        val pendingIntent = PendingIntent.getBroadcast(
            this@AlarmNotifActivity,
            0,
            nextintent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + snoozeTiming,
            pendingIntent
        )
        stopSound()
        finish()
    }

    private fun playSound(uri: Uri) {
        mMediaPlayer = MediaPlayer.create(this@AlarmNotifActivity, uri)
        mMediaPlayer.isLooping = true
        mMediaPlayer.start()
    }

    private fun stopSound() {
        mMediaPlayer?.let {
            it.stop()
            it.release()
        }
    }

    override fun onPause() {
        super.onPause()
        stopSound()
    }
}
