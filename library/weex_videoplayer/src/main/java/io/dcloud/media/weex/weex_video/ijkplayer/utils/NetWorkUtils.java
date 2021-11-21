//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.dcloud.media.weex.weex_video.ijkplayer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class NetWorkUtils {
    public static final String NETWORK_TYPE_WIFI = "wifi";
    public static final String NETWORK_TYPE_3G = "eg";
    public static final String NETWORK_TYPE_2G = "2g";
    public static final String NETWORK_TYPE_WAP = "wap";
    public static final String NETWORK_TYPE_UNKNOWN = "unknown";
    public static final String NETWORK_TYPE_DISCONNECT = "disconnect";

    public NetWorkUtils() {
    }

    public static int getNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
        NetworkInfo networkInfo = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();
        return networkInfo == null ? -1 : networkInfo.getType();
    }

    public static String getNetworkTypeName(Context context) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService("connectivity");
        String type = "disconnect";
        NetworkInfo networkInfo;
        if (manager != null && (networkInfo = manager.getActiveNetworkInfo()) != null) {
            if (networkInfo.isConnected()) {
                String typeName = networkInfo.getTypeName();
                if ("WIFI".equalsIgnoreCase(typeName)) {
                    type = "wifi";
                } else if ("MOBILE".equalsIgnoreCase(typeName)) {
                    String proxyHost = Proxy.getDefaultHost();
                    type = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork(context) ? "eg" : "2g") : "wap";
                } else {
                    type = "unknown";
                }
            }

            return type;
        } else {
            return type;
        }
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
        if (telephonyManager == null) {
            return false;
        } else {
            switch(telephonyManager.getNetworkType()) {
                case 0:
                    return false;
                case 1:
                    return false;
                case 2:
                    return false;
                case 3:
                    return true;
                case 4:
                    return false;
                case 5:
                    return true;
                case 6:
                    return true;
                case 7:
                    return false;
                case 8:
                    return true;
                case 9:
                    return true;
                case 10:
                    return true;
                case 11:
                    return false;
                case 12:
                    return true;
                case 13:
                    return true;
                case 14:
                    return true;
                case 15:
                    return true;
                default:
                    return false;
            }
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService("connectivity");
            NetworkInfo info = cm.getActiveNetworkInfo();
            return null != info && info.isConnected();
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(0);
            if (mMobileNetworkInfo != null && mMobileNetworkInfo.isAvailable()) {
                return mMobileNetworkInfo.isConnected();
            }
        }

        return false;
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(1);
            if (mWiFiNetworkInfo != null && mWiFiNetworkInfo.isAvailable()) {
                return mWiFiNetworkInfo.isConnected();
            }
        }

        return false;
    }
}
