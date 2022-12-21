package com.example.servicestest.background
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

private  const val SERVICE_TAG = "service_tag"

class MyService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        TODO()
    }


    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        coroutineScope.launch {
            repeat(100){ count ->
                delay(1000)
                log("Timer $count")
            }
        }
        return super.onStartCommand(intent, flags, startId)

    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")

    }


    private fun log(message:String){
        Log.d(SERVICE_TAG,message)
    }

    companion object{
        fun newIntent(context: Context) = Intent(context, MyService::class.java)
    }
}