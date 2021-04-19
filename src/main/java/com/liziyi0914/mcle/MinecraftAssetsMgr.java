package com.liziyi0914.mcle;

import com.alibaba.fastjson.JSON;
import com.liziyi0914.mcle.objects.AssetsIndex;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MinecraftAssetsMgr {

    private final File root;

    public MinecraftAssetsMgr(File assetsRoot) {
        root = assetsRoot;
    }

    public File getRoot() {
        return root;
    }

    public File getIndexFile(String version,boolean checkExistence) throws IOException {
        File jsonFile = new File(root,"indexes/"+version+".json");
        if(checkExistence){
            Utils.checkExist(jsonFile);
        }
        return jsonFile;
    }

    public File getIndexFile(String version) throws IOException {
        return getIndexFile(version,true);
    }

    public AssetsIndex getIndex(String version) throws IOException {
        return JSON.parseObject(Files.readAllBytes(getIndexFile(version).toPath()), AssetsIndex.class);
    }

    public void delIndex(String version) throws IOException {
        Files.delete(getIndexFile(version).toPath());
    }

    public void saveIndex(String version,AssetsIndex index) throws IOException {
        new DataOutputStream(new FileOutputStream(getIndexFile(version,false))).writeUTF(JSON.toJSONString(index));
    }

    public void addIndex(String version,File originFile) throws IOException {
        Files.copy(originFile.toPath(), getIndexFile(version,false).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }


    public File getObject(String hash,boolean checkExistence) throws FileNotFoundException {
        File objFile = new File(root,"objects/"+hash.substring(0,2)+"/"+hash);
        if (checkExistence) {
            Utils.checkExist(objFile);
        }
        return objFile;
    }

    public File getObject(String hash) throws FileNotFoundException {
        return getObject(hash,true);
    }

    public void delObject(String hash) throws IOException {
        Files.delete(getObject(hash).toPath());
    }

    public void addObject(String hash,File originFile) throws IOException {
        Files.copy(originFile.toPath(), getObject(hash,false).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }


    public File getVirtual(String path,boolean checkExistence) throws FileNotFoundException {
        File objFile = new File(root,"virtual/legacy/"+path);
        if (checkExistence) {
            Utils.checkExist(objFile);
        }
        return objFile;
    }

    public File getVirtual(String path) throws FileNotFoundException {
        return getVirtual(path,true);
    }

    public void delVirtual(String path) throws IOException {
        Files.delete(getVirtual(path).toPath());
    }

    public void addVirtual(String path,File originFile) throws IOException {
        Files.copy(originFile.toPath(), getVirtual(path,false).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }


    public File getLogCfg(String name,boolean checkExistence) throws FileNotFoundException {
        File objFile = new File(root,"log_configs/"+name);
        if (checkExistence) {
            Utils.checkExist(objFile);
        }
        return objFile;
    }

    public File getLogCfg(String name) throws FileNotFoundException {
        return getLogCfg(name,true);
    }

    public void delLogCfg(String name) throws IOException {
        Files.delete(getLogCfg(name).toPath());
    }

    public void addLogCfg(String name,File originFile) throws IOException {
        Files.copy(originFile.toPath(), getLogCfg(name,false).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
