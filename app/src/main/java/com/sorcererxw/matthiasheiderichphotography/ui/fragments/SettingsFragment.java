package com.sorcererxw.matthiasheiderichphotography.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.models.SettingsBean;
import com.sorcererxw.matthiasheiderichphotography.ui.adapters.ResolutionAdapter;
import com.sorcererxw.matthiasheiderichphotography.ui.adapters.SettingsAdapter;
import com.sorcererxw.matthiasheiderichphotography.util.StringUtil;
import com.sorcererxw.matthiasheidericphotography.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sorcerer on 2016/8/26.
 */
public class SettingsFragment extends Fragment {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @BindView(R.id.recyclerView_fragment_settings)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        List<SettingsBean> list = new ArrayList<>();

        final SharedPreferences sharedPreferences =
                getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);

        list.add(new SettingsBean(
                        "Preview resolution",
                        StringUtil.resolutionToString(sharedPreferences
                                .getInt(MHApp.PREFERENCE_SETTINGS_PREVIEW_RESOLUTION, 1080)),
                        new SettingsBean.SettingsCallback() {
                            @Override
                            public void call(final SettingsBean item,
                                             final SettingsBean.SettingCallbackCallback callback) {
                                new MaterialDialog.Builder(SettingsFragment.this.getContext())
                                        .adapter(new ResolutionAdapter(
                                                        SettingsFragment.this.getContext(),
                                                        MHApp.RESOLUTIONS,
                                                        sharedPreferences.getInt(
                                                                MHApp.PREFERENCE_SETTINGS_PREVIEW_RESOLUTION,
                                                                1080)
                                                ),
                                                new LinearLayoutManager(SettingsFragment.this.getContext(),
                                                        LinearLayoutManager.VERTICAL, false))
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog,
                                                                @NonNull DialogAction which) {
                                                item.setValue(
                                                        StringUtil.resolutionToString(
                                                                ((ResolutionAdapter) dialog
                                                                        .getRecyclerView()
                                                                        .getAdapter()).getCurrentValue())
                                                );
                                                sharedPreferences.edit()
                                                        .putInt(MHApp.PREFERENCE_SETTINGS_PREVIEW_RESOLUTION,
                                                                ((ResolutionAdapter) dialog
                                                                        .getRecyclerView()
                                                                        .getAdapter())
                                                                        .getCurrentValue()).commit();
                                                MHApp.previewResolution = ((ResolutionAdapter) dialog
                                                        .getRecyclerView()
                                                        .getAdapter())
                                                        .getCurrentValue();
                                                callback.call(item);
                                            }
                                        })
                                        .positiveText("Ok")
                                        .negativeText("Cancel")
                                        .build().show();
                            }
                        }
                )
        );
        list.add(new SettingsBean(
                        "Download resolution",
                        StringUtil.resolutionToString(sharedPreferences
                                .getInt(MHApp.PREFERENCE_SETTINGS_DOWNLOAD_RESOLUTION, 1080)),
                        new SettingsBean.SettingsCallback() {
                            @Override
                            public void call(final SettingsBean item,
                                             final SettingsBean.SettingCallbackCallback callback) {
                                new MaterialDialog.Builder(SettingsFragment.this.getContext())
                                        .adapter(new ResolutionAdapter(
                                                        SettingsFragment.this.getContext(),
                                                        MHApp.RESOLUTIONS,
                                                        sharedPreferences.getInt(
                                                                MHApp.PREFERENCE_SETTINGS_DOWNLOAD_RESOLUTION,
                                                                1080)
                                                ),
                                                new LinearLayoutManager(SettingsFragment.this.getContext(),
                                                        LinearLayoutManager.VERTICAL, false))
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog,
                                                                @NonNull DialogAction which) {
                                                item.setValue(
                                                        StringUtil.resolutionToString(
                                                                ((ResolutionAdapter) dialog
                                                                        .getRecyclerView()
                                                                        .getAdapter()).getCurrentValue())
                                                );
                                                sharedPreferences.edit()
                                                        .putInt(MHApp.PREFERENCE_SETTINGS_DOWNLOAD_RESOLUTION,
                                                                ((ResolutionAdapter) dialog
                                                                        .getRecyclerView()
                                                                        .getAdapter())
                                                                        .getCurrentValue()).commit();
                                                MHApp.downloadResolution = ((ResolutionAdapter) dialog
                                                        .getRecyclerView()
                                                        .getAdapter())
                                                        .getCurrentValue();
                                                callback.call(item);
                                            }
                                        })
                                        .positiveText("Ok")
                                        .negativeText("Cancel")
                                        .build().show();
                            }
                        }
                )
        );

        SettingsAdapter adapter = new SettingsAdapter(getContext(), list);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        mRecyclerView.setHasFixedSize(true);


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
