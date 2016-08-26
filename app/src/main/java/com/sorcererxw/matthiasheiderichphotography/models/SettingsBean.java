package com.sorcererxw.matthiasheiderichphotography.models;

/**
 * Created by Sorcerer on 2016/8/26.
 */
public class SettingsBean {
    private String mLabel;
    private String mValue;
    private SettingsCallback mCallback;

    public interface SettingsCallback {
        void call(SettingsBean item, SettingCallbackCallback callback);
    }

    public interface SettingCallbackCallback {
        void call(SettingsBean item);
    }

    public SettingsBean(String label, String value,
                        SettingsCallback callback) {
        mLabel = label;
        mValue = value;
        mCallback = callback;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public SettingsCallback getCallback() {
        return mCallback;
    }

    public void setCallback(
            SettingsCallback callback) {
        mCallback = callback;
    }
}
