package com.liziyi0914.mcle;

import java.io.File;

public class MinecraftHome {

    private final File root;
    public MinecraftAssetsMgr assetsMgr;
    public MinecraftVersionMgr versionMgr;
    public MinecraftLibrariesMgr librariesMgr;

    public MinecraftHome(File home) {
        root = home;
        assetsMgr = new MinecraftAssetsMgr(new File(root,"assets"));
        versionMgr = new MinecraftVersionMgr(new File(root,"versions"));
        librariesMgr = new MinecraftLibrariesMgr(new File(root,"libraries"));
    }

    public File getRoot() {
        return root;
    }

}
