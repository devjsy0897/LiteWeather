package com.jsy.LiteWeather;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GetWeather {
    private Context mContext;
    public static int temp;
    public GetWeather(){}
    // 날씨 가져오기 통신
    public void weather(double lat, double lon) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://api.openweathermap.org/data/2.5/weather"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(lat + "", "UTF-8")); /*latitude*/
        urlBuilder.append("&" + URLEncoder.encode("lon", "UTF-8") + "=" + URLEncoder.encode(lon + "", "UTF-8")); /*longitude*/
        urlBuilder.append("&" + URLEncoder.encode("APPID", "UTF-8") + "=9e1362a69fea09a2cc6ac3de6124ed95"); /*API_KEY*/
        urlBuilder.append("&" + URLEncoder.encode("units", "UTF-8") + "=metric"); /*API_KEY*/

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        jParsing(sb.toString());
        Log.i("weathertest", sb.toString());
    }
    // 날씨 가져오기 통신

    void jParsing(String data) {
        try {

            JSONObject jObject = new JSONObject(data);
            JSONObject mainObject = jObject.getJSONObject("main");

            temp = mainObject.getInt("temp");

            //Log.i("temptest",temp+"");
        } catch (Exception e) {
            Log.i("mytag", e.getLocalizedMessage());
        }
    }
    public int getTemp(){
        return temp;
    }
}
