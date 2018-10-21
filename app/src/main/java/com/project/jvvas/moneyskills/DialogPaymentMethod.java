package com.project.jvvas.moneyskills;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DialogPaymentMethod extends AppCompatDialogFragment
{
    private EditText editTextAmount ;
    private RadioButton radioButton_Eur , radioButton_Usd ;
    private RadioGroup radioGroupCurrency;
    private dialogPaymentMethodListener listener;
    private String currency = "EUR"; // Default currency

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog , null);

        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(isThereAmount())
                        {
                            String amount = editTextAmount.getText().toString();
                            String currency = getCurrency();

                            listener.processPayment(amount , currency);
                        }
                        else{
                            showToast();
                        }
                    }
                });

        editTextAmount      = (EditText) view.findViewById(R.id.editText_EnterAmount);
        radioButton_Eur     = (RadioButton) view.findViewById(R.id.radioButton_Eur) ;
        radioButton_Usd     = (RadioButton) view.findViewById(R.id.radioButton_Usd) ;
        radioGroupCurrency  = (RadioGroup) view.findViewById(R.id.radioGroup_CurrencyGroup);

        radioGroupCurrency.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radioButton_Eur)
                {
                    currency = "EUR";
                }
                else if(checkedId == R.id.radioButton_Usd)
                {
                    currency = "USD";
                }
            }
        });

        return builder.create();
    }

    public String getCurrency(){
        return currency;
    }

    // called once the fragment is associated with its activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = ( dialogPaymentMethodListener ) context ;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Error in creating Dialog Payment") ;
        }
    }

    public boolean isThereAmount()
    {
        if(editTextAmount.getText().toString().equals(null) || editTextAmount.getText().toString().equals("0")
                || editTextAmount.getText().toString().equals("") || editTextAmount.getText().toString().equals(" ")  ){
            return false;
        }

        return true;
    }

    public void showToast(){ Toast.makeText(getActivity() , "No amount." , Toast.LENGTH_SHORT).show();   }

    public interface dialogPaymentMethodListener {
        void processPayment(String amount , String currency ) ;
    }

}
