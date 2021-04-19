package com.liziyi0914.mcle;

import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Arguments extends HashMap<String,String> {

    public static final String JAVA_AGENT = "java_agent";
    public static final String USERNAME = "auth_player_name";
    public static final String VERSION = "version_name";
    public static final String GAME_DIR = "game_directory";
    public static final String ASSETS_DIR = "assets_root";
    public static final String ASSETS_INDEX = "assets_index_name";
    public static final String UUID = "auth_uuid";
    public static final String TOKEN = "auth_access_token";
    public static final String TOKEN_OLD = "auth_session";
    public static final String USER_TYPE = "user_type";
    public static final String VERSION_TYPE = "version_type";
    public static final String WIDTH = "resolution_width";
    public static final String HEIGHT = "resolution_height";
    public static final String NATIVES_DIR = "natives_directory";
    public static final String LAUNCHER_NAME = "launcher_name";
    public static final String LAUNCHER_VERSION = "launcher_version";
    public static final String CP = "classpath";
    public static final String MAIN_CLASS = "main_class";
    public static final String USER_PROPERTIES = "user_properties";
    public static final String LIBRARIES_DIR = "libraries_directory";

    public static final String SERVER = "server";
    public static final String PORT = "port";

    public Arguments() {
        super();

        put(USER_TYPE,"mojang");

        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("windows")) {
            osName = "windows";
        } else if (osName.toLowerCase().contains("osx")) {
            osName = "osx";
        } else {
            osName = "linux";
        }
        put("os",osName);

        if (System.getProperty("os.arch").contains("64")) {
            put("arch","64");
        } else {
            put("arch","32");
        }

        put(LAUNCHER_NAME,"RedstoneEngine");
        put(LAUNCHER_VERSION,"0.1");
    }

    public void setPlayer(String name,String uuid,String token) {
        put(USERNAME,name);
        put(UUID,uuid);
        put(TOKEN,token);
        put(TOKEN_OLD,token);
    }

    public String replace(String line) {
        String result = line;
        String pattern = "\\$\\{(.*?)\\}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(result);
        while(m.find()) {
            try {
                result = result.replace("${"+m.group(1)+"}", Optional.ofNullable(get(m.group(1))).orElse(""));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
