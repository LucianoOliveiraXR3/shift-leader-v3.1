package br.com.crearesistemas.shift_leader.ui.datasync

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.wifi.WifiManager
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import br.com.crearesistemas.shift_leader.R
import br.com.crearesistemas.shift_leader.db_service.viewmodel.MachineViewModel
import br.com.crearesistemas.shift_leader.service.APIService
import br.com.crearesistemas.shift_leader.singleton.MessageSingleton
import br.com.crearesistemas.shift_leader.service.WifiService
import br.com.crearesistemas.socket.client.service.MachineStatusSingleton
import com.google.android.material.tabs.TabLayout
import java.lang.Thread.sleep
import java.text.SimpleDateFormat
import java.util.*


/**
 * Este fragmento é responsável por gerenciar a interface de data sync
 */
class DataSyncFragment : Fragment()
{
    private val formatDate  : SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private lateinit var rootView           : View
    private lateinit var machinesList       : ListView
    private lateinit var cloudModeLayout    : ConstraintLayout
    private lateinit var tabLayout          : TabLayout
    private lateinit var wifiManager        : WifiManager
    private lateinit var wifiService        : WifiService
    private lateinit var machineService     : MachineViewModel

    private var tabPosition : Int = 0
    private val mInterval   = 15000
    private var mHandler    : Handler? = null


    /**
     *
     */
    companion object
    {
        private val TAG = DataSyncFragment::class.java.simpleName
    }



    /**
     * NÃO CONPREENDI EXATAMENTE A NECESSIDADE DE HARD RELOAD.
     * NOS TESTES EXECUTADOS, PARECE DISPENSÁVEL ESTE PROCEDIMENTO
     */
    private fun hardReloadConnection(context: Context, message: String)
    {
//        val packageManager = context.packageManager
//        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
//        val componentName = intent!!.component
//        val mainIntent = Intent.makeRestartActivityTask(componentName)
//        mainIntent.putExtra("message", message)
//        context.startActivity(mainIntent)
//        Runtime.getRuntime().exit(0)
    }



    /**
     *
     */
    override fun onResume()
    {
        super.onResume()
        this.listMachines()
    }


    /**
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        this.listMachines()

        this.mHandler = Handler(Looper.getMainLooper())
        this.mStatusChecker.run()
    }


    /**
     *
     */
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View
    {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        this.rootView           = inflater.inflate(R.layout.fragment_datasync, container, false)
        this.machinesList       = this.rootView.findViewById(R.id.listview_datasync_machines)
        this.cloudModeLayout    = this.rootView.findViewById(R.id.layout_cloud_mode)
        this.tabLayout          = this.rootView.findViewById(R.id.tabLayout)
        this.wifiManager        = context?.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
        this.wifiService        = WifiService(requireContext(), requireActivity().application, wifiManager)
        this.machineService     = MachineViewModel(requireActivity().application)

        this.rootView.findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE

        if (requireActivity().intent.extras?.getBoolean("cloud_mode") == true) {
            this.machinesList.visibility     = View.GONE
            this.cloudModeLayout.visibility  = View.VISIBLE
            this.tabLayout.getTabAt(1)?.select()
        }

        // START SCANNING WIFI LOOPING
        this.wifiService.startScanningLooping()

        // FORÇA RESET DE NIVEL DE SINAL WIFI
        this.cleanMachineSignalLevel()

        // CONTROLA EXIBIÇÃO DAS ABAS
        this.tabControl()

        // EXECUTA AÇÃO NO CLICK DE SYNC TO CLOUD
        this.rootView.findViewById<Button>(R.id.btn_last_sync_cloud).setOnClickListener {
            syncToCloud()
        }

        return this.rootView
    }


    /**
     *
     */
    private fun tabControl()
    {
        this.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tabPosition = tab.position
                when (tab.position) {
                    0 -> {
                        machinesList.visibility = View.VISIBLE
                        cloudModeLayout.visibility = View.GONE
                    }
                    1 -> {
                        val alertDialog = AlertDialog.Builder(requireActivity()).create()
                        alertDialog.setTitle("CLOUD MODE MUST BE INTERNET")
                        alertDialog.setMessage("Please, disable all iOT connections and provide network with internet connection before Sync to cloud. ")
                        alertDialog.setButton(
                            AlertDialog.BUTTON_NEUTRAL, "Yes, i have internet connection."
                        ) { dialog, _ ->
                            machinesList.visibility = View.GONE
                            cloudModeLayout.visibility = View.VISIBLE
                            dialog.dismiss()
                            hardReloadConnection(requireActivity(), "cloud_mode")
                        }
                        alertDialog.show()
                    }
                    else -> {
                        println("ELSE")
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })
    }


