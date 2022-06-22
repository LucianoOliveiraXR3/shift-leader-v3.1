package br.com.crearesistemas.shift_leader.singleton

object MessageSingleton {
    var messages = mutableListOf<String>()
    var finished :Boolean = false
}