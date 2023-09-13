package com.couponusers.couponuser.Utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.O_MR1)
fun setFullScreen(context: Context) {
    setWindowFlag(context as Activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
    context.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    setWindowFlag(context, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
    context.window.statusBarColor = Color.TRANSPARENT
    setWindowFlag(context, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false)
}

fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
    val win = activity.window
    val winParams = win.attributes
    if (on) {
        winParams.flags = winParams.flags or bits
    } else {
        winParams.flags = winParams.flags and bits.inv()
    }
    win.attributes = winParams
}

@RequiresApi(Build.VERSION_CODES.O_MR1)
fun stausbarColor(context: Context){
    setWindowFlag(context as Activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
  //  context.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    setWindowFlag(context, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
    context.window.statusBarColor = Color.WHITE
    setWindowFlag(context, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false)

}