package android.rezndm.servicecount

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.preference.PreferenceManager
import android.rezndm.servicecount.Const.Companion.COUNT_DATA
import android.rezndm.servicecount.Const.Companion.INTENT_ACTION_COUNT
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.app.ActivityManager

class MainActivity : AppCompatActivity(), MainView {
    private val intentFilter: IntentFilter = IntentFilter()
    private lateinit var presenter: MainPresenter
    private lateinit var service: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intentFilter.addAction(INTENT_ACTION_COUNT)

        presenter = MainPresenterImpl(this,
            Repository(PreferenceManager.getDefaultSharedPreferences(applicationContext))
        )

        presenter.loadCountData()
        presenter.loadDateData()

        service = Intent(this, CountService::class.java)

        btn_start_service.setOnClickListener {
            if (!isServiceRunning(CountService::class.java)){
                startService(service)
                Toast.makeText(this, getString(R.string.toast_service_started), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.toast_service_already_running), Toast.LENGTH_SHORT).show()
            }
        }

        btn_stop_service.setOnClickListener {
            if (isServiceRunning(CountService::class.java)){
                stopService(service)
                Toast.makeText(this, getString(R.string.toast_service_stopped), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.toast_service_isnt_started), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun showDate(date: String) {
        textview_date.text = date
    }

    override fun showCount(count: Int) {
        textview_count.text = count.toString()
    }

    private val broadcastReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (intent.action == INTENT_ACTION_COUNT){
                    val count = intent.getIntExtra(COUNT_DATA, 0)
                    presenter.updateCountData(count)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onStop() {
        super.onStop()
        stopService(service)
    }
}