    /**
     *
     */
    private fun cleanMachineSignalLevel()
    {
        this.machineService.getAll().forEach {
            it.signal   = -999
            it.bssid    = ""
            machineService.save(it)
        }
    }


    /**
     *
     */
    private fun displaySyncDate()
    {
        try {
            var text = resources.getText(R.string.btn_last_sync_to_cloud_nodate)
            if (MachineStatusSingleton.lastSyncCloudDate != null) {
                text = resources.getText(R.string.btn_last_sync_to_cloud).toString() + " " + formatDate.format(MachineStatusSingleton.lastSyncCloudDate!!).toString()
            }
            this.rootView.findViewById<TextView>(R.id.btn_last_sync_to_cloud).text = text
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     *
     */
    private fun syncToCloud()
    {
        println("########################################################################################")
        println("# START SYNC TO CLOUD (BUTTON CLICK)")

        val progress    = this.rootView.findViewById<ProgressBar>(R.id.progressBar)
        val message     = this.rootView.findViewById<TextView>(R.id.menssagerDialog)
        val uncheck     = this.rootView.findViewById<ImageView>(R.id.uncheckcomm)
        val check       = this.rootView.findViewById<ImageView>(R.id.checkcomm)
        val handler     = Handler(Looper.getMainLooper())

        check.visibility    = View.INVISIBLE
        uncheck.visibility  = View.INVISIBLE
        progress.visibility = View.VISIBLE
        message.text    = ""
        MessageSingleton.messages.clear()
        MessageSingleton.messages.add("STARTED COMMUNICATION TO CLOUD.\n")

        val refreshList: Runnable = object : Runnable {
            override fun run() {
                try {
                    var messages = ""
                    var finish = false
                    MessageSingleton.messages.forEach {
                        messages = "$messages$it"
                        message.text = messages
                        if (message.text.indexOf("FINISHED SUCCESSFUL") > -1) {
                            progress.visibility = View.INVISIBLE
                            uncheck.visibility = View.INVISIBLE
                            check.visibility = View.VISIBLE
                            displaySyncDate()
                            finish = true
                        }
                        else if (message.text.indexOf("FAILED") > -1) {
                            uncheck.visibility = View.VISIBLE
                            check.visibility = View.INVISIBLE
                            progress.visibility = View.INVISIBLE
                            finish = true
                        }
                    }
                    if(!finish) {
                        handler.postDelayed(this, 2000L)
                    }
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        handler.postDelayed(refreshList, 1L)

        // LANÇA INTEGRAÇÃO COM API EM UMA THREAD ISOLADA
        Thread {
            sleep(100)
            APIService(requireActivity().application).syncToCloud()
            println("# FINAL SYNC TO CLOUD (BUTTON CLICK)")
            println("###############################################################################")
        }.start()
    }


    /**
     * Popula as listas com conteudo
     */
    private fun listMachines()
    {
        val machineService = MachineViewModel(requireActivity().application)
        val machineList = machineService.getAllFromSignalLevel()

        if (this.machinesList.headerViewsCount == 0) {
            val header = layoutInflater.inflate(  R.layout.datasync_header, this.machinesList, false ) as ViewGroup
            this.machinesList.addHeaderView(header, null, false)
        }
        this.machinesList.adapter = DataSyncListAdapter(context as Activity, machineList, this.wifiService)

    }



    /**
     *
     */
    override fun onDestroy() {
        super.onDestroy()
        mHandler!!.removeCallbacks(mStatusChecker)
    }


    /**
     *
     */
    var mStatusChecker: Runnable = object : Runnable
    {
        override fun run()
        {
            try {
                listMachines()
            }
            finally {
                mHandler!!.postDelayed(this, mInterval.toLong())
            }
        }
    }



    private fun getSSIDConnected(): String
    {
        return try {
            wifiManager.connectionInfo?.ssid.toString().replace("\"", "")
        } catch (e : Exception) {
            ""
        }
    }


}