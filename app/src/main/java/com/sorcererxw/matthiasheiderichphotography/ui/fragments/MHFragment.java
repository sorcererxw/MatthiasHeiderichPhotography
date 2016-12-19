package com.sorcererxw.matthiasheiderichphotography.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.db.ProjectDBHelper;
import com.sorcererxw.matthiasheiderichphotography.ui.activities.MainActivity;
import com.sorcererxw.matthiasheiderichphotography.ui.adapters.MHAdapter;
import com.sorcererxw.matthiasheiderichphotography.ui.others.LinerMarginDecoration;
import com.sorcererxw.matthiasheiderichphotography.util.DisplayUtil;
import com.sorcererxw.matthiasheiderichphotography.util.MHPreference;
import com.sorcererxw.matthiasheiderichphotography.util.ResourceUtil;
import com.sorcererxw.matthiasheiderichphotography.util.StringUtil;
import com.sorcererxw.matthiasheiderichphotography.util.ThemeHelper;
import com.sorcererxw.matthiasheiderichphotography.util.WebCatcher;
import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.typefaceviews.widgets.TypefaceSnackbar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

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

    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mFavoriteDBHelper = new ProjectDBHelper(getContext(), "favorite");
    }

    private ProjectDBHelper mDbHelper;

    private MHPreference<Long> mLastSync;

    @Nullable
    @Override
    public View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mh, container, false);
        ButterKnife.bind(this, view);
        mProjectName = getArguments().getString(PROJECT_KEY);
        initViews(mActivity);
        return view;
    }

    @BindView(R.id.coordinatorLayout_fragment_mh)
    CoordinatorLayout mRoot;

    @BindView(R.id.swipeRefreshLayout_fragment_mh)
    SwipeRefreshLayout mSwipeRefreshLayout;

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
                    TypefaceSnackbar.make(mRoot, "Removed from Favorite", LENGTH_LONG).show();
                    holder.playDislikeAnim(getContext());
                } else {
                    mFavoriteDBHelper.saveLink(data);
                    TypefaceSnackbar.make(mRoot, "Added to Favorite", LENGTH_LONG)
                            .setAction("show favorites", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((MainActivity) getActivity())
                                            .showFragment(MainActivity.FRAGMENT_TAG_FAVORITE);
                                }
                            })
                            .setActionTextColor(ResourceUtil.getColor(getContext(), R.color.white))
                            .show();
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

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                catchData();
            }
        });
    }

    private void initData() {
        mDbHelper = new ProjectDBHelper(getContext(), StringUtil.onlyLetter(mProjectName));
        mLastSync = MHApp.getInstance().getPrefs().getLastSync(mProjectName, 0L);
        if (System.currentTimeMillis() - mLastSync.getValue() < 86400000) {
            List<String> list = mDbHelper.getLinks();
            if (list.size() == 0) {
                mLastSync.setValue(0L);
                initData();
            } else {
                Collections.sort(list);
                mAdapter.setData(list);
            }
        } else {
            catchData();
        }
    }

    public void catchData() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        WebCatcher.catchImageLinks(
                "http://www.matthias-heiderich.de/" + getArguments().getString(PROJECT_KEY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        Collections.sort(strings);
                        mAdapter.setData(strings);
                        mLastSync.setValue(System.currentTimeMillis());
                        mDbHelper.saveLinks(strings);
                        mSwipeRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    @Override
    protected void refreshUI() {
        super.refreshUI();
        mAdapter.setNightMode(MHApp.getInstance().getPrefs().getThemeNightMode().getValue());
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(
                ThemeHelper.getPrimaryColorRes(getActivity()));
        mSwipeRefreshLayout.setColorSchemeResources(
                ThemeHelper.getAccentColorRes(getActivity()));
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
