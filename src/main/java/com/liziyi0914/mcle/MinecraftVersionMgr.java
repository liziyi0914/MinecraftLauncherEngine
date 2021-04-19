package com.liziyi0914.mcle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liziyi0914.mcle.objects.Version;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MinecraftVersionMgr {

    private final File root;

    public MinecraftVersionMgr(File versionsRoot) {
        root = versionsRoot;
    }

    public File getRoot() {
        return root;
    }

    public File getJar(String version, boolean checkExistence) throws IOException {
        if (getVersion(version).getInheritsFrom()!=null) {
            return getJar(getVersion(version).getInheritsFrom(),checkExistence);
        }
        return getFile(version, version + ".jar", checkExistence);
    }

    public File getJar(String version) throws IOException {
        return getJar(version, true);
    }

    public void delJar(String version) throws IOException {
        Files.delete(getJar(version).toPath());
    }

    public void addJar(String version, File originFile) throws IOException {
        Files.copy(originFile.toPath(), getJar(version).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }


    public File getJsonFile(String version, boolean checkExistence) throws FileNotFoundException {
        return getFile(version, version + ".json", checkExistence);
    }

    public File getJsonFile(String version) throws FileNotFoundException {
        return getJsonFile(version, true);
    }

    public void delJson(String version) throws IOException {
        Files.delete(getJsonFile(version).toPath());
    }

    public void addJson(String version, File originFile) throws IOException {
        Files.copy(originFile.toPath(), getJsonFile(version).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public File getVersionFile(String version, boolean checkExistence) throws FileNotFoundException {
        return getFile(version, "", checkExistence);
    }

    public File getVersionFile(String version) throws FileNotFoundException {
        return getVersionFile(version, true);
    }

    public Version getVersion(String version) throws IOException {
        return JSON.parseObject(Files.readAllBytes(getJsonFile(version).toPath()), Version.class);
    }

    public ArrayList<String> getVersions() {
        return Arrays.stream(Objects.requireNonNull(root.list())).filter(name -> {
            try {
                getJsonFile(name);
            } catch (FileNotFoundException e) {
                return false;
            }
            return true;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    public File getFile(String version, String name, boolean checkExistence) throws FileNotFoundException {
        File file = new File(root, version + "/" + name);
        if (checkExistence) {
            Utils.checkExist(file);
        }
        return file;
    }

    public boolean rename(String version, String newName) throws IOException {
        File dir = getFile(version, "", true);
        File newDir = getFile(newName, "", false);

        File json = getFile(newName, version + ".json", false);
        File newJson = getFile(newName, newName + ".json", false);

        File jar = getFile(newName, version + ".jar", false);
        File newJar = getFile(newName, newName + ".jar", false);

        if (newDir.exists()) {
            return false;
        }
        dir.renameTo(newDir);
        jar.renameTo(newJar);
        json.renameTo(newJson);

        Version v = getVersion(newName);
        v.setId(newName);

        Files.write(newJson.toPath(), JSONObject.toJSONString(v).getBytes());

        return true;
    }

    public String getGameArgs(String version) throws IOException {
        Version ver = getVersion(version);
        if (ver.getMinecraftArguments()==null) {
            if (ver.getInheritsFrom() != null) {
                return getGameArgs(ver.getInheritsFrom()) + " " + parseGameArgs(ver.getArguments().getOrDefault("game",new ArrayList<>()).stream());
            } else {
                return parseGameArgs(ver.getArguments().get("game").stream());
            }
        } else {
            return ver.getMinecraftArguments();
        }
    }

    String parseGameArgs(Stream<Object> stream) {
        return stream.filter(obj -> obj.getClass() == String.class).map(obj -> (String) obj).collect(Collectors.joining(" "));
    }

    public String getJvmArgs(String version) throws IOException {
        Version ver = getVersion(version);
        if (ver.getMinecraftArguments()==null) {
            if (ver.getInheritsFrom() != null) {
                return getJvmArgs(ver.getInheritsFrom()) + " " + parseJvmArgs(ver.getArguments().getOrDefault("jvm",new ArrayList<>()).stream());
            } else {
                return parseJvmArgs(ver.getArguments().get("jvm").stream());
            }
        } else {
            return "-Djava.library.path=${natives_directory} -Dminecraft.launcher.brand=${launcher_name} -Dminecraft.launcher.version=${launcher_version} -cp ${classpath}";
        }
    }

    String parseJvmArgs(Stream<Object> stream) {
        return stream.filter(obj -> obj.getClass() == String.class).map(obj -> (String) obj).collect(Collectors.joining(" "));
    }

}
