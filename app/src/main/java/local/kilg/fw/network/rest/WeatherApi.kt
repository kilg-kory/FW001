package local.kilg.fw.network.rest

import io.reactivex.Observable
import local.kilg.fw.BuildConfig
import local.kilg.fw.network.rest.pojo.Astronomy
import local.kilg.fw.network.rest.pojo.WeatherResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
*  kilg on 16.10.17.
*/

interface WeatherApi{

    @GET("forecast10day/q/{country}/{city}.json")
    fun get10DayForecast(@Path("country") country:String, @Path("city") city: String) : Observable<WeatherResponse>

    @GET("astronomy/q/{country}/{city}.json")
    fun getAstronomy(@Path("country") country:String, @Path("city") city:String) : Observable<Astronomy>




    companion object Factory{
        fun create(): WeatherApi {

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC



            val client = OkHttpClient.Builder().addInterceptor(logging).build()

            val retrofit = Retrofit.Builder()
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://api.wunderground.com/api/${BuildConfig.WEATHER_API_KEY}/")
                    .build()

            return retrofit.create(WeatherApi::class.java)
        }
    }
}