package metalrain.com.shared;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import metalrain.com.shared.server.Server;

/**
 * Created by Adam on 4/23/2016.
 */
public class NetworkTools {
    public static String getCurrentIp(Context ctx) {
        WifiManager wifiMgr = (WifiManager) ctx.getSystemService(ctx.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        return ipAddress;
    }
}
