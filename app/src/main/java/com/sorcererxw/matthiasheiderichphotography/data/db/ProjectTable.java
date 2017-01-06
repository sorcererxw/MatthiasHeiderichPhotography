package com.sorcererxw.matthiasheiderichphotography.data.db;

import android.database.sqlite.SQLiteDatabase;

import com.sorcererxw.matthiasheiderichphotography.data.Project;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/26
 */

public class ProjectTable {
    public static String PROJECT_FAVORITE = "favorite";

    public static final String LINK = "link";

    public static void onCreate(SQLiteDatabase db) {
        onCreate(db, PROJECT_FAVORITE);
        for (Project project : Project.values()) {
            onCreate(db, project.toDatabaseTableName());
        }
    }

    public static void onCreate(SQLiteDatabase db, String name) {
        db.execSQL(new TableBuilder(name)
                .addTextColumn(LINK, TableBuilder.FLAG_NOT_NULL)
                .addPrimaryKeyColumn(LINK)
                .build());
    }
}
