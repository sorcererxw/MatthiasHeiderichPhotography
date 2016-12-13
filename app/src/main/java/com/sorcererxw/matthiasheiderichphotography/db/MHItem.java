package com.sorcererxw.matthiasheiderichphotography.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import rx.functions.Func1;


/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/12/12
 */

@AutoValue
public abstract class MHItem implements Parcelable {
    public static final String LINK = "link";

    public abstract String link();

    public static final Func1<Cursor, MHItem> MAPPER = new Func1<Cursor, MHItem>() {
        @Override
        public MHItem call(Cursor cursor) {
            String link = Db.getString(cursor, LINK);
            return new AutoValue_MHItem(LINK);
        }
    };

    public static final class Builder {
        private final ContentValues mValues = new ContentValues();

        public Builder link(String link) {
            mValues.put(LINK, link);
            return this;
        }

        public ContentValues build() {
            return mValues;
        }
    }
}
