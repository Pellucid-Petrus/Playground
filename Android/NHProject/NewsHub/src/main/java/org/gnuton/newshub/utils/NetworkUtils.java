package org.gnuton.newshub.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.gnuton.newshub.R;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by gnuton on 6/8/13.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getName();

    public static String getDomainName(String url) {
        URI uri = null;
        url = url.replaceFirst("\\?.*","");
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        String domain = uri.getHost();
        if (domain == null || domain == "")
            return "";
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    public static String getMoreDetailedDomainName(String url) {
        URI uri = null;
        url = url.replaceFirst("\\?.*","");
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        String domain = uri.getHost() + uri.getPath();
        if (domain == null || domain == "")
            return "";
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    public static boolean isDeviceConnectedToInternet(){
        if (MyApp.mMainActivity == null)
            return true; // safer than false
        Context c = MyApp.mMainActivity.getApplicationContext();
        if (c == null)
            return true; // safer than false

        ConnectivityManager connMgr = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            Log.d(TAG, "Device not connected");
            //Notifications.showMsg(R.string.device_not_connected_to_internet);
            return false;
        }
    }
}
