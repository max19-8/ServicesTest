package com.example.servicestest.background

import android.content.Context
import android.util.Log
import androidx.work.*

private  const val TAG = "my_worker"

class MyWorker(context: Context, private val workerParameters: WorkerParameters) : Worker(context,workerParameters) {


    override fun doWork(): Result {
        log("onHandleIntent")
        val page = workerParameters.inputData.getInt(PAGE,0)
        repeat(5){ count ->
            Thread.sleep(1000)
            log("Timer $page $count")
        }
        return  Result.success()
    }


    private fun log(message:String){
        Log.d(TAG,"MyWorker: $message")
    }

    companion object{

        private const val PAGE = "page"

         const val WORK_NAME = "work_name"

        fun makeRequest( page:Int) =   OneTimeWorkRequestBuilder<MyWorker>()
            .setConstraints(createConstrains())
            .setInputData(workDataOf(PAGE to page))
            .build()

        private fun createConstrains() = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

    }


}