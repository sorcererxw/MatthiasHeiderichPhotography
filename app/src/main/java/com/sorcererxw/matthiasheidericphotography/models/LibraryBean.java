package com.sorcererxw.matthiasheidericphotography.models;

/**
 * Created by Sorcerer on 2016/8/25.
 */
public class LibraryBean {
    private String mName;
    private String mAuthor;
    private String mLicense;
    private String mLink;
    private String mDescribe;

    public LibraryBean(String name, String author, String license, String link,
                       String describe) {
        mName = name;
        mAuthor = author;
        mLicense = license;
        mLink = link;
        mDescribe = describe;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getLicense() {
        return mLicense;
    }

    public void setLicense(String license) {
        mLicense = license;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getDescribe() {
        return mDescribe;
    }

    public void setDescribe(String describe) {
        mDescribe = describe;
    }
}
