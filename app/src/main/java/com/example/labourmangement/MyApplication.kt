package com.example.labourmangement

import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.text.TextUtils
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class MyApplication : MultiDexApplication {
    private var mRequestQueue: RequestQueue? = null
    var preferences: MyApplication? = null
        private set

    /**
     * @return RequestQueue object for adding http requests
     */

    override fun attachBaseContext(base: Context?): Unit {
        super.attachBaseContext(base)
        MultiDex.install(this)
        //FacebookSdk.sdkInitialize(applicationContext)
    }
    private val requestQueue: RequestQueue
        get() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(applicationContext)
            }

            return this.mRequestQueue!!
        }

    constructor() {}

    constructor(mApp: MyApplication) {}


    override fun onCreate() {
        super.onCreate()
        application = this
        preferences = MyApplication(application!!)

        val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    /*  public static SchoolAppUtil getUtil() {
        return SchoolAppUtil.getInstance(mApp);
    }*/


    fun <T> addToRequestQueue(req: Request<T>, tag: String) {
        // set the default tag if tag is empty
        req.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue.add(req)
    }

    companion object {
        var application: MyApplication? = null
            private set
        private val TAG = "SchoolApplication"
    }

    fun showProgressDialog(context: Context, message: String): ProgressDialog {
        val dialog = ProgressDialog(context)
        dialog.setMessage(message)
        dialog.setCancelable(false)
        dialog.show()
        return dialog
    }


    fun hideProgressDialog(dialog: ProgressDialog?) {
        try {
            if (dialog != null && dialog.isShowing) {
                dialog.dismiss()
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }
}