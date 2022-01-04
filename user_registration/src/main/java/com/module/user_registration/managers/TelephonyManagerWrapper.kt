package com.module.user_registration.managers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager

object TelephonyManagerWrapper {

    private val telephonyCallback: TelephonyCallback? = null

    fun makeCall(
        context: Context,
        phoneNumber: String,
        listener: ((state: Int) -> Boolean)? = null
    ) {
        val telephoneSchema = "tel:"
        val phoneCallUri = Uri.parse(telephoneSchema + phoneNumber)

        val phoneCallIntent = Intent(Intent.ACTION_CALL).also {
            it.data = phoneCallUri
        }

        // Pass the Intent to System to start any <Activity> which can accept `ACTION_CALL`
        context.startActivity(phoneCallIntent)

        listener?.let {
            createSingleOutgoingCallListener(context, it)
        }

    }

    fun createSingleOutgoingCallListener(
        context: Context,
        onStatusChange: (state: Int) -> Boolean
    ) {
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val listener = object : TelephonyCallback(), TelephonyCallback.CallStateListener {
                override fun onCallStateChanged(state: Int) {
                    if (onStatusChange.invoke(state)) {
                        unregisterTelephonyCallback(context)
                    }
                }
            }
            manager.registerTelephonyCallback(context.mainExecutor, listener)

        } else {
            val listener = object : PhoneStateListener() {
                override fun onCallStateChanged(state: Int, incomingNumber: String) {
                    super.onCallStateChanged(state, incomingNumber)
                    onStatusChange.invoke(state)
                }
            }
            manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)
        }
    }

    fun unregisterTelephonyCallback(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        telephonyCallback?.let {
            manager.unregisterTelephonyCallback(it)
        }
    }

    fun callEnded(prevState: Int, state: Int): Boolean {
        return prevState == TelephonyManager.CALL_STATE_OFFHOOK && state == TelephonyManager.CALL_STATE_IDLE
    }

    fun callRejected(prevState: Int, state: Int): Boolean {
        return prevState == TelephonyManager.CALL_STATE_RINGING && state == TelephonyManager.CALL_STATE_IDLE
    }
}