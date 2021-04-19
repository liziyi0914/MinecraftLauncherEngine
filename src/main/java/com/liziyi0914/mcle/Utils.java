package com.liziyi0914.mcle;

import com.liziyi0914.mcle.objects.Library;
import com.liziyi0914.mcle.objects.Rule;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Utils {

    public static void checkExist(File f) throws FileNotFoundException {
        if (!f.exists()) {
            throw new FileNotFoundException();
        }
    }

    public static String parseMavenPath(String path) {
        String format = "jar";
        if (path.contains("@")) {
            format = path.substring(path.indexOf("@")+1);
            path = path.substring(0,path.indexOf("@"));
        }

        String[] pieces = path.split(":");
        String sp = File.separator;
        StringBuilder builder = new StringBuilder(pieces[0].replace(".", sp));
        builder.append(sp).append(pieces[1])
                .append(sp).append(pieces[2])
                .append(sp).append(pieces[1]).append("-").append(pieces[2]);
        if (pieces.length > 3) {
            builder.append("-").append(pieces[3]);
        }
        builder.append(".").append(format);
        return builder.toString();
    }

    public static Stream<Library> librariesFilterByRules(ArrayList<Library> libs, boolean nativesOnly) {
        Stream<Library> stream = libs.stream();
        if (nativesOnly) {
            stream = stream.filter(library -> library.getNatives() == null);
        } else {
            stream = stream.filter(library -> library.getNatives() != null);
        }
        return stream
                .filter(Utils::librariesRulesExecutor);
    }

    public static Stream<Library> librariesFilterByRules(ArrayList<Library> libs) {
        return librariesFilterByRules(libs,true);
    }

    public static boolean librariesRulesExecutor(Library library) {
        ArrayList<Rule> rules = library.getRules();
        if (rules == null) return true;
        return RulesExecutor.exec(rules);
    }

}
