package br.com.crearesistemas.shift_leader.dto

class AuthResponseDto {

    var id: Long? = null
    var username: String? = null
    var token: String? = null
    var accessLevel: String? = null

    constructor(token: String?) {
        this.token = token
    }

    constructor(
        id: Long?,
        username: String?,
        token: String?,
        accessLevel: String?
    ) {
        this.id = id
        this.username = username
        this.token = token
        this.accessLevel = accessLevel
    }

}