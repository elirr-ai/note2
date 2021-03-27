package com.example.notes2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckConnectivity1 {

	Context context;
    boolean haveConnectedWifi = false;
    boolean haveConnectedMobile = false;
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
	
    public CheckConnectivity1 (Context ctx) {
        this.context = ctx;
            }
    
    public boolean[] haveNetworkConnection() {
connectivityManager =
(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
 
NetworkInfo[] netInfo = connectivityManager.getAllNetworkInfo();

for (int i=0;i<netInfo.length;i++){
    if (netInfo[i].getTypeName().equalsIgnoreCase("WIFI"))
        if (netInfo[i].isConnected())
            haveConnectedWifi = true;
    if (netInfo[i].getTypeName().equalsIgnoreCase("MOBILE"))
        if (netInfo[i].isConnected())
            haveConnectedMobile = true;
}

    	boolean[] b=new boolean[2];
    	b[0]=haveConnectedWifi;
    	b[1]=haveConnectedMobile;
		return b;
    	
    	
    }
}
