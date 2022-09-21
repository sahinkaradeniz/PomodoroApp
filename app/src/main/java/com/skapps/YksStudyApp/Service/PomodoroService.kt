package com.skapps.YksStudyApp.Service

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.skapps.YksStudyApp.Model.DateClass
import com.skapps.YksStudyApp.Model.LogPomodoro
import com.skapps.YksStudyApp.R
import com.skapps.YksStudyApp.Statistics.Pomodoro.RatingPomodoro
import com.skapps.YksStudyApp.database.LocalDatabase
import com.skapps.YksStudyApp.database.LogPomDatabase
import com.skapps.YksStudyApp.database.PomodoroDatabase
import com.skapps.YksStudyApp.view.Pomodoro.PomodoroActivity
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.math.log

class PomodoroService : Service() {
    companion object {
        // Channel ID for notifications
        const val CHANNEL_ID = "Stopwatch_Notifications"

        // Service Actions
        const val START = "START"
        const val PAUSE = "PAUSE"
        const val RESET = "RESET"
        const val DURUM_AL = "GET_STATUS"
        const val ONE_CIKAR = "MOVE_TO_FOREGROUND"
        const val ARKA_PALANA_GEC = "MOVE_TO_BACKGROUND"

        // Intent Extras
        const val IZLEME_DURDUR = "STOPWATCH_ACTION"
        const val GECEN_SURE = "TIME_ELAPSED"
        const val KRONOMETRE_CALISIYOR = "IS_STOPWATCH_RUNNING"

        // Intent Actions
        const val STOPWATCH_TICK = "STOPWATCH_TICK"
        const val STOPWATCH_STATUS = "STOPWATCH_STATUS"
    }

    private var timeElapsed: Int = 25*60*1000
    private var isStopWatchRunning = false
    private var isStartControl=true
    var cTimer: CountDownTimer? = null
    private var updateTimer = Timer()
    private var timePause:Long=timeElapsed.toLong()
    private var date:DateClass?=null
    val calender = Calendar.getInstance()
    @OptIn(DelicateCoroutinesApi::class)
    private val rating= RatingPomodoro()
    private var logPomodoro= LogPomodoro(activity = "Diğer",time = 25,calender.get(Calendar.YEAR).toString(),
        SimpleDateFormat("MM",Locale.getDefault()).format(Date()).toString(),calender.get(Calendar.DAY_OF_MONTH).toString(),calender.get(Calendar.WEEK_OF_YEAR).toString(),SimpleDateFormat("HH",Locale.getDefault()).format(Date()).toString(),SimpleDateFormat("mm",Locale.getDefault()).format(Date()).toString())

    // Getting access to the NotificationManager
    private lateinit var notificationManager: NotificationManager

    /**
     * Sistem, yalnızca ilk istemci bağlandığında IBinder'ı almak için onBind() yöntemini çağırır.
     * Sistem daha sonra aynı IBinder'ı herhangi bir ek istemciye iletir.
     * onBind()'i tekrar çağırmadan.
     * */
    override fun onBind(p0: Intent?): IBinder? {

        Log.d("Stopwatch", "Stopwatch onBind")
        return null
    }

    /**
     * onStartCommand(), bir istemci hizmeti her başlattığında çağrılır
     * startService kullanarak (Niyet amacı)
     * Bu hizmetin hangi eylem için çağrıldığını kontrol edeceğiz ve ardından
     * buna göre hareket edin. Eylem, başlatmak için kullanılan niyetten çıkarılır.
     * bu servis.
     * */

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createChannel()
        getNotificationManager()
        val action = intent?.getStringExtra(IZLEME_DURDUR)!!
        Log.d("Stopwatch", "onStartCommand Action: $action")
        when (action) {
            START ->{
                if (isStartControl){
                    val custompomodoro =intent.getIntExtra("custompomodoro",31)
                    logPomodoro.activity= intent.getStringExtra("logactivity")
                    logPomodoro.time=custompomodoro
                    Log.e("Stopwatch", "test: ${custompomodoro}")
                    Log.e("Stopwatch", "test: ${logPomodoro.activity}")
                    timePause=custompomodoro*1000*60L
                    timeElapsed=timePause.toInt()
                    startStopwatch()
                    isStartControl=false
                }else{
                    startStopwatch()
                }
            }
            PAUSE -> pauseStopwatch()
            RESET -> resetStopwatch()
            DURUM_AL -> sendStatus()
            ONE_CIKAR -> moveToForeground()
            ARKA_PALANA_GEC -> moveToBackground()
        }

