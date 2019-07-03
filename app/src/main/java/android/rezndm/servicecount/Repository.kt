package android.rezndm.servicecount

import android.content.SharedPreferences

interface CountRepository {
    fun getCount():Int
    fun getDate():String
    fun saveCount(count: Int)
    fun saveDate(date: String)
}

class Repository (private val prefs: SharedPreferences):CountRepository {
    override fun saveCount(count: Int) {
        val editor = prefs.edit()
        editor.putInt(Const.PREFERENCES_COUNT_DATA, count)
        editor.apply()
    }

    override fun saveDate(date: String) {
        val editor = prefs.edit()
        editor.putString(Const.PREFERENCES_TIME_DATA, date)
        editor.apply()
    }

    override fun getCount(): Int {
        return prefs.getInt(Const.PREFERENCES_COUNT_DATA, 0)
    }

    override fun getDate(): String {
        return prefs.getString(Const.PREFERENCES_TIME_DATA, " ")
    }
}