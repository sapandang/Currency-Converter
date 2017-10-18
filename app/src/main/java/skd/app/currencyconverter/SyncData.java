package skd.app.currencyconverter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static skd.app.currencyconverter.GlobalData.pDialog;

/**
 * Created by sapan on 9/24/2017.
 */

public class SyncData extends AsyncTask<Void, Void, Boolean> {

    public static Context mcontext;
    public static Activity mactivity;
    public static String CURRENCY_STRING = "";

    /**
     * constructor
     *
     * @param con
     */
    public SyncData(Context con, Activity act) {
        mcontext = con;
        mactivity = act;
    }

    public static void saveCurrency() {
        SharedPreferences sharedPref = mcontext.getSharedPreferences(
                "myFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("CURRENCY_STRING", CURRENCY_STRING);
        editor.commit();
    }

    public static void LoadCurrency() {
        SharedPreferences sharedPref = mcontext.getSharedPreferences(
                "myFile", Context.MODE_PRIVATE);
        CURRENCY_STRING = sharedPref.getString("CURRENCY_STRING", ConverterUtil.curr);
    }

    /**
     * request the ccurrency API
     * Simple rouge code to get the Data
     *
     * @return
     * @throws Exception
     */
    public String requestAPI() throws Exception {

        String url = "http://api.fixer.io/latest?base=USD";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        //con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //

            //print result
            System.out.println(response.toString());
            return response.toString();
        }

        return "OOPS"; // nothing to return

    }

    /**
     * Connect in background
     *
     * @param voids
     * @return
     */
    @Override
    protected Boolean doInBackground(Void... voids) {

        mactivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pDialog = new SweetAlertDialog(mcontext, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Syncing ...");
                pDialog.setCancelable(false);
                pDialog.show();
            }
        });


        try {
           String cr= requestAPI();
            CURRENCY_STRING = cr;
            saveCurrency();
            LoadCurrency();
            //update the rates
            ConverterUtil convertUtilObj = new ConverterUtil();
            convertUtilObj.setRates();

            mactivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new SweetAlertDialog(mcontext, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Sucess")
                            .setContentText("Data Sucessfully Synced !")
                            .show();
                }
            });


        } catch (Exception e) {
            //GlobalData.pDialog.dismissWithAnimation();
            e.printStackTrace();
            Log.d("SyncData", "SomeThing Went Wrong");


            mactivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new SweetAlertDialog(mcontext, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Something went wrong!")
                            .show();
                }
            });


        }

        mactivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GlobalData.pDialog.dismissWithAnimation();
            }
        });


        return null;
    }
}
