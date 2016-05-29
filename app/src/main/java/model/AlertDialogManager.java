package model;

/**
 * Created by Hari Prahlad on 24-04-2016.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.murali.hariprahlad.walletbaba.R;

public class AlertDialogManager {
    /**
     * Function to display simple Alert Dialog
     * @param context - application context

     * @param message - alert message
     * @param status - success/failure (used to set icon)
     *               - pass null if you don't want icon
     * */
    public void showAlertDialog(Context context,String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle).create();
        // Setting Dialog Message
        alertDialog.setMessage(message);
        if(status != null)
            // Setting alert dialog icon
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}