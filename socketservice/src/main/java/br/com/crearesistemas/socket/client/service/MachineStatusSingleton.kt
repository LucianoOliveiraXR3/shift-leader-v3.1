package br.com.crearesistemas.socket.client.service

import java.util.*
import kotlin.collections.LinkedHashMap

object MachineStatusSingleton {

//    var greenStatusTimeout = 10800 * 1000 // de 0 ate 3 horas
//    var yellowStatusTimeout = 18000 * 1000 // de 3hs ate 5 horas
//    val redStatusTimeout = -1 // acima de 5hs ate infinito
//    var colorStatus = LinkedHashMap<String, Int>() // 1=Verde, 2= amarelo, 3=vermelho
//    var selecToggleName = ""
//    var lastReadTimestamp = LinkedHashMap<String, Long>()
//
//    var activationStatus = LinkedHashMap<String, Boolean>()
//
//    var isConnectingStatus = LinkedHashMap<String, Boolean>()
//    var passwords = LinkedHashMap<String, String>()
//    var lastSyncCloudTimestamp : Long? = null
    var lastSyncCloudDate : Date? = null
//
//    var currentMachineName : String? = null
//
//    var messages = mutableListOf<String>()
    var messagesSocket = mutableListOf<String>()
 //   var syncFinished :Boolean = false
}