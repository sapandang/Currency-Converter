package skd.app.currencyconverter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;



import java.math.BigDecimal;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.widget.Toast.makeText;
/**
 * Created by sapan on 9/24/2017.
 */
public class ConverterActivity extends AppCompatActivity {

    Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b0,bdecimal,bclr;
    View.OnClickListener buttonListner;
    Context con;
    ImageButton swapbtn,infoButton,syncButton;
    Spinner spinFrom ,spinTo;

    EditText etFrom, etTo ;

    //App Vars
    BigDecimal fromCurrency,toCurrency ;
    BigDecimal conversionFactor= new BigDecimal("2");
    String fromText="",ToText="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //make status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.converter_activity);



        //hiding the keyboard
        InputMethodManager im =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

        //set the listner
        con = this;

        //set the listners
        buttonListner = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId())
                {
                    case R.id.b1 : appendTextToEditText("1");
                        break;
                    case R.id.b2 : appendTextToEditText("2");
                        break;
                    case R.id.b3 : appendTextToEditText("3");
                        break;
                    case R.id.b4 : appendTextToEditText("4");
                        break;
                    case R.id.b5 : appendTextToEditText("5");
                        break;
                    case R.id.b6 : appendTextToEditText("6");
                        break;
                    case R.id.b7 : appendTextToEditText("7");
                        break;
                    case R.id.b8 : appendTextToEditText("8");
                        break;
                    case R.id.b9 : appendTextToEditText("9");
                        break;
                    case R.id.b0 : appendTextToEditText("0");
                        break;
                    case R.id.bdecimal : appendTextToEditText(".");
                        break;
                    case R.id.bclr : removeTextFromEditText();
                        break;
                }

            }
        };

        //set up the currency
        ConverterUtil convertUtilObj = new ConverterUtil();
        convertUtilObj.setRates();

        //find the editText
        etFrom = (EditText)findViewById(R.id.editText);
        etTo = (EditText)findViewById(R.id.editText2);






        //Find the spinner
        spinFrom   = (Spinner)findViewById(R.id.currFrom);
        spinTo = (Spinner)findViewById(R.id.currTo);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency,android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinFrom.setAdapter(adapter);
        spinTo.setAdapter(adapter);

        //find the button
        b1 = (Button)findViewById(R.id.b1);
        b2 = (Button)findViewById(R.id.b2);
        b3 = (Button)findViewById(R.id.b3);
        b4 = (Button)findViewById(R.id.b4);
        b5 = (Button)findViewById(R.id.b5);
        b6 = (Button)findViewById(R.id.b6);
        b7 = (Button)findViewById(R.id.b7);
        b8 = (Button)findViewById(R.id.b8);
        b9 = (Button)findViewById(R.id.b9);
        b0 = (Button)findViewById(R.id.b0);
        bdecimal = (Button)findViewById(R.id.bdecimal);
        bclr =  (Button)findViewById(R.id.bclr);
        infoButton = (ImageButton) findViewById(R.id.imageButton2);
        syncButton = (ImageButton) findViewById(R.id.imageButton3);

        //set the listners
        b1.setOnClickListener(buttonListner);
        b2.setOnClickListener(buttonListner);
        b3.setOnClickListener(buttonListner);
        b4.setOnClickListener(buttonListner);
        b5.setOnClickListener(buttonListner);
        b6.setOnClickListener(buttonListner);
        b7.setOnClickListener(buttonListner);
        b8.setOnClickListener(buttonListner);
        b9.setOnClickListener(buttonListner);
        b0.setOnClickListener(buttonListner);
        bdecimal.setOnClickListener(buttonListner);
        bclr.setOnClickListener(buttonListner);

        //Find the Swap button
        swapbtn = (ImageButton)findViewById(R.id.imageButton);

        //set the swap button hadler
        swapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //copy text from ToTextFied to From field
                etFrom.setText(etTo.getText());
                //clear the to Text field
                etTo.setText("");
                //Swap the Spin From and to
                int spinFromselectedindex = spinFrom.getSelectedItemPosition();
                int spinToselectedindex = spinTo.getSelectedItemPosition();

                spinTo.setSelection(spinFromselectedindex);
                spinFrom.setSelection(spinToselectedindex);

                //convert the currency
                convertCurrency();
            }
        });


        //set the info button listner
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), AboutActivity.class);
                startActivity(i);
            }
        });

        //set sync button event handler
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SweetAlertDialog(con, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Update Currency Rates")
                        .setContentText("Currency rates will be sync from server ")
                        .setConfirmText("Yes,update it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                //Show Update Box


                                //show the sync Dialog
                                SyncData obj_sync = new SyncData(con,ConverterActivity.this);
                                obj_sync.execute();

                            }
                        })
                        .show();





            }
        });

        //setting up the spinner listners
       spinFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
             //  Toast.makeText(con, adapterView.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();

               //converting the amount
               fromText = adapterView.getItemAtPosition(pos).toString();
convertCurrency();
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });

        //listner fot the second list
        spinTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
           //    Toast.makeText(con, adapterView.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
                    ToText = adapterView.getItemAtPosition(pos).toString();
                convertCurrency();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }




    //append the characters to the EditText
    public  void  appendTextToEditText(String text)
    {

        //etFocused.setText(etFocused.getText()+text); //append the new text
        etFrom.append(text);
        //Set the currency edit text
        convertCurrency();

    }


    //Convert the currency Data
    public  void convertCurrency()
    {
        //avoid null if user del all chars
        Log.d("eFromText","==>"+etFrom.getText());
       if(etFrom.getText().length()>0)
       {
           fromCurrency = new BigDecimal(""+etFrom.getText());
           toCurrency = fromCurrency.multiply(conversionFactor);
           //set the text field
          // etTo.setText(toCurrency.toString());
           etTo.setText(""+ConverterUtil.ConvertAmt(fromText,ToText,""+etFrom.getText()));

       //    Toast.makeText(con,"converted amt "+fromText+" to "+ToText+" amt "+
        //           ConverterUtil.ConvertAmt(fromText,ToText,""+etFrom.getText()),Toast.LENGTH_SHORT).show();
       }else
       {
           etTo.setText("");
       }


    }

    //remove the character from the editText
    public void removeTextFromEditText()
    {
        Editable editable = etFrom.getText();
        int charCount = editable.length();
        if (charCount > 0) {
            editable.delete(charCount - 1, charCount);
            //convert the currency
            convertCurrency();
        }

    }

    //
}
