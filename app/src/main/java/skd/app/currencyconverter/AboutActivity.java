package skd.app.currencyconverter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by sapan on 9/24/2017.
 */

public class AboutActivity extends AppCompatActivity {

    WebView wview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        //find the webview
        wview = (WebView)findViewById(R.id.wview);
        wview.loadUrl("file:///android_asset/about.html");
    }


}
