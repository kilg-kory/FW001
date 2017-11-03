package local.kilg.fw.network.sync

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Created by kilg on 17.10.17.
 */
class AuthenticatorService : Service() {
    private val mAuthenticator: Authenticator = Authenticator(this)
    override fun onBind(intent: Intent?): IBinder = mAuthenticator.iBinder
}