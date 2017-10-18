package skd.app.currencyconverter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
/**
 * Created by sapan on 9/24/2017.
 */
public class MainActivity extends AppCompatActivity {

    Button  startbtn;
    Context mcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcon = this;

        //make status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }


        //Load Currency from the Preference
        SyncData sy = new SyncData(mcon,MainActivity.this);
        sy.LoadCurrency();


        setContentView(R.layout.activity_main);

        //find the views
        startbtn = (Button)findViewById(R.id.button);
        //set the listner
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getBaseContext(), ConverterActivity.class);
                startActivity(i);

            }
        });

    }
}
