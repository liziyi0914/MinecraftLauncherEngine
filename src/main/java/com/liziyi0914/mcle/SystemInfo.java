package com.liziyi0914.mcle;

import java.util.HashMap;

public class SystemInfo {

    static HashMap<String,String> map = new HashMap<>();

    static {
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String osArch = System.getProperty("os.arch");

        if (osName.toLowerCase().contains("windows")) {
            osName = "windows";
        } else if (osName.toLowerCase().contains("osx")) {
            osName = "osx";
        } else {
            osName = "linux";
        }

        if (osArch.contains("64")) {
            osArch = "64";
        } else {
            osArch = "32";
        }

        map.put("name",osName);
        map.put("version",osVersion);
        map.put("arch",osArch);
    }

    public static String get(String key) {
        return map.get(key);
    }

}
