package gunveer.codes.weathertoady

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var etFullName : EditText
    private lateinit var etMob : EditText
    private lateinit var etDate : EditText
    private lateinit var etAddress1 : EditText
    private lateinit var etAddress2 : EditText
    private lateinit var etPincode : EditText
    private lateinit var dropdownGender : Spinner
    private lateinit var btnCheck : Button
    private lateinit var btnRegister : Button
    private lateinit var tvDistrictFill : TextView
    private lateinit var tvStateFill : TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etFullName = findViewById(R.id.etFullName)
        etMob = findViewById(R.id.etMob)
        etDate = findViewById(R.id.etDate)
        etAddress1 = findViewById(R.id.etAddress1)
        etAddress2 = findViewById(R.id.etAddress2)
        etPincode = findViewById(R.id.etPincode)
        dropdownGender = findViewById(R.id.dropdownGender)
        btnCheck = findViewById(R.id.btnCheck)
        btnRegister = findViewById(R.id.btnRegister)
        tvDistrictFill = findViewById(R.id.tvDistrictFill)
        tvStateFill = findViewById(R.id.tvStateFill)

        val sharedPreferences = this.getPreferences(Context.MODE_PRIVATE)
        if(sharedPreferences.getString("cityName", null) != null){
            communeToWeather(sharedPreferences)
        }

        btnRegister.setOnClickListener {
            if(checkConditions(it)){
                //Save to shared preferences and move on to next activity.
                saveToSharedPreferences(sharedPreferences)
            }
        }

        etPincode.addTextChangedListener {
            btnCheck.isEnabled = it.toString().length == 6

        }

        btnCheck.setOnClickListener {
            getPinCodeData(etPincode.text.toString(), it)
        }

    }

    private fun communeToWeather(sharedPreferences: SharedPreferences) {
        val intent = Intent(this, WeatherActivity::class.java)
        intent.putExtra("cityName", sharedPreferences.getString("cityName", null))
        startActivity(intent)
    }

    private fun saveToSharedPreferences(sharedPreferences : SharedPreferences) {
        sharedPreferences.edit().putString("cityName", tvDistrictFill.text.toString()).apply()
        communeToWeather(sharedPreferences)
    }

    private fun getPinCodeData(pin : String, it : View) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.postalpincode.in/pincode/$pin"
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
        Response.Listener { response ->
            tvStateFill.setText(response.getJSONObject(0).getJSONArray("PostOffice").getJSONObject(0).getString("State").toString())
            tvDistrictFill.setText(response.getJSONObject(0).getJSONArray("PostOffice").getJSONObject(0).getString("District").toString())
        },
            Response.ErrorListener { error ->
                Snackbar.make(it, "Some error has occurred.", Snackbar.LENGTH_LONG).show()
            }
        )
        queue.add(jsonArrayRequest)
    }

    private fun checkConditions(it: View): Boolean {
        if(etFullName.text.toString() == "" || etMob.text.toString() == "" || etMob.text.toString().length <10
            || etDate.text.toString() == "" || etAddress1.text.toString() == "" || etAddress1.text.toString().length <3
            || etPincode.text.toString() == "" || dropdownGender.selectedItem == "Gender *"){
            Snackbar.make(it, "Please fill the required entries.", Snackbar.LENGTH_LONG).show()
            return false
        }
        return true
    }
}