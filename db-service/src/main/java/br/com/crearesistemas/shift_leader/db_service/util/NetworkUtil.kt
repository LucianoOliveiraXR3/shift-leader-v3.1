package br.com.crearesistemas.shift_leader.db_service.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import java.util.*

class NetworkUtil {

    @SuppressLint("HardwareIds")
    fun getMacAddress(context: Context): String {
        val manager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = manager.connectionInfo

        return info.macAddress.toUpperCase(Locale.ROOT)
    }

}