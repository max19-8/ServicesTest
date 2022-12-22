package com.example.servicestest.background

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

private  const val TAG = "intent_service2_tag"

class MyIntentService2 : IntentService(SERVICE_NAME) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        setIntentRedelivery(true)
    }

    override fun onHandleIntent(p0: Intent?) {
        log("onHandleIntent")
        val page = p0?.getIntExtra(PAGE,0)
        repeat(5){ count ->
            Thread.sleep(1000)
            log("Timer $page $count")
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
        private const val PAGE = "page"

        fun newIntent(context: Context,page:Int) = Intent(context, MyIntentService::class.java).apply {
            putExtra(PAGE,page)
        }

    }
}