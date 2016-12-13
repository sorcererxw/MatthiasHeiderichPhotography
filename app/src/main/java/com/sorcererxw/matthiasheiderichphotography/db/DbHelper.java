package com.sorcererxw.matthiasheiderichphotography.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/13
 */

public class DbHelper {
//    private BriteDatabase mDatabase;
//
//    private String mTable;
//
//    public DbHelper(BriteDatabase database, String table) {
//        mDatabase = database;
//        mTable = table;
//    }
//
//    private Observable<Boolean> isTableExisted(String table) {
//        return mDatabase.createQuery("sqlite_master",
//                "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
//                table)
//                .map(new Func1<SqlBrite.Query, Boolean>() {
//                    @Override
//                    public Boolean call(SqlBrite.Query query) {
//                        Cursor cursor = query.run();
//                        if (cursor == null) {
//                            return false;
//                        }
//                        boolean has = cursor.moveToNext();
//                        cursor.close();
//                        return has;
//                    }
//                });
//    }
//
//    private void createTable(String table) {
//        mDatabase.execute(
//                "CREATE TABLE " + table + " (id integer primary key autoincrement, link TEXT");
//    }
//
//    private Observable<Integer> clear() {
//        return Observable.just(mDatabase.delete(mTable, null));
//    }
//
//    public void deleteLink(String link) {
//        mDatabase.execute("DELETE link FROM ");
//        Observable.just(mDatabase.delete(mTable, ));
//    }
//
//    public Observable<Long> saveLink(String link) {
//        ContentValues value = new ContentValues();
//        value.put(MHItem.LINK, link);
//        return Observable.just(mDatabase.insert(mTable, value));
//    }
//
//    public Observable saveLinks(List<String> list) {
//        return clear().map(new Func1() {
//            @Override
//            public Object call(Object o) {
//                return null;
//            }
//        });
//    }
//
//    public Observable<List<String>> getLinks() {
//        String sql = "SELECT link FROM " + mTable;
//        return mDatabase.createQuery(mTable, "SELECT link FROM " + mTable)
//                .mapToList(new Func1<Cursor, String>() {
//                    @Override
//                    public String call(Cursor cursor) {
//                        String link = cursor.getString(0);
//                        cursor.close();
//                        return link;
//                    }
//                });
//    }
}
