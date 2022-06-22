package br.com.crearesistemas.socket.client.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 *      CREARE SISTEMAS 2021 - ORBCOMM COMMUNICATOR SERVICE
 *
 *      @author: Luiz Gregory
 *      @email: luiz.gregory@crearesistemas.com.br
 *
 */
class ClientSocketService : Service()
{

    /**
     *
     */
    private lateinit var socketClient : ClientSocketController

    /**
     *
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        this.socketClient = ClientSocketController( application )
        this.socketClient.start()
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     *
     */
    override fun onBind(p0: Intent?): IBinder?
    {
        TODO("Not yet implemented")
    }

} // FINAL CLASS

