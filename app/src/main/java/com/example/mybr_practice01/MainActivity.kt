package com.example.mybr_practice01

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.mybr_practice01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var broadcastReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
        initPermission()
        checkSettingOverlayWindow(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkSettingOverlayWindow(intent)
    }
//    fun getMessage(intent: Intent?) {
////        if(intent!=null){
////            if(intent.hasExtra("msgSender") and intent.hasExtra("msgBody")){
////                val msgSender = intent.getStringExtra("msgSender")
////                val msgBody = intent.getStringExtra("msgBody")
////                Toast.makeText(this,
////                    "보낸번호: "+msgSender+"\n"+msgBody,Toast.LENGTH_SHORT).show()
////            }
////        }
////    }
    fun getMessage(intent: Intent?) {
        if (intent != null) {
            if (intent.hasExtra("msgSender") && intent.hasExtra("msgBody")) {
                val msgSender = intent.getStringExtra("msgSender")
                val msgBody = intent.getStringExtra("msgBody")

                val numberRegex = Regex("[A-Z]+") // 정규표현식을 사용하여 숫자를 추출
                val matchResult = numberRegex.find(msgBody!!) // 첫 번째로 매칭되는 숫자 찾기
                val number = matchResult?.value // 매칭된 숫자 추출

                if (number != null) {
                    Toast.makeText(this, "추출된 숫자: $number", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "추출된 숫자가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    /*
    ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$
    ^(https?|ftp)://[^\s/$.?#].[^\s]*$
    ^\d{2,3}-\d{3,4}-\d{4}$
    ^[-+]?\d*\.?\d+$
    ^[A-Z]+$
    ^[a-z]+$
    ^[a-zA-Z]+$
     */

    private fun initPermission() {
        when{
            (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.RECEIVE_SMS)==PackageManager.PERMISSION_GRANTED)->{
                    Toast.makeText(this,"문자 수신 동의함", Toast.LENGTH_SHORT).show()
                }
            ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.RECEIVE_SMS)->{
                permissionAlertDlg()
            }
            else->{
                requestPermissionLauncher.launch(android.Manifest.permission.RECEIVE_SMS)
            }

        }
    }
    val requestPermissionLauncher=
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                initPermission()
            }else{
                Toast.makeText(this,"권한승인이 거부되었습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    fun checkSettingOverlayWindow(intent: Intent?){
        if(Settings.canDrawOverlays(this)){
            getMessage(intent)
        }
        else
        {
            val overlayIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            requestSettingLauncher.launch(overlayIntent)
        }

    }
    val requestSettingLauncher=
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(Settings.canDrawOverlays(this)){
                getMessage(this.intent)
            }else{
                Toast.makeText(this,"권한승인이 거부되었습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    private fun permissionAlertDlg() {
        val builder=AlertDialog.Builder(this)
        builder.setMessage("반드시 문자 수신 권한이 허용되어야 합니다.") //강의시간 5분 54초
            .setTitle("권한 체크")
            .setPositiveButton("OK"){
                _,_->
                requestPermissionLauncher.launch(android.Manifest.permission.RECEIVE_SMS)
                }
            .setNegativeButton("Cancel"){
                dlg,_->dlg.dismiss()}
                val dlg=builder.create()
                dlg.show()
    }




    private fun initLayout() {
        broadcastReceiver = object:BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent!=null)
                    if(intent.action.equals(Intent.ACTION_POWER_CONNECTED)){
                        Toast.makeText(context,"충전 시작",Toast.LENGTH_SHORT).show()

                    }else if(intent.action.equals(Intent.ACTION_POWER_DISCONNECTED)){
                        Toast.makeText(context,"충전 해제",Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(Intent.ACTION_POWER_CONNECTED)
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        registerReceiver(broadcastReceiver,intentFilter)

    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
    }
}