package com.cs446.kluster.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkUtils {

	public NetworkUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static Boolean CheckConnection(Context context) {
	    ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
        	Toast.makeText(context, "No Connection", Toast.LENGTH_SHORT).show();
        	return false;
        }
	}
}
