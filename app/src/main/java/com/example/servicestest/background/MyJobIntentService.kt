package com.example.servicestest.background

import android.app.IntentService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.JobIntentService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

// комбинация из интент сервиса и jobService .
// под капотом: если версия апи меньше 26 используется интент сервис
// ,если выше то jobService. по сути сделали тоже самое сделали в кнопке
// jobSScheduler, только тут это делается без нашего участия

private  const val TAG = "job_intent_service_tag"

class MyJobIntentService : JobIntentService() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }


    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    override fun onHandleWork(intent: Intent) {
        log("onHandleIntent")
        val page = intent.getIntExtra(PAGE,0)
        repeat(5){ count ->
            Thread.sleep(1000)
            log("Timer $page $count")
        }
    }


    private fun log(message:String){
        Log.d(TAG,"MyIntentService: $message")
    }

    companion object{
        private const val PAGE = "page"
        private const val SERVICE_ID = 111

        fun enqueue(context: Context, page:Int) {
            enqueueWork(context,MyJobIntentService::class.java, SERVICE_ID, newIntent(context, page))
        }

       private fun newIntent(context: Context, page:Int) = Intent(context, MyIntentService::class.java).apply {
            putExtra(PAGE,page)
        }

    }
}