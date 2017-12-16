package local.kilg.fw

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Path
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation
import kotlinx.android.synthetic.main.activity_splash.*
import local.kilg.fw.network.Repository
import kotlin.concurrent.thread


/**
 * Splash.
 *
 * Loading data from server.
 * Using only when database haven't values in table forecast.
 *
 */
class SplashActivity : AppCompatActivity() {

    /**
     * If have connection to internet run load data through repository,
     * than close and return to main activity and set "CLEAR_TOP" to intent
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        thread(true) {

            val repository = Repository(this)

            if (repository.isOnline()) {
                repository.loadWeather()
                repository.loadAstronomy()
            }else{
                tv_state.setText(R.string.heve_not_internet_connection)
            }

            Thread.sleep(5000)

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        beginAnimation()
    }


    /**
     * Begin animation for image view by Path.
     */
    private fun beginAnimation() {

        val imageSize = 50
        val left = resources.displayMetrics.widthPixels / 2 - imageSize
        val top = resources.displayMetrics.heightPixels / 2 - imageSize
        val radius = 200f
        val duration = 1000L

        val path = Path()
        path.addCircle(left.toFloat(), top.toFloat(), radius, Path.Direction.CW)


        val animator = ObjectAnimator.ofFloat(iv_sunny, "x", "y", path)
        animator.duration = duration
        animator.repeatCount = Animation.INFINITE
        val animatorSet = AnimatorSet()


        animatorSet.playTogether(animator)
        animatorSet.start()
    }
}
