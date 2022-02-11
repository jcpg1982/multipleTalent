package pe.com.master.machines.multiplicatalent.ui.activities.splashActivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import pe.com.master.machines.multiplicatalent.R
import pe.com.master.machines.multiplicatalent.databinding.ActivitySplashBinding
import pe.com.master.machines.multiplicatalent.ui.activities.mainActivity.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bounce))

        Handler(Looper.getMainLooper()).postDelayed({
            moveToMainActivity()
        }, 3000)
    }

    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}