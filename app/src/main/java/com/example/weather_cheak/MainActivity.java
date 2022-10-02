package com.example.weather_cheak;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText etCity,etCountry;
    TextView tvResult;

    private  final String url="https://home.openweathermap.org//data/2.5/weather";
    private  final String appid="76dd37d4affb24c01345ee00703e9849";

    DecimalFormat df=new DecimalFormat("#,##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity=findViewById(R.id.et_city);
        etCountry=findViewById(R.id.et_country);
       tvResult=findViewById(R.id.tv_result);
    }

    public void  getWeatherDetails() {

        String tempUrl = "";
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        if (city.equals("")) {
            tvResult.setText("city fild can not be empty !");
        } else {

            if (!country.equals("")) {

                tempUrl = url + "?q" + city + "," + country + "&appid" + appid;


        } else{

            tempUrl = url + "?q" + city + "&appid" + appid;
        }
            StringRequest stringRequest=new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    String output="";

                    try {
                        JSONObject jsonResponse=new JSONObject(response);
                        JSONArray jsonArray=jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather=jsonArray.getJSONObject(0);
                        String description=jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain=jsonResponse.getJSONObject("main");
                        double temp=jsonObjectMain.getDouble("temp")-273.15;
                        double feelsLike=jsonObjectMain.getDouble("feels Like")-273.15;
                        float pressure= jsonObjectMain.getInt("pressure");
                        int humidity=jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind=jsonResponse.getJSONObject("wind");
                        String wind=jsonObjectWind.getString("speed");
                        JSONObject jsonObjectclouds=jsonResponse.getJSONObject("clouds");
                        String clouds=jsonObjectclouds.getString("all");
                        JSONObject jsonObjectSys=jsonResponse.getJSONObject("Sys");
                        String countryName=jsonObjectSys.getString("country");
                        String cityName=jsonResponse.getString("country");
                        tvResult.setTextColor(Color.rgb(68,134,199));
                       output +="Current weather of "+cityName+"("+countryName+")"
                               +"\n Temp: "+df.format(temp)+"C"
                               +"\n Feels: "+df.format(feelsLike)+"C"
                               +"\n Humidity: "+humidity+" % "
                               +"\n Description: "+description
                               +"\n Wind Speed: "+wind+"m/s (meter per second)"
                               +"\n Cloudness: "+clouds+"%"
                               +"\n Pressure: "+pressure+"hPa";
                       tvResult.setText(output);





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(MainActivity.this,error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue =Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }

    }
}