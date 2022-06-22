package br.com.crearesistemas.shift_leader.ui.datasync

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import br.com.crearesistemas.shift_leader.R
import br.com.crearesistemas.shift_leader.db_service.model.Machine
import br.com.crearesistemas.shift_leader.service.WifiService
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 */
internal class DataSyncListAdapter( private val context: Context, private val machineList: List<Machine>, private val wifiService : WifiService) : BaseAdapter()
{
    private var layoutInflater  : LayoutInflater? = null
    private val completeDate    : SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    override fun getCount(): Int
    {
        return machineList.size
    }

    override fun getItem(p0: Int): Any?
    {
        return null
    }

    override fun getItemId(p0: Int): Long
    {
        return 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View
    {
        var convertView = view
        if (layoutInflater == null) {
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.list_datasync, null)
        }

        val nameText            = convertView!!.findViewById(R.id.name)         as TextView
        val dateText            = convertView!!.findViewById(R.id.date)         as TextView
        val imageViewWifi       = convertView!!.findViewById(R.id.icon_wifi)    as ImageView
        val iconOnOff           = convertView!!.findViewById(R.id.icon_onoff)   as ImageView
        val buttonConnection    = convertView!!.findViewById(R.id.btn_connect)  as Button

        val machine = machineList[position]
        val machineConnected = wifiService.getConnectedBssid()

        iconOnOff.setImageResource(R.drawable.status_red)

        if(machine.lastSyncDate.isNullOrEmpty())  {
            machine.lastSyncDate = "NOT SYNCHRONIZED"
        }
        else {
            try {
                val localDate = completeDate.parse(machine.lastSyncDate)
                val diffTime = this.differenceInSeconds(localDate, Date())
                if ( diffTime < 60*60*32 ) { // 32 horas
                    iconOnOff.setImageResource(R.drawable.status_yellow)
                }
                if ( diffTime < 60*60*16 ) { // 16 horas
                    iconOnOff.setImageResource(R.drawable.status_green)
                }
            }
            catch ( e : Exception) {
            }
        }

        nameText.text = machine.description
        dateText.text = machine.lastSyncDate

        if(machine.signal > -45)        imageViewWifi.setImageResource(R.drawable.wifi_full)
        else if(machine.signal > -90)   imageViewWifi.setImageResource(R.drawable.wifi_half)
        else if(machine.signal > -135)  imageViewWifi.setImageResource(R.drawable.wifi_low)
        else if(machine.signal > -150)  imageViewWifi.setImageResource(R.drawable.wifi_empty)
        else                            imageViewWifi.setImageResource(R.drawable.ic_baseline_wifi_off_24)

        buttonConnection.isEnabled  = (machine.signal > -90)
        buttonConnection.visibility = if (machine.signal > -90) View.VISIBLE else View.INVISIBLE

        if(machineConnected == null) {
            buttonConnection.setOnClickListener {
                buttonConnection.text = "CONNECTING..."
                buttonConnection.isEnabled = false
                wifiService.connect(machine.hotspotSsid!!, machine.hotspotPassword!!)
            }
        }
        else {
            buttonConnection.visibility = View.INVISIBLE
        }

        return convertView
    }



    /**
     *
     */
    private fun differenceInSeconds(startDate: Date, endDate: Date): Long {
        return (endDate.time - startDate.time) / 1000
    }

}