package com.skapps.YksStudyApp.view.Pomodoro
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.skapps.YksStudyApp.R
import com.skapps.YksStudyApp.Service.PomodoroService
import com.skapps.YksStudyApp.databinding.ActivityPomodoroBinding
import com.skapps.YksStudyApp.view.Pomodoro.AddPomodoroDialog.AddPomodoroFragment
import com.skapps.YksStudyApp.view.MainActivity

class PomodoroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPomodoroBinding

    private var isStopwatchRunning = false
    private var  addPomodroTime:Int?=null
    private lateinit var statusReceiver: BroadcastReceiver
    private lateinit var timeReceiver: BroadcastReceiver
    private var logactivitiy:String?=null
    private var logtime:Int?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        binding = ActivityPomodoroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHosFragment=supportFragmentManager.findFragmentById(R.id.navhostPomodoro) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomBar,navHosFragment.navController)
        val bottomNavigationViewation : BottomNavigationView = findViewById(R.id.bottomBar)
        val radius = resources.getDimension(R.dimen.my_value)

        val shapeDrawable : MaterialShapeDrawable = bottomNavigationViewation.background as MaterialShapeDrawable
        shapeDrawable.shapeAppearanceModel = shapeDrawable.shapeAppearanceModel
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, radius)
            .build()
        binding.toggleButton.setOnClickListener {
            if (isStopwatchRunning) pauseStopwatch() else startStopwatch()
        }

        binding.addPomodoro.setOnClickListener {
            resetStopwatch()
            showDialogOne()
        }
        binding.resetImageView.setOnClickListener {
            resetStopwatch()
        }
        binding.backPomodoro.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


    }
    override fun onStart() {
        super.onStart()
         addPomodroTime=intent.getIntExtra("addpomodoro",25)
         logactivitiy= intent.getStringExtra("activity")
         logtime=addPomodroTime
        Log.e("Pomodoro Activity", "value: ${addPomodroTime}")
        if (addPomodroTime!=25){
            startStopwatch()
        }
        // Moving the service to background when the app is visible
        moveToBackground()
    }

    override fun onResume() {
        super.onResume()
        getStopwatchStatus()
        // Receiving stopwatch status from service
        val statusFilter = IntentFilter()
        statusFilter.addAction(PomodoroService.STOPWATCH_STATUS)
        statusReceiver = object : BroadcastReceiver() {
            @SuppressLint("SetTextI18n")
            override fun onReceive(p0: Context?, p1: Intent?) {
                val isRunning = p1?.getBooleanExtra(PomodoroService.KRONOMETRE_CALISIYOR, false)!!
                isStopwatchRunning = isRunning
                val timeElapsed = p1.getIntExtra(PomodoroService.GECEN_SURE, 0)
                updateLayout(isStopwatchRunning)
                updateStopwatchValue(timeElapsed)
            }
        }
        registerReceiver(statusReceiver, statusFilter)
        // Receiving time values from service
        val timeFilter = IntentFilter()
        timeFilter.addAction(PomodoroService.STOPWATCH_TICK)
        timeReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val timeElapsed = p1?.getIntExtra(PomodoroService.GECEN_SURE, 0)!!
                updateStopwatchValue(timeElapsed)
            }
        }
        registerReceiver(timeReceiver, timeFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(statusReceiver)
        unregisterReceiver(timeReceiver)
        // Moving the service to foreground when the app is in background / not visible
        moveToForeground()
    }


    @SuppressLint("SetTextI18n")
    private fun updateStopwatchValue(timeElapsed: Int) {
     //   val minutes: Int = timeElapsed / 60
     //   val seconds: Int = timeElapsed % 60
        val seconds = (timeElapsed/ 1000) % 60
        val minutes = (timeElapsed / (1000 * 60) % 60)
        //  binding!!.choronometre.text = "$minutes : $seconds"
        binding.stopwatchValueTextView.text = "${"%02d".format(minutes)}:${"%02d".format(seconds)}"
        //binding.circularProgressView.setProgress(timeElapsed.toFloat()/100*minutes, true),


    }
    private fun updateProgress(totalTime:Float,liveTime:Float){
        val secondRemaining =(liveTime/1000)
        val second=totalTime/1000
        val tick = 100/second
        val progress= (totalTime-secondRemaining)*tick
        binding.circularProgressView.setProgress(progress)
    }

    private fun updateLayout(isStopwatchRunning: Boolean) {
        if (isStopwatchRunning) {
            binding.toggleButton.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_pause)
          binding.resetImageView.visibility = View.INVISIBLE
        } else {
            binding.toggleButton.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_play)
            binding.resetImageView.visibility = View.VISIBLE
        }

    }

    private fun getStopwatchStatus() {
        val pomodoroService = Intent(this, PomodoroService::class.java)
        pomodoroService.putExtra(PomodoroService.IZLEME_DURDUR , PomodoroService.DURUM_AL)
        startService(pomodoroService)
    }

    private fun startStopwatch() {
        val pomodoroService = Intent(this, PomodoroService::class.java)
        pomodoroService.putExtra(PomodoroService.IZLEME_DURDUR , PomodoroService.START)
        pomodoroService.putExtra("custompomodoro" , addPomodroTime)
        if (logactivitiy!=null){
            pomodoroService.putExtra("logactivity",logactivitiy)
        }else{
            pomodoroService.putExtra("logactivity","Diğer")
        }
       // totalProgress=addPomodroTime!!*1000*60F
        startService(pomodoroService)
    }

    private fun pauseStopwatch() {
        val pomodoroService = Intent(this, PomodoroService::class.java)
        pomodoroService.putExtra(PomodoroService.IZLEME_DURDUR ,PomodoroService.PAUSE)
        startService(pomodoroService)
    }

    private fun resetStopwatch() {
        val pomodoroService = Intent(this, PomodoroService::class.java)
        pomodoroService.putExtra(PomodoroService.IZLEME_DURDUR , PomodoroService.RESET)
        startService(pomodoroService)
    }

    private fun moveToForeground() {
        val pomodoroService = Intent(this, PomodoroService::class.java)
        pomodoroService.putExtra(
            PomodoroService.IZLEME_DURDUR ,
            PomodoroService.ONE_CIKAR
        )
        startService(pomodoroService)
    }

    private fun moveToBackground() {
        val pomodoroService = Intent(this, PomodoroService::class.java)
        pomodoroService.putExtra(
            PomodoroService.IZLEME_DURDUR ,
            PomodoroService.ARKA_PALANA_GEC
        )
        startService(pomodoroService)
    }

    fun showDialogOne() {
        val addPomodoro = AddPomodoroFragment()
       addPomodoro.show(supportFragmentManager, addPomodoro.getTag())
    }


}