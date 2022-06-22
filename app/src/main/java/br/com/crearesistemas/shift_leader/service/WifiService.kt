package br.com.crearesistemas.shift_leader.service

import android.app.AlertDialog
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import br.com.crearesistemas.shift_leader.R
import br.com.crearesistemas.shift_leader.db_service.viewmodel.MachineViewModel
import br.com.crearesistemas.socket.client.service.ClientSocketService
import br.com.crearesistemas.socket.client.service.MachineStatusSingleton
import com.thanosfisherman.wifiutils.WifiUtils
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener
import com.thanosfisherman.wifiutils.wifiDisconnect.DisconnectionErrorCode
import com.thanosfisherman.wifiutils.wifiDisconnect.DisconnectionSuccessListener
import java.text.SimpleDateFormat
import java.util.*


/**
 * !!! REFACTORED CLASS!!!
 * @author Luiz Gregory
 */

class WifiService( private val context: Context, private val application : Application, private val wifiManager: WifiManager)
{
    private var scanning                    : Boolean = false
    private var countResult                 = 0
    private val scanHandler                 = Handler(Looper.getMainLooper())
    private var lastScanDate                : Date? = null
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var machineService     : MachineViewModel
    private lateinit var ssid               : String
    private lateinit var pass               : String
    private val completeDate: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    /**
     *
     */
    companion object
    {
        private val TAG = WifiService::class.java.simpleName
    }


    /**
     *
     */
    init {

        val wifiScanReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                if (success) {
                    updateMachines()
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        context.registerReceiver(wifiScanReceiver, intentFilter)
    }


    /**
     *
     */
    fun startScanningLooping()
    {
        this.scanHandler.postDelayed(this.refreshListWifi, 1L)
    }


    /**
     *
     */
    private var refreshListWifi: Runnable = object : Runnable
    {
        override fun run()
        {
            if(scanning() > 0) {
                scanHandler.postDelayed(this, 35000L)
            }
            else {
                scanHandler.postDelayed(this, 15000L)
            }
        }
    }


    /**
     *
     */
    private fun scanning() : Int
    {
        val diffTime = if(this.lastScanDate == null) 0 else this.differenceInSeconds(this.lastScanDate!!, Date())
        if(this.lastScanDate == null || (!this.scanning && diffTime > 45)) {
            this.scanning = true
            this.lastScanDate = Date()
            wifiManager.startScan()
//            println("-----------------------------------------------------------")
//            println("- SCANNNING")
        }
        return this.countResult
    }


    /**
     *
     */
    private fun updateMachines()
    {
        val results = wifiManager.scanResults
        this.scanning = false
        this.countResult = results.size

        if (results.isEmpty()) {
            return
        }

//        println("--------------------------------------------")
//        results.forEach {
//            println("${it.SSID} = ${it.level}")
//        }

        this.machineService = MachineViewModel( application )
        this.machineService.getAll().forEach { itMachine ->

            itMachine.signal    = -999
            itMachine.bssid     = ""

            results.forEach { _itWifi ->
                if (_itWifi.SSID == itMachine.description) {
                    itMachine.signal    = _itWifi.level
                    itMachine.bssid     = _itWifi.BSSID
                }
            }

            machineService.save(itMachine)
        }
    }


    /**
     *
     */
    private var networkConnectionCallback = object : ConnectionSuccessListener
    {
        override fun success()
        {
            Toast.makeText(context, "CONNECTION SUCCESS", Toast.LENGTH_LONG).show()
            startSocketService()
        }

        override fun failed(errorCode: ConnectionErrorCode)
        {
            Toast.makeText(context, "DIDN'T CONNECT TO WIFI '$ssid'. PLEASE TRY AGAIN!", Toast.LENGTH_LONG).show()
        }
    }

    /**
     *
     */
    fun connect(ssid: String, pass: String)
    {
        println("fun connect($ssid: String, $pass: String)")

        this.ssid = ssid
        this.pass = pass

        WifiUtils.withContext(context)
            .connectWith(ssid, pass)
            .setTimeout(30000)
            .onConnectionResult(this.networkConnectionCallback)
            .start()
    }


    /**
     *
     */
    private var networkDisconnectionCallback = object : DisconnectionSuccessListener
    {
        override fun success()
        {
            println("override fun failed(errorCode: ConnectionErrorCode)")
        }

        override fun failed(errorCode: DisconnectionErrorCode)
        {
            println("override fun failed(errorCode: ConnectionErrorCode)")
        }
    }


    /**
     *
     */
    private fun disconnect()
    {
        WifiUtils.withContext(context).disconnect(networkDisconnectionCallback)
    }


    /**
     *
     */
    fun getConnectedBssid(): String?
    {
        return wifiManager.connectionInfo.bssid
    }


    /**
     *
     */
    private fun differenceInSeconds(startDate: Date, endDate: Date): Long {
        return (endDate.time - startDate.time) / 1000
    }


    /**
     *
     */
    private fun startSocketService()
    {
        val layoutInflater = LayoutInflater.from(context)
        val builder     = AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert)
        val dialogView  = layoutInflater.inflate(R.layout.feature_status_window, null)
        val message     = dialogView.findViewById<TextView>(R.id.menssagerDialog)
        val progress    = dialogView.findViewById<ProgressBar>(R.id.progressBar)
        val uncheck     = dialogView.findViewById<ImageView>(R.id.uncheckcomm)
        val check       = dialogView.findViewById<ImageView>(R.id.checkcomm)
        val handler     = Handler(Looper.getMainLooper())

        MachineStatusSingleton.messagesSocket.clear()
        MachineStatusSingleton.messagesSocket.add("STARTED COMMUNICATION TO MACHINE.\n")

        val refreshList: Runnable = object : Runnable
        {
            override fun run()
            {
                var finish = false
                try {
                    var messages = ""

                    MachineStatusSingleton.messagesSocket.forEach {

                        messages = "$messages$it"
                        message.text = messages

                        if (message.text.indexOf("FINISHED SUCCESSFUL") > -1) {
                            progress.visibility = View.INVISIBLE
                            uncheck.visibility = View.INVISIBLE
                            check.visibility = View.VISIBLE
                            machineService.updateSyncDate(ssid, completeDate.format(Date()))
                            finish = true
                        }
                        else if (message.text.indexOf("CONNECTION FAILED") > -1) {
                            println("")
                            uncheck.visibility = View.VISIBLE
                            check.visibility = View.INVISIBLE
                            progress.visibility = View.INVISIBLE
                            finish = true
                        }
                    }
                }
                catch (e: Exception) {
                    finish = true
                }

                if(!finish) {
                    handler.postDelayed(this, 3000L)
                }
                else {
                    scanHandler.postDelayed(refreshListWifi, 1L)
                    disconnect()
                }
            }
        }
        handler.postDelayed(refreshList, 1L)

        try {
            builder.setView(dialogView)
            builder.setCancelable(true)
            builder.show()
            builder.create().show()
        }
        catch (e: Exception) {
        }

        Thread {
            Thread.sleep(10000)
            context.startService(Intent(context, ClientSocketService::class.java))
        }.start()
    }


}