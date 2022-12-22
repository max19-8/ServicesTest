package com.example.servicestest.background

import android.app.IntentService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

private  const val TAG = "intent_service_tag"

class MyIntentService : IntentService(SERVICE_NAME) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onHandleIntent(p0: Intent?) {
        log("onHandleIntent")
        repeat(5){ count ->
            Thread.sleep(1000)
            log("Timer $count")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }


    private fun log(message:String){
        Log.d(TAG,"MyIntentService: $message")
    }

    companion object{
        private const val SERVICE_NAME = "MyIntentService"
        fun newIntent(context: Context) = Intent(context, MyIntentService::class.java)

    }
}