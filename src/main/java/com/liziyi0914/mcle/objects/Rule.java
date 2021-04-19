package com.liziyi0914.mcle.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rule {

    String action;
    HashMap<String,String> os;
    /*
    name
    version
     */

}
