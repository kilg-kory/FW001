package local.kilg.fw.network.sync

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Created by kilg on 18.10.17.
 */
class SyncAdapterService : Service() {
    private val sSyncAdapter: SyncAdapter = SyncAdapter(this, true)
    override fun onBind(intent: Intent?): IBinder = sSyncAdapter.syncAdapterBinder
}