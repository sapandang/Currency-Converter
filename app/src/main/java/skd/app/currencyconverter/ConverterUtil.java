package skd.app.currencyconverter;

import android.util.Log;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
/**
 * Created by sapan on 9/24/2017.
 */

public class ConverterUtil {

    static public JSONObject ratesJson;

   // public static String curr = "{\"base\":\"USD\",\"date\":\"2017-02-17\",\"rates\":{\"AUD\":1.3044,\"BGN\":1.8364,\"BRL\":3.0918,\"CAD\":1.3079,\"CHF\":0.99878,\"CNY\":6.867,\"CZK\":25.372,\"DKK\":6.9797,\"GBP\":0.80488,\"HKD\":7.7614,\"HRK\":6.9869,\"HUF\":289.5,\"IDR\":13332.0,\"ILS\":3.7061,\"INR\":67.1,\"JPY\":112.75,\"KRW\":1150.0,\"MXN\":20.474,\"MYR\":4.453,\"NOK\":8.3235,\"NZD\":1.3905,\"PHP\":50.055,\"PLN\":4.0662,\"RON\":4.2463,\"RUB\":58.185,\"SEK\":8.8712,\"SGD\":1.4165,\"THB\":34.995,\"TRY\":3.673,\"ZAR\":13.085,\"EUR\":0.93897}}";
    public static String curr = "{\"base\":\"USD\",\"date\":\"2017-02-22\",\"rates\":{\"AUD\":1.3021,\"BGN\":1.8604,\"BRL\":3.084,\"CAD\":1.3168,\"CHF\":1.0123,\"CNY\":6.8782,\"CZK\":25.702,\"DKK\":7.0705,\"GBP\":0.80329,\"HKD\":7.7604,\"HRK\":7.0846,\"HUF\":292.52,\"IDR\":13356.0,\"ILS\":3.7079,\"INR\":66.987,\"JPY\":112.99,\"KRW\":1142.7,\"MXN\":20.03,\"MYR\":4.4536,\"NOK\":8.3851,\"NZD\":1.3956,\"PHP\":50.288,\"PLN\":4.0889,\"RON\":4.3002,\"RUB\":57.814,\"SEK\":9.0079,\"SGD\":1.4178,\"THB\":35.01,\"TRY\":3.6023,\"ZAR\":13.105,\"EUR\":0.9512}}";
    public static String curr2 = "{\"base\":\"USD\",\"date\":\"2017-02-22\",\"rates\":{\"AUD\":1.3021,\"BGN\":1.8604,\"BRL\":3.084,\"CAD\":1.3168,\"CHF\":1.0123,\"CNY\":6.8782,\"CZK\":25.702,\"DKK\":7.0705,\"GBP\":0.80329,\"HKD\":7.7604,\"HRK\":7.0846,\"HUF\":292.52,\"IDR\":13356.0,\"ILS\":3.7079,\"INR\":66.987,\"JPY\":112.99,\"KRW\":1142.7,\"MXN\":20.03,\"MYR\":4.4536,\"NOK\":8.3851,\"NZD\":1.3956,\"PHP\":50.288,\"PLN\":4.0889,\"RON\":4.3002,\"RUB\":57.814,\"SEK\":9.0079,\"SGD\":1.4178,\"THB\":35.01,\"TRY\":3.6023,\"ZAR\":13.105,\"EUR\":0.9512}}";


    static public String ConvertAmt(String from, String to, String amt) {
        BigDecimal converted_amt = new BigDecimal("0.0").setScale(12, BigDecimal.ROUND_HALF_UP);
        BigDecimal conversionFactor = new BigDecimal("0.0").setScale(12, BigDecimal.ROUND_HALF_UP);
        BigDecimal fromamt = new BigDecimal("0.0"), toamtt = new BigDecimal("0.0");

        //check usd in from and to since usd does not exist in json

            try {
                fromamt = new BigDecimal(ratesJson.getString(from));
            } catch (Exception ex) {
                ex.printStackTrace();
            }



            try {
                toamtt = new BigDecimal(ratesJson.getString(to));
            } catch (Exception ex) {
                ex.printStackTrace();
            }



        try {
            conversionFactor = toamtt.divide(fromamt,12, RoundingMode.HALF_UP);


        } catch (Exception ex) {
            Log.d("SKD ConverterUtil", "Conversion error");
            ex.printStackTrace();
        }
        converted_amt = conversionFactor.multiply(new BigDecimal(amt));
        return converted_amt.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public void readCurrency(String text) {
        try {
            JSONObject curjson = new JSONObject(text);

            ratesJson = curjson.getJSONObject("rates");
            ratesJson.put("USD","1");
        } catch (Exception ex) {
            Log.d("SKD ConverterUtil", "Rates read error");
            ex.printStackTrace();
        }
    }


    public static String getAppRates()
    {
        String result="";
        try {
            JSONObject obj_json = new JSONObject(curr);
            result = obj_json.getString("date");
        }catch (Exception ex)
        {

        }
        return  result;
    }

    // set the rates variable
    public void setRates() {
        try{
            curr = SyncData.CURRENCY_STRING;
            readCurrency(curr);
        }catch (Exception ex) {
            Log.e("ConverterUtil","Json read error");
            // error occur load the default currency
            readCurrency(curr);
        }
    }
}
