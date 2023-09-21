package com.sharath070.accuweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.sharath070.accuweather.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigDecimal
import java.math.RoundingMode
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 9939046c410d1ce3f7a32524b3918678

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchWeatherData("Udupi")

        searchCity()

    }

    private fun searchCity() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query.capitalize())
                    Log.d("@@@@", "onQueryTextSubmit: $query Sumbitted")
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }


    private fun fetchWeatherData(cityName: String) {
        Log.d("@@@@", "onQueryTextSubmit: $cityName Sumbitted")
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response = retrofit.getWeather(
            cityName,
            "9939046c410d1ce3f7a32524b3918678", "metric"
        )

        response.enqueue(object : Callback<WeatherApp> {
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                Log.d("@@@@", "onQueryTextSubmit: ${response.body()} Sumbitted")

                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val temperature = responseBody.main.temp
                    val tempInC =
                        BigDecimal(temperature - 273.15).setScale(2, RoundingMode.HALF_EVEN)
                            .toString()
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunrise = responseBody.sys.sunrise.toLong()
                    val sunSet = responseBody.sys.sunset.toLong()
                    val seaLevel = responseBody.main.sea_level
                    val condition = responseBody.weather.firstOrNull()?.main ?: "Unknown"
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min

                    val minTempInC =
                        BigDecimal(minTemp - 273.15).setScale(2, RoundingMode.HALF_EVEN)
                            .toString()

                    val maxTempInC =
                        BigDecimal(maxTemp - 273.15).setScale(2, RoundingMode.HALF_EVEN)
                            .toString()

                    binding.tvTemp.text = tempInC
                    binding.tvWeather.text = condition
                    binding.tvMaxTemp.text = "Max : $maxTempInC"
                    binding.tvMinTemp.text = "Min : $minTempInC"

                    binding.tvHumidity.text = "$humidity%"
                    binding.tvWindSpeed.text = "$windSpeed m/s"
                    binding.tvSunrise.text = "${time(sunrise)}"
                    binding.tvSunset.text = "${time(sunSet)}"
                    binding.tvSea.text = "$seaLevel hPa"
                    binding.tvConditions.text = condition
                    binding.tvDay.text = dayName(System.currentTimeMillis())
                    binding.tvdate.text = date()
                    binding.tvCityName.text = cityName

                    changeBg(condition)

                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun changeBg(condition: String) {
        when(condition.lowercase()){
            "clear sky", "sunny", "clear" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
            "partly clouds", "clouds", "overcast", "mist", "foggy" -> {
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "rain", "light rain", "drizzle", "moderate rain", "showers", "heavy rain" -> {
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }
            "snow", "light snow", "heavy snow", "moderate snow", "blizzard" -> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

            else -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

        }
        binding.lottieAnimationView.playAnimation()
    }

    fun dayName(timestamp: Long): String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return  sdf.format(Date())
    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return  sdf.format(Date())
    }

    private fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return  sdf.format(Date(timestamp * 1000))
    }
}