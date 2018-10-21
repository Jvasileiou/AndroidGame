package com.project.jvvas.moneyskills;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

public class PaymentDetails extends AppCompatActivity implements View.OnClickListener {

    private TextView txtId , txtDate , txtAmount , txtStatus ;
    private Button buttonOk ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);


        txtId      = (TextView) findViewById(R.id.textView_Id) ;
        txtDate    = (TextView) findViewById(R.id.textView_Date) ;
        txtAmount  = (TextView) findViewById(R.id.textView_Amount) ;
        txtStatus  = (TextView) findViewById(R.id.textView_Status) ;
        buttonOk   = (Button)   findViewById(R.id.button_OK);

        // Get Intent
        Intent intent = getIntent();

        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails")) ;
            showDetails(jsonObject.getJSONObject("response") , intent.getStringExtra("PaymentAmount") , intent.getStringExtra("Currency"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        buttonOk.setOnClickListener(this);

    }

    private void showDetails(JSONObject response, String paymentAmount , String currency) {

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        try {
            txtId.setText(response.getString("id"));
            txtDate.setText(currentDateTimeString);
            txtStatus.setText(response.getString("state"));

            if(currency.equals("EUR"))
            {
                txtAmount.setText(paymentAmount + " €");
            }
            else if(currency.equals("USD"))
            {
                txtAmount.setText(paymentAmount + " $");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_OK:
                goToProfilActivity();
                break;

        }
    }

    private void goToProfilActivity() {

        Intent singleMultiIntent = new Intent(PaymentDetails.this , ProfilActivity.class);

        // Clear all activities
        singleMultiIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(singleMultiIntent);
        finish();
    }

}


