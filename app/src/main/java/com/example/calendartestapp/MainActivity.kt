package com.example.calendartestapp

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.calendartestapp.alertDialog.DeleteRecipeDialog
import com.example.calendartestapp.notification.Receiver
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Float.max
import javax.annotation.Resource


@AndroidEntryPoint
class MainActivity :
    AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener,
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // activity_toolbar_sample.xml からToolbar要素を取得
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        // アクションバーにツールバーをセット
        setSupportActionBar(toolbar)
        //ツールバーに戻るボタンを設置

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.main_nav_host)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.calendarRecipeFragment,
                R.id.calendarRecipeTodayFragment,
                R.id.noDataFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val nf = prefs.getBoolean("notificationFlag", false)
        notification(nf)
        prefs.registerOnSharedPreferenceChangeListener(this)

    }

    //ToolBarのメニューを初期化するためのメソッド
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.setting_menu, menu)
        menu.apply {
            this?.getItem(0)?.icon?.setTint(getColor(R.color.brown_200))
            this?.findItem(android.R.id.home)?.icon?.setTint(getColor(R.color.brown_200))
        }
        return super.onCreateOptionsMenu(menu)
    }

    //ToolBarのメニューのクリックイベントを初期化するためのメソッド
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
                // 設定用の Fragment を表示
                findNavController(R.id.main_nav_host).navigate(R.id.action_global_preferenceFragment)
                Log.d("aiueo", "preferenceに遷移します")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //通知
    private fun notification(notificationFlag: Boolean) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, Receiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        if(notificationFlag) {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                //set(Calendar.HOUR_OF_DAY, 15)
                //set(Calendar.MINUTE, 25)
                add(Calendar.SECOND, 10)
            }
//            alarmManager.setRepeating( //Required minSdkVersion >= 19　・・・※１
//                AlarmManager.RTC,
//                calendar.timeInMillis,
//                60000,
//                pendingIntent
//            )
        alarmManager.setExact(
            AlarmManager.RTC,
            calendar.timeInMillis,
            pendingIntent

        )
        } else {
            alarmManager.cancel(pendingIntent)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        findNavController(R.id.main_nav_host).popBackStack()
        Log.d("aiueo", "popBackします")
        return super.onSupportNavigateUp()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if(key == "notificationFlag") {
            val value = sharedPreferences!!.getBoolean(key, false)
            notification(value)
        }
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat?,
        pref: Preference?
    ): Boolean {
        val fragment = supportFragmentManager.fragmentFactory.instantiate(classLoader, pref!!.fragment).apply {
            //arguments = pref.extras
        }
        val dialog = DeleteRecipeDialog.Builder(fragment)
        dialog
            .build()
            .show(supportFragmentManager,"editRecipeDialog")
        //overridePendingTransition(R.anim.open_enter, R.anim.open_exit)

        Log.d("aiueo", "deleteDialogを表示します")

        return true
    }
}
