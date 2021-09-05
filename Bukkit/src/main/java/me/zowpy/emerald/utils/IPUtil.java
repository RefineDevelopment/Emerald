package me.zowpy.emerald.utils;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 8/16/2021
 * Project: Emerald
 */

public class IPUtil {

    @SneakyThrows
    public static String getIP() {
        URL url = new URL("https://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        return in.readLine();
    }
}
