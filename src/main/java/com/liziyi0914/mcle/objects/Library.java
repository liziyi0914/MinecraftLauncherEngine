package com.liziyi0914.mcle.objects;

import com.alibaba.fastjson.JSONObject;
import com.liziyi0914.mcle.SystemInfo;
import com.liziyi0914.mcle.RulesExecutor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Library {

    String name;
    String url;
    HashMap<String,String> natives;
    ArrayList<Rule> rules;
    _Download downloads;

    JSONObject extract;

    boolean serverreq;
    boolean clientreq;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class _Download {

        Download artifact;
        HashMap<String,Download> classifiers;

    }

    public boolean execRules() {
        if (Objects.nonNull(rules)) {
            return RulesExecutor.exec(rules);
        }
        return true;
    }

    public String libUrl() {
        Download dl = availableDownload();
        if (Objects.isNull(dl)) {
            return null;
        } else {
            return dl.getUrl();
        }
    }

    public String libHash() {
        Download dl = availableDownload();
        if (Objects.isNull(dl)) {
            return null;
        } else {
            return dl.getSha1();
        }
    }

    private Download availableDownload() {
        if (Objects.nonNull(natives)) {
            if (!natives.containsKey(SystemInfo.get("name"))) {
                return null;
            }
            return downloads.getClassifiers().get(natives.get(SystemInfo.get("name")).replace("${arch}",SystemInfo.get("arch")));
        } else {
            return downloads.getArtifact();
        }
    }

}
