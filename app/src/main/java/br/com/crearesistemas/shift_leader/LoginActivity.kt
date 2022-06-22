package br.com.crearesistemas.shift_leader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import java.util.*
import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import br.com.crearesistemas.shift_leader.db_service.model.ShiftLeaderConfig
import br.com.crearesistemas.shift_leader.db_service.model.User
import br.com.crearesistemas.shift_leader.db_service.viewmodel.MachineViewModel
import br.com.crearesistemas.shift_leader.db_service.viewmodel.ShiftLeaderConfigMachineViewModel
import br.com.crearesistemas.shift_leader.db_service.viewmodel.ShiftLeaderConfigViewModel
import br.com.crearesistemas.shift_leader.db_service.viewmodel.UserViewModel
import br.com.crearesistemas.shift_leader.singleton.ConfigSingleton
import br.com.crearesistemas.shift_leader.singleton.LoginSingleton
import br.com.crearesistemas.socket.client.service.MachineStatusSingleton
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 */
class LoginActivity : AppCompatActivity()
{

    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        if (intent.extras?.getString("message") == "cloud_mode") {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("cloud_mode", true)
            startActivity(intent)
        }

        setContentView(R.layout.activity_login)
        bindEvents()

        val versionapp = findViewById<TextView>(R.id.versionApp)
        versionapp.text = "v"+ BuildConfig.VERSION_NAME+"-Beta-QA"

        try {

            val machineService = MachineViewModel(application)
            val allMachines = machineService.getAll()
            allMachines.forEach {
                val machineConfigService = ShiftLeaderConfigMachineViewModel(application)
                val machineConfig = machineConfigService.getBySsid(it.hotspotSsid!!)

                if (machineConfig != null) {
//                    var timestamp = sdf.parse("01/01/2021")?.time
//
//                    if (machineConfig.lastSyncTimestamp != null && machineConfig.lastSyncTimestamp != "null") {
//                        timestamp = machineConfig.lastSyncTimestamp.toString().toLong()
//                    }

//                    MachineStatusSingleton.lastReadTimestamp[it.hotspotSsid!!] = timestamp!!.toLong()
//                    MachineStatusSingleton.activationStatus[it.hotspotSsid!!] = machineConfig.isConnected!!
//                    MachineStatusSingleton.isConnectingStatus[it.hotspotSsid!!] = machineConfig.isConnected!!
                }
            }

            val globalConfigService = ShiftLeaderConfigViewModel(application)

            val globalConfig = globalConfigService.getById(1)
            if (globalConfig?.lastSyncTime != null) {

//                MachineStatusSingleton.lastSyncCloudTimestamp = globalConfig.lastSyncTime.toString().toLong()
//                MachineStatusSingleton.greenStatusTimeout = globalConfig.limitYellow ?: 10800 * 1000
//                MachineStatusSingleton.lastSyncCloudTimestamp = (globalConfig.limitRed ?: 18000 * 1000).toLong()

                ConfigSingleton.cloudAddress = globalConfig.cloudAddress
                ConfigSingleton.machineTabletAddress = globalConfig.machineTabletAddress
                ConfigSingleton.wifiDefaultPassword = globalConfig.wifiDefaultPassword
            } else {

                val config = ShiftLeaderConfig()
                config.id = 1
                config.limitRed = 18000 * 1000
                config.limitYellow = 10800 * 1000
                globalConfigService.save(config)
            }

        } 
	catch (e: Exception) {
            e.printStackTrace()
        }


        val b = intent.extras
        var value = ""
        if (b != null) {
            value = b.get("message").toString()
        }
        if (value == "connect") {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
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

    private fun bindEvents() {

        val userNameTtxt = findViewById<EditText>(R.id.user_name)
        val userPwdTtxt = findViewById<EditText>(R.id.user_password)

        // bind de botao login
        findViewById<Button>( R.id.btn_login ).setOnClickListener {

            //startActivity(Intent(this, MainActivity::class.java ) )

            val userInput = User()
            userInput.username = userNameTtxt.text.toString()
            userInput.password = userPwdTtxt.text.toString()

            when {
                checkLogin(userInput) -> {
                    LoginSingleton.loginAttempts = 0
                    LoginSingleton.loginFailTimestamp = 0
                    LoginSingleton.isAdmin = false
                    LoginSingleton.statusLogin = true

                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java
                        )
                    )
                }
                else -> {
                    LoginSingleton.isAdmin = false
                    LoginSingleton.statusLogin = false
                    LoginSingleton.loginAttempts++
                    LoginSingleton.loginFailTimestamp = Date().time
                    Toast.makeText(
                        this,
                        "Invalid login.", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        val buttonEn = findViewById<ImageButton>(R.id.lang_en)
        buttonEn.setOnClickListener {
            setLocale(this, "")
            resources.getString(R.string.msg_change_login)
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )
        }

        val buttonIn = findViewById<ImageButton>(R.id.lang_in)
        buttonIn.setOnClickListener {
            setLocale(this, "in")
            Toast.makeText(this, resources.getString(R.string.msg_change_login), Toast.LENGTH_SHORT)
                .show()
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )
        }
    }

    private fun checkLogin(userInputData: User): Boolean {
        // Super admin temporario
        if (userInputData.username == "admuser" && userInputData.password == "inpm7099") return true;
        if (userInputData.username == "creare"  && userInputData.password == "Creare5544") return true;

        val userService = UserViewModel(application)
        val loginFound = userService.login(userInputData.username!!, userInputData.password!!)
        return loginFound != null
    }

}
