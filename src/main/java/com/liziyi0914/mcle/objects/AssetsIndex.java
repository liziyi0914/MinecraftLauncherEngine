package com.liziyi0914.mcle.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetsIndex {

    HashMap<String,AssetsIndexObj> objects;
    boolean virtual = false;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssetsIndexObj {

        String hash;
        int size;

    }
}
