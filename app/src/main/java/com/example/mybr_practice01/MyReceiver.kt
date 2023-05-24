package com.example.mybr_practice01

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            val msg = Telephony.Sms.Intents.getMessagesFromIntent(intent)
//            for (smsMessage in msg)
//                Log.i("msg",smsMessage.displayMessageBody)
//        }
            val newIntent = Intent(context, MainActivity::class.java)
            newIntent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            newIntent.putExtra("msgSender", msg[0].originatingAddress)
            newIntent.putExtra("msgBody", msg[0].messageBody)
            context.startActivity(newIntent)

        }
    }
}
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.provider.Telephony
//import android.util.Log
//import java.util.regex.Pattern
//
//class MyReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
//        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
//            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
//            for (message in messages) {
//                val sender = message.originatingAddress
//                val body = message.messageBody
//
//                // Parse the SMS body using regular expressions
//                val pattern = Pattern.compile("\\d+")
//                val matcher = pattern.matcher(body)
//
//                if (matcher.find()) {
//                    // Extract the desired information using group() method
//                    val parsedInfo = matcher.group(1)
//
//                    // Perform further actions with the parsed information
//                    Log.d("MyReceiver", "Parsed Info: $parsedInfo")
//                }
//            }
//        }
//    }
//}
