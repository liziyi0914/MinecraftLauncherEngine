package com.liziyi0914.mcle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MinecraftLibrariesMgr {

    private final File root;

    public MinecraftLibrariesMgr(File librariesRoot) {
        root = librariesRoot;
    }

    public File getLibrary(String path,boolean checkExistence) throws FileNotFoundException {
        File libFile = new File(root, Utils.parseMavenPath(path));
        if (checkExistence) {
            Utils.checkExist(libFile);
        }
        return libFile;
    }

    public File getLibrary(String path) throws FileNotFoundException {
        return getLibrary(path,true);
    }

    public void delLibrary(String path) throws IOException {
        Files.delete(getLibrary(path).toPath());
    }

    public void addLibrary(String path,File originFile) throws IOException {
        File libFile = new File(Utils.parseMavenPath(path));
        libFile.getParentFile().mkdirs();
        Files.copy(originFile.toPath(), libFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public File getRoot() {
        return root;
    }

}
