package com.liziyi0914.mcle;

import com.liziyi0914.mcle.objects.Rule;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RulesExecutor {

    private static boolean transAction(String text) {
        return text.equals("allow");
    }

    public static boolean exec(ArrayList<Rule> rules) {
        boolean result = false;
        for (Rule rule : rules) {
            if (rule.getOs()==null || rule.getOs().isEmpty()) {
                result = transAction(rule.getAction());
                continue;
            }
            boolean match = true;
            for (String key : rule.getOs().keySet()) {
                boolean tmp = Pattern.compile(rule.getOs().get(key)).matcher(SystemInfo.get(key)).find();
                if (!tmp) {
                    match = false;
                    break;
                }
            }
            if (match) {
                result = transAction(rule.getAction());
            }
        }
        return result;
    }

}
