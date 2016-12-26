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
    REFLECTION_1,
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
        return toString().toLowerCase().replaceAll("_", " ");
    }

    public String toDatabaseTableName() {
        return toString().toLowerCase().replaceAll("_", "");
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

    public static Project fromProjectName(String name) {
        for (Project project : values()) {
            if (project.toString().equals(name)) {
                return project;
            }
        }
        return values()[0];
    }
//    "material-i",
//    "reflections-1",
//    "nowhere-in-particular",
//    "systems-layers-iii",
//    "systems-layers-ii",
//    "systems-layers",
//    "northbound",
//    "reflexionen-drei",
//    "reflexionen-zwei",
//    "reflexionen-eins",
//    "reflexiones",
//    "spektrum-eins",
//    "spektrum-zwei",
//    "fragment",
//    "uae",
//    "stadt-der-zukunft",
//    "kali",
//    "a7-southbound",
//    "ost-west",
//    "studien",
//    "color-berlin",
//    "random"
}
