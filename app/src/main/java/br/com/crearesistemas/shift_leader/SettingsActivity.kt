package br.com.crearesistemas.shift_leader

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.text.InputType
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.crearesistemas.shift_leader.db_service.model.ShiftLeaderConfig
import br.com.crearesistemas.shift_leader.db_service.viewmodel.ShiftLeaderConfigViewModel
import br.com.crearesistemas.shift_leader.singleton.ConfigSingleton
import br.com.crearesistemas.socket.client.service.MachineStatusSingleton
import java.util.*
import kotlin.math.min

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonGabung = findViewById<Button>(R.id.btn_back)
        buttonGabung.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )

        }

        val buttonEn = findViewById<ImageButton>(R.id.lang_en)
        buttonEn.setOnClickListener {
            setLocale(this, "")
            Toast.makeText(this, "Changing language...", Toast.LENGTH_SHORT).show()

            startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )

        }

        val buttonIn = findViewById<ImageButton>(R.id.lang_in)
        buttonIn.setOnClickListener {

            setLocale(this, "in")

            Toast.makeText(this, "Mengubah Bahasa...", Toast.LENGTH_SHORT).show()

            startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )
        }


        val globalConfigService = ShiftLeaderConfigViewModel(application)
        val globalConfig = globalConfigService.getById(1) ?: ShiftLeaderConfig()


        val machineTabletAddress = findViewById<EditText>(R.id.machine_tablet_address)
        val wifiDefaultPassword = findViewById<EditText>(R.id.wifi_password)
        val cloudAddress = findViewById<EditText>(R.id.cloud_address)

        machineTabletAddress.text.clear()
        machineTabletAddress.text.append(globalConfig.machineTabletAddress ?: "")

        wifiDefaultPassword.text.clear()
        wifiDefaultPassword.text.append(globalConfig.wifiDefaultPassword ?: "")

        cloudAddress.text.clear()
        cloudAddress.text.append(globalConfig.cloudAddress ?: "")

        val buttonSettingsDialog = findViewById<Button>(R.id.btn_ajust_time)
        buttonSettingsDialog.setOnClickListener {

            val settingDialog = Dialog(this, R.style.Theme_FullScreen)
            settingDialog.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )

            settingDialog.setContentView(R.layout.layout_status_setting_dialog)

            val yellowTime = settingDialog.findViewById<EditText>(R.id.yellow)
            val redTime = settingDialog.findViewById<EditText>(R.id.red)

            yellowTime.inputType = InputType.TYPE_CLASS_NUMBER
            redTime.inputType = InputType.TYPE_CLASS_NUMBER

            yellowTime.text.clear()
            //yellowTime.text.append(MachineStatusSingleton.greenStatusTimeout.toString())

            redTime.text.clear()
            //redTime.text.append(MachineStatusSingleton.yellowStatusTimeout.toString())

            //TODO converter ms para hora usando 36000000ms = 1 hora

            settingDialog.show()

            val btnSave = settingDialog.findViewById<Button>(R.id.btn_save)
            btnSave.setOnClickListener {

                val yellowInt = min(yellowTime.text.toString().toInt(), 10800 * 1000)
                val redInt = min(redTime.text.toString().toInt(), 18000 * 1000)

                //MachineStatusSingleton.greenStatusTimeout = yellowInt
                //MachineStatusSingleton.yellowStatusTimeout = redInt

                globalConfig.limitYellow = yellowInt
                globalConfig.limitRed = redInt

                //TODO nao sei se esta funcionando corretamente
                globalConfigService.save(globalConfig)

                settingDialog.hide()
            }

            val btnBack = settingDialog.findViewById<Button>(R.id.btn_back)
            btnBack.setOnClickListener {
                settingDialog.hide()
            }

        }

        val buttonSettingsSave = findViewById<Button>(R.id.btn_save)
        buttonSettingsSave.setOnClickListener {

            globalConfig.machineTabletAddress = machineTabletAddress.text.toString()
            globalConfig.wifiDefaultPassword = wifiDefaultPassword.text.toString()
            globalConfig.cloudAddress = cloudAddress.text.toString()

            ConfigSingleton.cloudAddress = globalConfig.cloudAddress
            ConfigSingleton.machineTabletAddress = globalConfig.machineTabletAddress
            ConfigSingleton.wifiDefaultPassword = globalConfig.wifiDefaultPassword

            globalConfigService.save(globalConfig)
        }

    }

    private fun setLocale(activity: Activity, languageCode: String?) {
        val locale = languageCode?.let { Locale(it) }
        if (locale != null) {
            Locale.setDefault(locale)
        }
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }


}

