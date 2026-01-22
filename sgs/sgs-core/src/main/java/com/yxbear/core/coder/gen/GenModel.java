package com.yxbear.core.coder.gen;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GenModel {

    int extendsType;

    String name;

    List<String> attrs = new ArrayList<>();

    transient String pkg;

    public String getPkg() {
        if (pkg != null) { return pkg; }
        if (name == null || name.trim() == "") { return ""; }
        name = name.trim();
        for (int i = 1; i < name.length(); i++) {
            if (Character.isUpperCase(name.charAt(i))) { return pkg = name.substring(0, i); }
        }
        return name;
    }

    public String getModelName() {
        if (name == null) { return ""; }
        name = name.trim();
        String pkg = getPkg();
        return name.substring(pkg.length(), name.length());
    }

}
