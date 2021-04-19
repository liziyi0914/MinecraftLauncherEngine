package com.liziyi0914.mcle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liziyi0914.mcle.objects.Library;
import com.liziyi0914.mcle.objects.Rule;
import com.liziyi0914.mcle.objects.Version;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Launcher {

    private final MinecraftHome home;
    private Version version;
    private Version inheritedVersion;
    private final Arguments arguments;

    private final ArrayList<Library> libs;

    public Launcher(MinecraftHome home, String version, Arguments arguments, File gameDir) {
        this.home = home;
        try {
            this.version = home.versionMgr.getVersion(version);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.version.getInheritsFrom() != null) {
            try {
                this.inheritedVersion = home.versionMgr.getVersion(this.version.getInheritsFrom());
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.libs = Stream.concat(
                    this.version.getLibraries().stream(),
                    this.inheritedVersion.getLibraries().stream()
            )
                    .filter(Library::execRules).collect(Collectors.toCollection(ArrayList::new));
        } else {
            this.libs = this.version.getLibraries().stream()
                    .filter(Library::execRules).collect(Collectors.toCollection(ArrayList::new));
        }
        this.arguments = arguments;

        this.arguments.put(Arguments.GAME_DIR, gameDir.getAbsolutePath());


    }

    public String genCommandLine() throws IOException {
        String gameArgs = "";
        String jvmArgs = "";
        String allArgs = "";

        if (version.getInheritsFrom() != null) {
            if (version.getJar() == null) {
                version.setJar(inheritedVersion.getJar());
            }
            if (version.getAssets() == null) {
                version.setAssets(inheritedVersion.getAssets());
            }
        }

        gameArgs = home.versionMgr.getGameArgs(version.getId());
        jvmArgs = home.versionMgr.getJvmArgs(version.getId());

        if (arguments.containsKey(Arguments.WIDTH)) gameArgs = gameArgs + " --width ${resolution_width}";
        if (arguments.containsKey(Arguments.HEIGHT)) gameArgs = gameArgs + " --height ${resolution_height}";
        if (arguments.containsKey(Arguments.SERVER)) gameArgs = gameArgs + " --server ${server}";
        if (arguments.containsKey(Arguments.PORT)) gameArgs = gameArgs + " --port ${port}";
        if (arguments.containsKey(Arguments.JAVA_AGENT)) jvmArgs = "-javaagent:${java_agent} " + jvmArgs;

        allArgs = jvmArgs+" -Dfile.encoding=UTF-8 " + " ${main_class} " + gameArgs;

        arguments.put(Arguments.NATIVES_DIR, new File(home.versionMgr.getRoot(), version.getId() + "/natives").getAbsolutePath());
        arguments.put(Arguments.VERSION, version.getId());
        arguments.put(Arguments.VERSION_TYPE, version.getType());
        arguments.put(Arguments.ASSETS_DIR, home.assetsMgr.getRoot().getAbsolutePath());
        arguments.put(Arguments.ASSETS_INDEX, version.getAssets());
        arguments.put(Arguments.LIBRARIES_DIR,home.librariesMgr.getRoot().getAbsolutePath());
        arguments.put(Arguments.CP, parseLibraries() + ";" + home.versionMgr.getJar(version.getId()).getAbsolutePath());
        arguments.put(Arguments.MAIN_CLASS, version.getMainClass());
        arguments.put(Arguments.USER_PROPERTIES, "{}");

        unzipNative();

        return arguments.replace(allArgs);
    }

    public String getGameArgs(Stream<Object> stream) {
        return stream.filter(obj -> obj.getClass() == String.class).map(obj -> (String) obj).collect(Collectors.joining(" "));
    }

    public String getJvmArgs(Stream<Object> stream) {
        return stream
                .map(obj -> {
                    if (obj.getClass() == String.class) return obj;
                    if (RulesExecutor.exec(new ArrayList<>(((JSONObject) obj).getJSONArray("rules").toJavaList(Rule.class)))) {
                        Object v = ((JSONObject) obj).get("value");
                        if (v.getClass() == String.class) {
                            return v;
                        } else {
                            return String.join(" ", ((JSONArray) v).toJavaList(String.class)).replace("Windows 10", "\"Windows 10\"");
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .map(obj -> (String) obj)
                .collect(Collectors.joining(" "));
    }

    public String parseLibraries() throws IOException {
        return Utils.librariesFilterByRules(libs)
                .map(library -> home.librariesMgr.getRoot().getAbsoluteFile() + File.separator + Utils.parseMavenPath(library.getName()))
                .collect(Collectors.joining(";"));
    }

    public void unzipNative() throws IOException {
        File _root = new File(home.versionMgr.getRoot(), version.getId() + "/natives");
        if (!_root.exists()) {
            _root.mkdirs();
        }
        Utils.librariesFilterByRules(libs,false)
                .forEach(library -> {
                    String path = library.getDownloads().getClassifiers().get(
                            arguments.replace(library.getNatives().get(arguments.get("os")))
                    ).getPath();
                    try {
                        JarArchiveInputStream in = new JarArchiveInputStream(new FileInputStream(new File(home.librariesMgr.getRoot(), path)));
                        JarArchiveEntry entry = null;
                        while ((entry = (JarArchiveEntry) in.getNextEntry()) != null) {
                            if (entry.getName().startsWith("META-INF")) continue;
                            File f = new File(_root, entry.getName());
                            if (f.exists()) continue;
                            if (entry.isDirectory()) {
                                f.mkdir();
                            } else {
                                OutputStream out = FileUtils.openOutputStream(f);
                                IOUtils.copy(in, out);
                                out.close();
                            }
                        }
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

}
