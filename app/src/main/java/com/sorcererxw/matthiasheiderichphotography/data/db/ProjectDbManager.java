package com.sorcererxw.matthiasheiderichphotography.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/26
 */

public class ProjectDbManager {
    private BriteDatabase mDatabase;

    private String mProject;

    public ProjectDbManager(BriteDatabase database, String project) {
        mDatabase = database;
        mProject = project;
    }

    public Observable<List<String>> getLinks() {
        return mDatabase.createQuery(mProject, "SELECT * FROM " + mProject + " ORDER BY link")
                .mapToList(new Func1<Cursor, String>() {
                    @Override
                    public String call(Cursor cursor) {
                        return Db.getString(cursor, ProjectTable.LINK);
                    }
                });
    }

    public void saveLinks(List<String> links) {
        try (BriteDatabase.Transaction transaction = mDatabase.newTransaction()) {
            for (String link : links) {
                ContentValues values = new ContentValues();
                values.put(ProjectTable.LINK, link);
                mDatabase.insert(mProject, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            transaction.markSuccessful();
        }
    }

    public void removeLink(String link) {
        mDatabase.delete(mProject, ProjectTable.LINK + "=?", link);
    }

    public boolean isContain(String link) {
        try (Cursor cursor = mDatabase
                .query("SELECT link FROM " + mProject + " WHERE link=?", link)) {
            return cursor.moveToNext();
        }
    }

}
