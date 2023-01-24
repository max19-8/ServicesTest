package com.example.servicestest

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.servicestest.background.*
import com.example.servicestest.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private val binding by  lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.serviceButton.setOnClickListener {
            startService(MyService.newIntent(this))
        }
        binding.foregroundServiceButton.setOnClickListener {
            ContextCompat.startForegroundService(this,MyForegroundService.newIntent(this))
        }
        binding.intentServiceButton.setOnClickListener {
            startService(MyIntentService.newIntent(this))
        }
        binding.jobSchedulerButton.setOnClickListener {
            val serviceComponent = ComponentName(this, MyJobService::class.java)

            val jobInfo = JobInfo.Builder(MyJobService.JOB_ID, serviceComponent)
              //  .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
               // .setPersisted(true)
                .build()
            val jobScheduler: JobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent  = MyJobService.newIntent(page++, this)
                jobScheduler.enqueue(jobInfo, JobWorkItem(intent))
            }else{
                val intent  = MyIntentService2.newIntent(this,page++)
                 startService(intent)
            }

            //schedule - каждый вновь запущенный сервис отменяет предыдущий
            //enqueue - каждый вновь запущенный сервис становится в очередь и выполняется после завершения предыдущего
        }
        binding.jobIntentServiceButton.setOnClickListener {
            MyJobIntentService.enqueue(this,page++)
        }
        binding.workManagerButton.setOnClickListener {
            WorkManager.getInstance(applicationContext).enqueueUniqueWork(
                MyWorker.WORK_NAME,
                ExistingWorkPolicy.APPEND,
                MyWorker.makeRequest(page++)
            )
        }
        binding.alarmManagerButton.setOnClickListener {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.SECOND,30)
            val intent = AlarmReceiver.newIntent(this)
            val pendingIntent = PendingIntent.getBroadcast(this,100,intent,0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
        }
    }
}