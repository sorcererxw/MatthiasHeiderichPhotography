package com.sorcererxw.matthiasheiderichphotography.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcererxw.matthiasheiderichphotography.ui.others.LinerMarginDecoration;
import com.sorcererxw.matthiasheiderichphotography.util.DialogUtil;
import com.sorcererxw.matthiasheiderichphotography.util.DisplayUtil;
import com.sorcererxw.matthiasheiderichphotography.util.ProjectDBHelper;
import com.sorcererxw.matthiasheiderichphotography.util.StringUtil;
import com.sorcererxw.matthiasheidericphotography.BuildConfig;
import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.matthiasheiderichphotography.ui.adapters.MHAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Sorcerer on 2016/8/22.
 */
public class MHFragment extends Fragment {

    private static final String PROJECT_KEY = "project key";

    private String mProjectName;

    public static MHFragment newInstance(String projects) {
        MHFragment fragment = new MHFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PROJECT_KEY, projects);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mh, container, false);
        ButterKnife.bind(this, view);
        mProjectName = getArguments().getString(PROJECT_KEY);
        initViews();
        initData();
        return view;
    }

    @BindView(R.id.recyclerView_fragment_mh)
    RecyclerView mRecyclerView;
    private MHAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private void initViews() {
        mAdapter = new MHAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView
                .addItemDecoration(new LinerMarginDecoration(DisplayUtil.dip2px(getContext(), 8)));
        mRecyclerView.setHasFixedSize(true);
    }

    private static final String PREFERENCES_UPDATE_DATE = "Preference-Update-Date";

    private void initData() {
        final ProjectDBHelper dbHelper =
                new ProjectDBHelper(getContext(), StringUtil.onlyLetter(mProjectName));
        if (System.currentTimeMillis()
                - getContext().getSharedPreferences(PREFERENCES_UPDATE_DATE,
                Context.MODE_PRIVATE).getLong(mProjectName, 0) < 86400000) {
            List<String> list = dbHelper.getLinks();
            if (list.size() == 0) {
                getContext().getSharedPreferences(PREFERENCES_UPDATE_DATE,
                        Context.MODE_PRIVATE).edit()
                        .putLong(mProjectName, 0).apply();
                initData();
            } else {
                mAdapter.setData(list);
            }
        } else {
            final MaterialDialog dialog = DialogUtil.getProgressDialog(getContext(), "Loading");
            dialog.show();
            Observable.just(
                    "http://www.matthias-heiderich.de/" + getArguments().getString(PROJECT_KEY))
                    .map(new Func1<String, String>() {
                        @Override
                        public String call(String path) {
                            URL url;
                            String res = "";
                            try {
                                url = new URL(path);
                                URLConnection conn = url.openConnection();
                                conn.setRequestProperty(
                                        "User-Agent",
                                        "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
                                BufferedReader br = new BufferedReader(
                                        new InputStreamReader(conn.getInputStream()));

                                String inputLine;
                                while ((inputLine = br.readLine()) != null) {
                                    res += inputLine;
                                }
                                br.close();
                            } catch (IOException e) {
                                if (BuildConfig.DEBUG) {
                                    e.printStackTrace();
                                }
                            }
                            return res;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<String, List<String>>() {
                        @Override
                        public List<String> call(String s) {
                            Log.d("main", s);
                            Pattern p = Pattern.compile(
                                    "data-image=\"http://static1.squarespace.com/static/[0-9a-z]*/[0-9a-z]*/[0-9a-z]*/[0-9a-z]*/[a-zA-Z0-9-_.]*\"");
                            Matcher m = p.matcher(s);
                            List<String> list = new ArrayList<>();
                            while (m.find()) {
                                String tmp = m.group(0).split("\"")[1];
                                if (!list.contains(tmp)) {
                                    list.add(tmp);
                                }

                            }
                            return list;
                        }
                    })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<String>>() {
                        @Override
                        public void call(List<String> strings) {
                            mAdapter.setData(strings);
                            getContext().getSharedPreferences(PREFERENCES_UPDATE_DATE,
                                    Context.MODE_PRIVATE).edit()
                                    .putLong(mProjectName,
                                            System.currentTimeMillis()).apply();
                            dbHelper.saveLinks(strings);
                            dialog.dismiss();
                        }
                    });
        }
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
