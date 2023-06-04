package com.febrian.sharedapps.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast

class Helper(private val context: Context) {

    fun showLog(message: String) {
        Log.d("AppsUp", message)
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun moveActivity(activity: Activity, targetActivity: Activity) {
        val intent = Intent(activity, targetActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun moveActivityWithFinish(activity: Activity, targetActivity: Activity) {
        val intent = Intent(context, targetActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        activity.finish()
    }

    fun showLoading(isLoading: Boolean, view: View) {
        if (isLoading) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}
