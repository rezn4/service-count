package android.rezndm.servicecount

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.preference.PreferenceManager
import android.rezndm.servicecount.Const.Companion.COUNT_DATA
import android.rezndm.servicecount.Const.Companion.INTENT_ACTION_COUNT
import java.util.*


class CountService: Service(){
    private var count: Int = 0
    @Volatile private var isServiceRunning = true
    private lateinit var repository: CountRepository

    override fun onCreate() {
        super.onCreate()
        repository = Repository(PreferenceManager.getDefaultSharedPreferences(applicationContext))
        count = repository.getCount()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        repository.saveDate(getCurrentDate())

        Thread {
            val broadcastIntent = Intent()
            broadcastIntent.action = INTENT_ACTION_COUNT

            while (isServiceRunning){
                if (isServiceRunning){
                    count++
                    broadcastIntent.putExtra(COUNT_DATA, count)
                    sendBroadcast(broadcastIntent)
                    Thread.sleep(5000)
                }
            }
        }.start()

        return START_NOT_STICKY
    }

    override fun onDestroy(){
        isServiceRunning = false
        repository.saveCount(count)
        super.onDestroy()
    }

    private fun getCurrentDate() : String{
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR).toString()
        val month = formatDate(formatMonth(calendar.get(Calendar.MONTH)).toString())
        val day = formatDate(calendar.get(Calendar.DAY_OF_MONTH).toString())

        val hour = formatDate(calendar.get(Calendar.HOUR_OF_DAY).toString())
        val minute = formatDate(calendar.get(Calendar.MINUTE).toString())
        val second = formatDate(calendar.get(Calendar.SECOND).toString())

        val sb = StringBuilder()

        return sb.append("$day/").append("$month/").append(year).
            append(" $hour:").append("$minute:").append(second).toString()
    }

    private fun formatMonth(month: Int): Int{
        return month + 1
    }

    private fun formatDate(value: String): String{
        return if (value.length == 1){
            "0$value"
        } else {
            value
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}