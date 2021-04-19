package com.liziyi0914.mcle.objects;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Version {

    String id;
    String assets;
    String mainClass;
    int minimumLauncherVersion;
    String releaseTime;
    String time;
    String type;
    String clientVersion;
    String minecraftArguments;
    String jar;
    String inheritsFrom;

    HashMap<String, ArrayList<Object>> arguments;
    _AssetIndex assetIndex;

    JSONObject logging;

    HashMap<String,Download> downloads;

    ArrayList<Library> libraries;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class _AssetIndex {

        String id;
        String sha1;
        int size;
        int totalSize;
        String url;

    }
}
