package me.zowpy.emerald.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 8/16/2021
 * Project: Emerald
 */

public class IPUtil {

    public static String getIP() {
        URL url;
        BufferedReader in;
        String ipAddress;
        try {
            url = new URL("http://bot.whatismyipaddress.com");
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            ipAddress = in.readLine().trim();
            /* If not connected to internet, then
             * the above code will return an empty
             * string, we can check the length and
             * if the length is not greater than zero,
             * then we can go for LAN/Local/Private IP.
             */

            if (!(ipAddress.length() > 0)) {
                try {
                    InetAddress ip = InetAddress.getLocalHost();
                    System.out.println((ip.getHostAddress()).trim());
                    ipAddress = (ip.getHostAddress()).trim();
                } catch(Exception exp) {
                    ipAddress = "ERROR";
                }
            }
        } catch (Exception ex) {
            /* If we do not have internet, try getting it from our host. */
            try {
                InetAddress ip = InetAddress.getLocalHost();
                System.out.println((ip.getHostAddress()).trim());
                ipAddress = (ip.getHostAddress()).trim();
            } catch(Exception exp) {
                ipAddress = "ERROR";
            }
        }

        return ipAddress;
    }
}
