package com.example.servicestest.background

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import kotlinx.coroutines.*
private  const val TAG = "job_service_tag"

class MyJobService: JobService() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            log("onStartJob")
            coroutineScope.launch {
                var workItem = params?.dequeueWork()
                while (workItem !=null){
                    val page = workItem.intent?.getIntExtra(PAGE_INFO,0)
                    repeat(5) { count ->
                        delay(1000)
                        log("Timer $page $count")
                    }
                    params?.completeWork(workItem)
                    workItem = params?.dequeueWork()
                }
                jobFinished(
                    params,
                    false
                ) //  если после нормального завершения работы сервиса надо его перезапустить через какое-то время то true, иначе false
            }
        }
        return true
    // возвращаем true  если код асинхронный , иначе false.
    // если по окончанию работы метода  onStartJob  сервис еще выполняется тогда true , если закончил выполнятся то false
        // в таком случае сервис надо останавливть вручную , при помощи jobFinished
    }

    // вызывается только если система  убила наш сервис
    override fun onStopJob(params: JobParameters?): Boolean {
        log("onStopJob")
        return true
        // если после того как система убила сервис его надо перезапустить true  иначе false
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d(TAG, "MyJobService: $message")
    }

    companion object{
        const val  JOB_ID  = 111

      private  const val PAGE_INFO = "page_info"

        fun newIntent(page:Int,context: Context):Intent = Intent(context,MyJobService::class.java).apply {
            putExtra(PAGE_INFO,page)
        }
    }
}