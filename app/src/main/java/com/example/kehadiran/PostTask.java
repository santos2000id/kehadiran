package com.example.kehadiran;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class PostTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "";
    private final Context mContext;
    private final String mUrl;
    private final HashMap<String, String> params;
    public ArrayList<ArrayList<String>> nodes;
    ArrayList<HashMap<String, String>> resultList;
    private ProgressDialog pDialog;


    public PostTask(Context context, String url, HashMap<String, String> bodyParams) {
        mContext = context;
        mUrl = url;
        params = bodyParams;
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
            String jsonStr = sh.postServiceCall(url, params);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject results = new JSONObject(jsonStr);

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
        if (pDialog.isShowing())
            pDialog.dismiss();

    }
}