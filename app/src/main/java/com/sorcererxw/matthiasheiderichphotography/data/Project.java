package com.sorcererxw.matthiasheiderichphotography.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/26
 */

public enum Project {
    MATERIAL_I,
    REFLECTIONS_1,
    NOWHERE_IN_PARTICULAR,
    SYSTEMS_LAYERS_III,
    SYSTEMS_LAYERS_II,
    SYSTEMS_LAYERS,
    NORTHBOUND,
    REFLEXIONEN_DREI,
    REFLEXIONEN_ZWEI,
    REFLEXIONEN_EINS,
    REFLEXIONES,
    SPEKTRUM_EINS,
    SPEKTRUM_ZWEI,
    FRAGMENT,
    UAE,
    STADT_DER_ZUKUNFT,
    KALI,
    A7_SOUTHBOUND,
    OST_WEST,
    STUDIEN,
    COLOR_BERLIN,
    RANDOM;

    public String toCollectionName() {
        String name = toString().toLowerCase();
        String[] nameFragment = name.split("_");
        String res = "";
        for (String fragment : nameFragment) {
            if (fragment.length() > 0) {
                if (fragment.matches("i+")) {
                    res += (fragment.toUpperCase() + " ");
                } else {
                    res += (Character.toUpperCase(fragment.charAt(0)) + fragment.substring(1))
                            + " ";
                }
            }
        }
        return res;
    }

    public String toDatabaseTableName() {
        return toString().toLowerCase().replaceAll("_", "");
    }

    public String toUrlName() {
        return toString().toLowerCase().replaceAll("_", "-");
    }

    public String toSimpleName() {
        return toString().toLowerCase();
    }

    public static List<String> toCollectionNameList() {
        List<String> list = new ArrayList<>();
        for (Project project : Project.values()) {
            list.add(project.toCollectionName());
        }
        return list;
    }
}
