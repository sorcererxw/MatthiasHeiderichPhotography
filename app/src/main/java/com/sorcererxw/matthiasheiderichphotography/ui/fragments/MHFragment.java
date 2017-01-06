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
import android.widget.Toast;

import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.data.Project;
import com.sorcererxw.matthiasheiderichphotography.data.db.ProjectDbManager;
import com.sorcererxw.matthiasheiderichphotography.data.db.ProjectTable;
import com.sorcererxw.matthiasheiderichphotography.ui.activities.MainActivity;
import com.sorcererxw.matthiasheiderichphotography.ui.adapters.MHAdapter;
import com.sorcererxw.matthiasheiderichphotography.ui.others.LinerMarginDecoration;
import com.sorcererxw.matthiasheiderichphotography.util.DisplayUtil;
import com.sorcererxw.matthiasheiderichphotography.util.MHPreference;
import com.sorcererxw.matthiasheiderichphotography.util.ResourceUtil;
import com.sorcererxw.matthiasheiderichphotography.util.ThemeHelper;
import com.sorcererxw.matthiasheiderichphotography.util.WebCatcher;
import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.typefaceviews.widgets.TypefaceSnackbar;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/22
 */
public class MHFragment extends BaseFragment {

    private static final String PROJECT_KEY = "project key";

    public static MHFragment newInstance(String projects) {
        MHFragment fragment = new MHFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PROJECT_KEY, projects);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Activity mActivity;

    private ProjectDbManager mFavoriteDBHelper;
    private Project mProject;
    private ProjectDbManager mProjectsHelper;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mFavoriteDBHelper = MHApp.getDb(getContext()).getFavoriteDbManager();
        mProject = Project.valueOf(getArguments().getString(PROJECT_KEY));
    }

    @Nullable
    @Override
    public View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mh, container, false);
        ButterKnife.bind(this, view);
        initViews(mActivity);
        initData();
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
                if (!mAdapter.hasItemShowed(data)) {
                    return;
                }
                if (mFavoriteDBHelper.isContain(data)) {
                    mFavoriteDBHelper.removeLink(data);
                    TypefaceSnackbar.make(mRoot, "Removed from Favorite", LENGTH_LONG).show();
                    holder.playDislikeAnim(getContext());
                } else {
                    mFavoriteDBHelper.saveLinks(Collections.singletonList(data));
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
        mProjectsHelper =
                MHApp.getDb(getContext()).getProjectDbManager(mProject);
        mProjectsHelper.getLinks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        mAdapter.setData(strings);
                    }
                });
        catchData();
    }

    private void catchData() {
        WebCatcher.catchImageLinks(mProject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        mSwipeRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshLayout.setRefreshing(true);
                            }
                        });
                    }

                    @Override
                    public void onCompleted() {
                        mSwipeRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                        Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }

                    @Override
                    public void onNext(List<String> strings) {
                        Collections.sort(strings);
                        if (strings.size() > 0) {
                            mProjectsHelper.saveLinks(strings);
                        }
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
        Timber.d("onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
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
