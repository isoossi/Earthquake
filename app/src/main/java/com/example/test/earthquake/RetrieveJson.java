package com.example.test.earthquake;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

//import java.util.Date;

/**
 * Created by test on 25.5.2016.
 */
public class RetrieveJson extends AsyncTask<String, Void, ArrayList<String>> {
    HttpURLConnection httpclient = null;
    BufferedReader reader = null;
    Observer o = null;
    String result2 = null;
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
            URL url = new URL("https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson");
            httpclient = (HttpURLConnection) url.openConnection();
            //httpclient.setRequestMethod("GET");
            httpclient.connect();
            int test = httpclient.getContentLength();
            // Read the input stream into a String
            InputStream in = httpclient.getInputStream();

            StringBuffer buffer = new StringBuffer();
            if (in == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(in, "utf-8"), 8);

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            result2 = buffer.toString();

        } catch (Exception e) {
            Log.e("StringBuilding", "Error converting result " + e.toString());
        }

        try{
            JSONObject jsonobjekt = new JSONObject(result2);

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
                                String mag = properties.getString("mag");
                                if(!mag.equals("null")) {
                                    list.add(mag);
                                } else {
                                    list.add("No data");
                                }
                                String place = properties.getString("place");
                                if(!place.equals("null")) {
                                    list.add(place);
                                } else {
                                    list.add("No data");
                                }
                                String time = properties.getString("time");
                                if(!time.equals("null")) {
                                    long timel = Long.parseLong(time);
                                    String format = "yyyy-MM-dd HH:mm:ss";
                                    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                                    sdf.setTimeZone(TimeZone.getDefault());
                                    String newtime = sdf.format(new Date(timel));
                                    list.add(newtime);
                                } else {
                                    list.add("No data");
                                }
                                String coordinates = point.getString("coordinates");
                                if(!coordinates.equals("null")) {
                                    String test[] = coordinates.split("\\[");
                                    String testi = test[1];
                                    String test2[] = testi.split(",");
                                    String first = test2[0];
                                    list.add(first);
                                    String second = test2[1];
                                    list.add(second);
                                } else {
                                    list.add("No data");
                                }
                                Log.e("JSON", "> " + mag + place + time + coordinates );
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
