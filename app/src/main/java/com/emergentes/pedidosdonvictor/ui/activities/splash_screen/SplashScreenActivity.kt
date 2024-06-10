package com.emergentes.pedidosdonvictor.ui.activities.splash_screen


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.emergentes.pedidosdonvictor.R
import com.emergentes.pedidosdonvictor.ui.MainActivity
import com.emergentes.pedidosdonvictor.ui.activities.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            // Vemos si hay una cuenta registrada
            val account = GoogleSignIn.getLastSignedInAccount(this@SplashScreenActivity)
            if (account != null) {
                val intent = Intent(
                    this@SplashScreenActivity,
                    MainActivity::class.java
                )
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            } else {
                val intent = Intent(
                    this@SplashScreenActivity,
                    LoginActivity::class.java
                )
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            }
        },2000)

    }
}