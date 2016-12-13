package com.sorcererxw.matthiasheiderichphotography.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.ui.adapters.MHAdapter;
import com.sorcererxw.matthiasheiderichphotography.ui.others.LinerMarginDecoration;
import com.sorcererxw.matthiasheiderichphotography.util.DialogUtil;
import com.sorcererxw.matthiasheiderichphotography.util.DisplayUtil;
import com.sorcererxw.matthiasheiderichphotography.util.MHPreference;
import com.sorcererxw.matthiasheiderichphotography.db.ProjectDBHelper;
import com.sorcererxw.matthiasheiderichphotography.util.StringUtil;
import com.sorcererxw.matthiasheiderichphotography.util.WebCatcher;
import com.sorcererxw.matthiasheidericphotography.BuildConfig;
import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.typefaceviews.TypefaceSnackbar;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/22
 */
public class MHFragment extends BaseFragment {

    private static final String PROJECT_KEY = "project key";

    private String mProjectName;

    public static MHFragment newInstance(String projects) {
        MHFragment fragment = new MHFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PROJECT_KEY, projects);
        fragment.setArguments(bundle);
        return fragment;
    }

    ProjectDBHelper mFavoriteDBHelper;

//    @Inject
//    BriteDatabase mDatabase;

    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
//        MHApp.getComponent(getContext()).inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mFavoriteDBHelper = new ProjectDBHelper(getContext(), "favorite");
    }

    @Nullable
    @Override
    public View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mh, container, false);
        ButterKnife.bind(this, view);
        mProjectName = getArguments().getString(PROJECT_KEY);
        initViews(mActivity);
        initData();
        return view;
    }

    @BindView(R.id.coordinatorLayout_fragment_mh)
    CoordinatorLayout mRoot;

    @BindView(R.id.recyclerView_fragment_mh)
    RecyclerView mRecyclerView;
    private MHAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private void initViews(Activity activity) {
        mAdapter = new MHAdapter(activity);
        mAdapter.setOnItemLongClickListener(new MHAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(View view, String data, int position,
                                    MHAdapter.MHViewHolder holder) {
                if (!mAdapter.hasItemShowed(position)) {
                    return;
                }
                if (mFavoriteDBHelper.isLinkContain(data)) {
                    mFavoriteDBHelper.deleteLink(data);
                    TypefaceSnackbar.make(mRoot, "Removed from Favorite", Snackbar.LENGTH_LONG)
                            .show();
                    holder.playDislikeAnim(getContext());
                } else {
                    mFavoriteDBHelper.saveLink(data);
                    TypefaceSnackbar.make(mRoot, "Added to Favorite", Snackbar.LENGTH_LONG).show();
                    holder.playLikeAnim(getContext());
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView
                .addItemDecoration(new LinerMarginDecoration(DisplayUtil.dip2px(getContext(), 4)));
        mRecyclerView.setHasFixedSize(true);
    }

    private void initData() {
        final ProjectDBHelper dbHelper =
                new ProjectDBHelper(getContext(), StringUtil.onlyLetter(mProjectName));
        final MHPreference<Long> lastSync =
                MHApp.getInstance().getPrefs().getLastSync(mProjectName, 0L);
        if (!BuildConfig.DEBUG && System.currentTimeMillis() - lastSync.getValue() < 86400000) {
            List<String> list = dbHelper.getLinks();
            if (list.size() == 0) {
                lastSync.setValue(0L);
                initData();
            } else {
                mAdapter.setData(list);
            }
        } else {
            final MaterialDialog dialog = DialogUtil.getProgressDialog(getContext(), "Loading");
            dialog.show();
            WebCatcher.catchImageLinks(
                    "http://www.matthias-heiderich.de/" + getArguments().getString(PROJECT_KEY))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<String>>() {
                        @Override
                        public void call(List<String> strings) {
                            mAdapter.setData(strings);
                            lastSync.setValue(System.currentTimeMillis());
                            dbHelper.saveLinks(strings);
                            dialog.dismiss();
                        }
                    });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void refreshUI() {
        super.refreshUI();
        mAdapter.setNightMode(MHApp.getInstance().getPrefs().getThemeNightMode().getValue());
    }

    @Override
    public void onThemeChanged() {
        super.onThemeChanged();
    }

    @Override
    public void onToolbarDoubleTap() {
        super.onToolbarDoubleTap();
        mRecyclerView.smoothScrollToPosition(0);
    }
}
