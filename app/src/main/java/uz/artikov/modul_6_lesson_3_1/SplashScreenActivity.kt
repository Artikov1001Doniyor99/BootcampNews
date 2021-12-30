package uz.artikov.modul_6_lesson_3_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.ActionBar

class SplashScreenActivity : AppCompatActivity() {

    private val splashScreenTimeOut: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        Handler().postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },splashScreenTimeOut)

    }
}