package com.example.kehadiran;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.kehadiran.model.Kehadiran;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class GetTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "";
    private final Context mContext;
    private final String mUrl;
    public AsyncResponse delegate = null;
    private ProgressDialog pDialog;
    private ArrayList<Kehadiran> data;

    public GetTask(Context context, String url, AsyncResponse delegate) {
        mContext = context;
        mUrl = url;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    protected Void doInBackground(Void... voids) {
        String text = null;
        try {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = mUrl;
            String jsonStr = sh.getServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    // Getting JSON Array node
                    JSONArray results = new JSONArray(jsonStr);
                    data = new ArrayList<Kehadiran>();
                    if (results != null) {
                        for (int i = 0; i < results.length(); i++) {

                            JSONObject hadir = results.getJSONObject(i);
                            String tanggal = hadir.getString("tglKehadiran");
                            String nim = hadir.getString("nim");
                            Double jarak = hadir.getDouble("jarak");
                            Kehadiran kehadiran = new Kehadiran();
                            kehadiran.setNim(nim);
                            kehadiran.setMasuk(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(tanggal.replace("T", " ")));
                            kehadiran.setJarak(jarak.toString());
                            data.add(kehadiran);
                        }
                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                            /*
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            });
                            */
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                        /*
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Couldn't get json from server. Check LogCat for possible errors!",
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                        */
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        delegate.processFinish(data);
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}