        return START_STICKY
    }

    /*
     * Bu işlev, uygulama artık kullanıcı tarafından görülmediğinde tetiklenir
     * Kronometrenin çalışıp çalışmadığını kontrol eder, çalışıyorsa ön plan servisini başlatır.
     * bildirim ile.
     * Bildirimi her saniye güncellemek için başka bir zamanlayıcı çalıştırıyoruz.
     * */
    private fun moveToForeground() {

        if (isStopWatchRunning) {
            startForeground(1, buildNotification())

            updateTimer = Timer()

            updateTimer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    updateNotification()

                }
            }, 0, 1000)
        }
    }

    /*
      * Bu işlev, uygulama kullanıcıya tekrar görünür olduğunda tetiklenir
      * Bildirimi her saniye güncelleyen zamanlayıcıyı iptal eder
      * Ayrıca ön plan hizmetini durdurur ve bildirimi kaldırır
      * */
    private fun moveToBackground() {
        updateTimer.cancel()
        stopForeground(true)
    }

    /*
     * Bu işlev kronometreyi başlatır
     * Kronometrenin çalışma durumunu true olarak ayarlar
     * Bir Zamanlayıcı başlatıyoruz ve geçen süreyi her saniye 1 artırıyoruz ve değeri yayınlıyoruz
     * STOPWATCH_TICK eylemiyle.
     * Geçen süreye erişmek için bu yayını MainActivity'de alacağız.
     * */
    @OptIn(DelicateCoroutinesApi::class)
    private fun startStopwatch() {
        isStopWatchRunning = true
        sendStatus()
       // stopwatchTimer = Timer()
      /*
        stopwatchTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val stopwatchIntent = Intent()
                stopwatchIntent.action = STOPWATCH_TICK

                timeElapsed--
                stopwatchIntent.putExtra(GECEN_SURE, timeElapsed)
                sendBroadcast(stopwatchIntent)
            }
        }, 0, 1000)
        */
        //timePause
         cTimer = object : CountDownTimer(timePause, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val stopwatchIntent = Intent()
                stopwatchIntent.action = STOPWATCH_TICK
                timePause=millisUntilFinished
                timeElapsed =millisUntilFinished.toInt()
                stopwatchIntent.putExtra(GECEN_SURE, timeElapsed)
                sendBroadcast(stopwatchIntent)
            }
             override fun onFinish() {
                 pauseStopwatch()
                 GlobalScope.launch{
               //      logPomodoro.date= DateClass(calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),calender.get(Calendar.DAY_OF_MONTH),calender.get(Calendar.WEEK_OF_YEAR),calender.get(Calendar.HOUR_OF_DAY),calender.get(Calendar.MINUTE))
                     saveRoom(logPomodoro)
                     Log.e("PomodoroService", "Savetest succes: ${logPomodoro.activity}")
                     rating.updateTimes(logPomodoro.time!!)
                 }
             }
         }
        (cTimer as CountDownTimer).start()
    }

    /*
     * Bu işlev kronometreyi duraklatır
     * Kronometrenin mevcut durumunun bir güncellemesini gönderir
     * */
    private suspend fun saveRoom(logPomodoro: LogPomodoro){
        Log.e("save Room",logPomodoro.activity.toString())
        val dao =LogPomDatabase(getApplication()).logPomDao()
        dao.insert(logPomodoro)
    }
    private fun pauseStopwatch() {
        // stopwatchTimer.cancel()
        cTimer?.cancel()
        isStopWatchRunning = false
        sendStatus()
    }

    /*
     * Bu işlev kronometreyi sıfırlar
     * Kronometrenin mevcut durumunun bir güncellemesini gönderir
     * */
    private fun resetStopwatch() {
        pauseStopwatch()
        timeElapsed = 0
        sendStatus()
        isStartControl=true
    }

    /*
      * Bu işlev, kronometrenin durumunu yayınlamaktan sorumludur.
      * Kronometre çalışıyorsa ve geçen süreyi de yayınlar
      * */
    private fun sendStatus() {
        val statusIntent = Intent()
        statusIntent.action = STOPWATCH_STATUS
        statusIntent.putExtra(KRONOMETRE_CALISIYOR, isStopWatchRunning)
        statusIntent.putExtra(GECEN_SURE, timeElapsed)
        sendBroadcast(statusIntent)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Stopwatch",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.setSound(null, null)
            notificationChannel.setShowBadge(true)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun getNotificationManager() {
        notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
    }

    /** * Bu işlev, Kronometrenin mevcut * durumunu ve Geçen süreyi içeren bir Bildirim oluşturmak ve geri göndermekten sorumludur * */
    private fun buildNotification(): Notification {
        val title = if (isStopWatchRunning) {
            "Pomodoro"
        } else {
            "Pomodoro Tamamlandı!"
        }
        val seconds = (timeElapsed/ 1000) % 60
        val minutes = (timeElapsed / (1000 * 60) % 60)
        //  binding!!.choronometre.text = "$minutes : $seconds"


        val intent = Intent(this, PomodoroActivity::class.java)
       //val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        var pIntent : PendingIntent? = null
        pIntent  = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_ONE_SHOT)
        }
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle( "${"%02d".format(minutes)}:${
                "%02d".format(
                    seconds
                )
            }")
            .setOngoing(true)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setColorized(true)
            .setSubText(title)
            .setColor(Color.parseColor("#2196F3"))
            .setSmallIcon(R.drawable.ic_clock)
            .setOnlyAlertOnce(true)
            .setContentIntent(pIntent)
            .setAutoCancel(true)
            .build()
    }


    /** Bu işlev, mevcut bildirimi yeni bildirimle güncellemek için bildirim Yöneticisini kullanır **/
    private fun updateNotification() {
        notificationManager.notify(
            1,
            buildNotification()
        )
    }
}