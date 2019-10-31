package app.alan.alanexample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.alan.alansdk.Alan
import com.alan.alansdk.BasicSdkListener

const val CHANNEL_ID = "ForegroundServiceChannel"


/**
 * One don't need this if only wants to make background working SDK
 */
class MainService : Service() {

    inner class MainServiceBinder : Binder() {

        fun getSdk(): Alan {
            return alanSdk
        }
    }

    private lateinit var alanSdk: Alan

    override fun onBind(p0: Intent?): IBinder? {
        return MainServiceBinder()
    }

    override fun onCreate() {
        super.onCreate()
        alanSdk = Alan.getInstance(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        setForeground()

        alanSdk.registerCallback(object : BasicSdkListener(){

            override fun onEvent(event: String?, payload: String?) {
                super.onEvent(event, payload)
                if (event == "notification") {
                    //show notificaiton
                }
            }
        })
        return START_NOT_STICKY
    }

    private fun setForeground() {
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText("Hey Alan")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build()

        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
}