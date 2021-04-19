package com.liziyi0914.mcle.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Download {

    String path;
    String sha1;
    int size;
    String url;

}