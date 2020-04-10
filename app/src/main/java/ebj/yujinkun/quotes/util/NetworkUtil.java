package ebj.yujinkun.quotes.util;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;

import ebj.yujinkun.quotes.R;

public class NetworkUtil {

    public static boolean isConnected(Context context) {
        if (context == null) return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean registerNetworkListener(Context context, ConnectivityManager.NetworkCallback callback) {
        if (context == null) return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(callback);
            return true;
        }
        return false;
    }

    public static boolean unregisterNetworkListener(Context context, ConnectivityManager.NetworkCallback callback) {
        if (context == null) return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.unregisterNetworkCallback(callback);
            return true;
        }
        return false;
    }

    public static AlertDialog createNetworkDialog(Context context) {
        return createNetworkDialog(context, null);
    }

    public static AlertDialog createNetworkDialog(Context context, DialogInterface.OnDismissListener dismissListener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.no_network_connection)
                .setMessage(context.getString(R.string.please_connect))
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setOnDismissListener(dismissListener)
                .create();
        return dialog;
    }
}
