package gunveer.codes.weathertoady

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar

class WeatherActivity : AppCompatActivity() {

    private lateinit var etCityName: EditText
    private lateinit var btnShowResults: Button
    private lateinit var tvCentigrade: TextView
    private lateinit var tvFahrenheit: TextView
    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView

    companion object{
        private const val TAG = "Weather"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        etCityName = findViewById(R.id.etCityName)
        btnShowResults = findViewById(R.id.btnShowResults)
        tvCentigrade = findViewById(R.id.tvCentigrade)
        tvFahrenheit = findViewById(R.id.tvFahrenheit)
        tvLatitude = findViewById(R.id.tvLatitude)
        tvLongitude = findViewById(R.id.tvLongitude)

        Log.d(TAG, "onCreate: ${intent.getStringExtra("cityName")}")

        etCityName.setText(intent.getStringExtra("cityName"))
        btnShowResults.setOnClickListener {
            getWeatherData(it, etCityName.text.toString())
        }
    }

    private fun getWeatherData(it: View, cityName: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.weatherapi.com/v1/current.json?key=35c9f92ac5bf4df0811144140212307&q=$cityName&aqi=no"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
        Response.Listener { response ->
            tvCentigrade.setText(response.getJSONObject("current").getString("temp_c").toString())
            tvFahrenheit.setText(response.getJSONObject("current").getString("temp_f").toString())
            tvLatitude.setText(response.getJSONObject("location").getString("lat").toString())
            tvLongitude.setText(response.getJSONObject("location").getString("lon").toString())
        }, Response.ErrorListener { error ->
                Snackbar.make(it, "Some error has occurred.", Snackbar.LENGTH_LONG).show()
            })
        queue.add(jsonObjectRequest)
    }
}