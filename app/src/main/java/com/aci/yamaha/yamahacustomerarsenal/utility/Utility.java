package com.aci.yamaha.yamahacustomerarsenal.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.aci.yamaha.yamahacustomerarsenal.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utility {
    public static void snack(Context context, View view, String msg) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setActionTextColor(ContextCompat.getColor(context, R.color.white));
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.blackish));
        snackbar.show();
    }

    public static boolean isValidMobile(String mobile) {
        mobile = "+88" + mobile;

        Pattern patt = Pattern.compile("^(?:\\+?88)?01[15-9]\\d{8}$");
        Matcher matcher = patt.matcher(mobile);


        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    public static Boolean isConnected(Context context) {
        Boolean status = false;

        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInf = conMgr.getAllNetworkInfo();

        for (NetworkInfo inf : netInf) {
            if (inf != null) {
                if (inf.getTypeName().toUpperCase().contains("WIFI")
                        || inf.getTypeName().toUpperCase().contains("MOBILE")) {
                    if (inf.getState() == NetworkInfo.State.CONNECTED) {
                        status = true;
                        break;
                    }
                }
            }
        }

        return status;

    }


}
