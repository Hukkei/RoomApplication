package com.example.hukkei.roomapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class alertDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Bekräfta!");
        builder.setMessage("Bekräfta genom att välja ett av alternativen nedan.");
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(getActivity(), "Negativ knapp", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Toast.makeText(getActivity(),"Positiv knapp",Toast.LENGTH_SHORT).show();
            }
        });

                Dialog dialog = builder.create();

                return dialog;
    }
}
