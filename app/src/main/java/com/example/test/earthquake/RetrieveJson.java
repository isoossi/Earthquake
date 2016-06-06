package com.example.test.earthquake;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by test on 25.5.2016.
 */
public class RetrieveJson extends AsyncTask<String, Void, ArrayList<String>> {
    URLConnection httpclient;
    Observer o = null;
    StringBuilder result = new StringBuilder();
    String result2 = "";
    static JSONObject properties = null;
    static JSONObject point = null;
    ArrayList<String> list = new ArrayList<>();


    public interface Observer {
        void update(ArrayList<String> list);
    }

    public void setObserver(Observer ob){
        o = ob;

    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        try {
            URL url = new URL("http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_day.geojson");
            httpclient = url.openConnection();
            InputStream in = new BufferedInputStream(httpclient.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"),8 );

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line+ "\n");
            }
            in.close();
            result2 = result.toString();

        } catch (Exception e) {
            Log.e("StringBuilding", "Error converting result " + e.toString());
        }

        try{
            JSONObject jsonobjekt= new JSONObject(result2);

            //finding magnitude, place name, time and coordinates from json
            for (int i = 0; i < 1; i++) {
                try {
                    JSONArray jSearchData = jsonobjekt.getJSONArray("features");
                    for(int j=0; j < jSearchData.length(); j++){
                        JSONObject object = jSearchData.getJSONObject(j);
                        properties = object.getJSONObject("properties");
                        point = object.getJSONObject("geometry");

                        for (int k = 0; k < 1 ; k++) {
                            try {
                                String value = properties.getString("mag");
                                double max = Double.parseDouble(value);
                                String mag = properties.getString("mag");
                                list.add(mag);
                                String place = properties.getString("place");
                                list.add(place);
                                String time = properties.getString("time");
                                long timel = Long.parseLong(time);
                                String format = "yyyy-MM-dd HH:mm:ss";
                                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                                sdf.setTimeZone(TimeZone.getDefault());
                                String newtime = sdf.format(new Date(timel));
                                list.add(newtime);
                                String coordinates = point.getString("coordinates");
                                String test[] = coordinates.split("\\[");
                                String testi = test[1];
                                String test2[] = testi.split(",");
                                String first = test2[0];
                                list.add(first);
                                String second = test2[1];
                                list.add(second);
                                Log.e("JSON", "> " + mag + place + coordinates );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            }catch (JSONException e){
            Log.e("StringBuilding", "Error converting result " + e.toString());

        }
        return list;
    }

    protected void onPostExecute(ArrayList<String> list) {
        o.update(list);
    }

}
