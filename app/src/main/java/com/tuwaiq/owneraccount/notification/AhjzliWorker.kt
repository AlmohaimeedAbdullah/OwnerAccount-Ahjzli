package com.tuwaiq.owneraccount.notification

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters

class AhjzliWorker(private val context: Context, params: WorkerParameters) : Worker(context, params) {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun doWork(): Result {
        NotificationHelper(context).createNotification(
            inputData.getString("title").toString(),
            inputData.getString("message").toString()
        )
        Log.d("doWork2", "doWork:Success")
        return Result.success()
    }
}