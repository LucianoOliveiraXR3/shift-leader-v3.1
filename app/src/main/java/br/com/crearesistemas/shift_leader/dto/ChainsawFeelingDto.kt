package br.com.crearesistemas.shift_leader.dto

import com.google.gson.Gson

class ChainsawFeelingDto {

    var location: String = ""
    var areaSize: Float = 0.0f
    var quantityOfChainsawMen: Int = 0
    var workingHours: Float = 0.0f
    var gang: String? = null

    fun toJson(): String {
        return Gson().toJson(this).toString()
    }

}
