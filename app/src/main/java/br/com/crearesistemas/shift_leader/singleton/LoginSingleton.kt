package br.com.crearesistemas.shift_leader.singleton

object LoginSingleton {
    var statusLogin: Boolean = false
    var isAdmin: Boolean = false
    var loginAttempts: Int = 0
    var loginFailTimestamp: Long = 0L
}

