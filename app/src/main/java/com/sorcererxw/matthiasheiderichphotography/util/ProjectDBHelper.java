package com.sorcererxw.matthiasheiderichphotography.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/22
 */
public class ProjectDBHelper {

    private SQLiteDatabase mDB;

    private String mTable;

    public ProjectDBHelper(Context context, String table) {
        mTable = table;
        mDB = context.openOrCreateDatabase("PROJECT", Context.MODE_PRIVATE, null);
        if (!isTableExisted(table)) {
            createTable(table);
        }
    }

    private boolean isTableExisted(String name) {
        String sql =
                "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
        Cursor cursor = mDB.rawQuery(sql, new String[]{name});
        boolean has = cursor.moveToNext();
        cursor.close();
        return has;
    }

    private void createTable(String name) {
        String sql = "CREATE TABLE " + name
                + " (id integer primary key autoincrement, link TEXT)";
        mDB.execSQL(sql);
    }

    public void clear() {
        String sql = "DELETE FROM " + mTable;
        mDB.execSQL(sql);
    }

    public boolean isLinkContain(String link) {
        String sql = "select link from " + mTable + " where link=?";
        Cursor cursor = mDB.rawQuery(sql, new String[]{link});
        boolean has = cursor.moveToNext();
        cursor.close();
        return has;
    }

    public void deleteLink(String link) {
        String sql = "delete from " + mTable + " where link=?";
        mDB.execSQL(sql, new String[]{link});
    }

    public void saveLink(String link) {
        String sql = "insert into " + mTable + "(link) values(?)";
        mDB.execSQL(sql, new String[]{link});
    }

    public void saveLinks(List<String> list) {
        clear();
        String sql = "INSERT INTO " + mTable + "(link) values(?)";
        for (int i = 0; i < list.size(); i++) {
            mDB.execSQL(sql, new String[]{list.get(i)});
        }
    }

    public List<String> getLinks() {
        String sql =
                "SELECT link FROM " + mTable;
        Cursor cursor = mDB.rawQuery(sql, null);
        List<String> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
        cursor.close();
        return list;
    }
}
