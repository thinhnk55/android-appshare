
package vn.vfossa.wifidirect;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Util {
    
    public static String getIpFromMac() {
        BufferedReader br = null;
        String line = null;
        
        try {
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            while ((line = br.readLine()) != null) {
                String[] s = line.split(" +");
                if (s != null && s.length >= 4) {
                    String device = s[5];
                    if (device.matches(".*p2p0.*")) {
                        br.close();                        
                        return s[0];
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getLocalIpAddress(Context context) {
        WifiManager mWifiManager = (WifiManager) context
                .getSystemService(android.content.Context.WIFI_SERVICE);
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        int ip = mWifiInfo.getIpAddress();
        String ipAddress = String.format("%d.%d.%d.%d", (ip & 0xff),
                (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));

        return ipAddress;
    }

    public static String getMacAddress(Context context) {
        WifiManager mWifiManager = (WifiManager) context
                .getSystemService(android.content.Context.WIFI_SERVICE);
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        String macAddress = mWifiInfo.getMacAddress();

        return macAddress;
    }
}
