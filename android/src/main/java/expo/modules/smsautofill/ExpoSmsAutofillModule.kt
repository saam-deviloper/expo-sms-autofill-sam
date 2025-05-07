package expo.modules.smsautofill

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.util.Log
import com.facebook.react.modules.core.DeviceEventManagerModule
import expo.modules.kotlin.modules.*
import expo.modules.kotlin.events.EventEmitter
import expo.modules.kotlin.Promise
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class ExpoSmsAutofillModule : Module(), ActivityProvider {
    private lateinit var context: Context
    private var smsReceiver: BroadcastReceiver? = null

    override fun definition() = ModuleDefinition {
        Name("ExpoSmsAutofill")

        Events("onSmsReceived")

        OnCreate {
            context = appContext.reactContext ?: throw Exception("No React Context")
        }

        Function("startSmsListener") {
            val client = SmsRetriever.getClient(context)
            val task = client.startSmsRetriever()

            task.addOnSuccessListener {
                Log.d("ExpoSmsAutofill", "SMS Retriever started successfully")
            }

            task.addOnFailureListener {
                Log.e("ExpoSmsAutofill", "SMS Retriever failed to start", it)
            }

            registerReceiver()
        }
    }

    private fun registerReceiver() {
        smsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
                    val extras = intent.extras
                    val status = extras?.get(SmsRetriever.EXTRA_STATUS) as Status
                    when (status.statusCode) {
                        CommonStatusCodes.SUCCESS -> {
                            val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                            Log.d("ExpoSmsAutofill", "Received SMS: $message")
                            sendEvent("onSmsReceived", mapOf("otp" to extractOtp(message)))
                        }
                        CommonStatusCodes.TIMEOUT -> {
                            Log.w("ExpoSmsAutofill", "SMS Retriever timed out")
                        }
                    }
                }
            }
        }

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        context.registerReceiver(smsReceiver, intentFilter)
    }

    private fun extractOtp(message: String): String {
        // Very basic 6-digit OTP extraction
        val regex = Regex("\\b\\d{6}\\b")
        return regex.find(message)?.value ?: ""
    }

    private fun sendEvent(eventName: String, params: Map<String, Any?>) {
        try {
            appContext
                .eventEmitter
                .emit(eventName, params)
        } catch (e: Exception) {
            Log.e("ExpoSmsAutofill", "Failed to send event: $eventName", e)
        }
    }

    override fun getCurrentActivity(): Activity? {
        return appContext.currentActivity
    }

    override fun onDestroy() {
        super.onDestroy()
        smsReceiver?.let {
            context.unregisterReceiver(it)
        }
    }
}
