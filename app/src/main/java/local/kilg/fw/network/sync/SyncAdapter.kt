package local.kilg.fw.network.sync

import android.accounts.Account
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import local.kilg.fw.network.Repository
import local.kilg.fw.network.rest.WeatherApi
import local.kilg.fw.network.rest.pojo.Astronomy
import local.kilg.fw.network.rest.pojo.WeatherResponse
import java.util.*
import kotlin.collections.ArrayList
import local.kilg.fw.provider.ForecastWeatherContract.Forecast


/**
 * Created by kilg on 17.10.17/.
 */


class SyncAdapter : AbstractThreadedSyncAdapter {
    constructor(context: Context, autoInitialize: Boolean) : super(context, autoInitialize)
    constructor(context: Context, autoInitialize: Boolean, allowParallelSyncs: Boolean) : super(context, autoInitialize, allowParallelSyncs)


    override fun onPerformSync(account: Account?, extras: Bundle?, authority: String?, provider: ContentProviderClient?, syncResult: SyncResult?) {
       val repository = Repository(context)
        repository.loadWeather()
        repository.loadAstronomy()
    }

}


