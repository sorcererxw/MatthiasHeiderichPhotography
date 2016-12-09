package com.sorcererxw.matthiasheiderichphotography.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.ui.adapters.MHAdapter;
import com.sorcererxw.matthiasheiderichphotography.ui.others.LinerMarginDecoration;
import com.sorcererxw.matthiasheiderichphotography.ui.views.dialog.TypefaceMaterialDialogBuilder;
import com.sorcererxw.matthiasheiderichphotography.util.DisplayUtil;
import com.sorcererxw.matthiasheiderichphotography.util.ProjectDBHelper;
import com.sorcererxw.matthiasheiderichphotography.util.ResourceUtil;
import com.sorcererxw.matthiasheiderichphotography.util.StyleUtil;
import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;
import com.sorcererxw.matthiasheidericphotography.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/31
 */
public class FavoriteFragment extends BaseFragment {
    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @BindView(R.id.textView_fragment_favorite_empty)
    TextView mEmptyView;

    @BindView(R.id.recyclerView_fragment_favorite)
    RecyclerView mRecyclerView;

    private MHAdapter mAdapter;

    private ProjectDBHelper mProjectDBHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, view);

        mProjectDBHelper = new ProjectDBHelper(getContext(), "favorite");

        mAdapter = new MHAdapter(mActivity);
        mAdapter.setOnItemLongClickListener(new MHAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(View view, final String data, final int position,
                                    final MHAdapter.MHViewHolder holder) {
                new TypefaceMaterialDialogBuilder(getContext())
                        .positiveText("Yes")
                        .negativeText("No")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog,
                                                @NonNull DialogAction which) {
                                mProjectDBHelper.deleteLink(data);
                                mAdapter.removeItem(position);
                                if (mAdapter.getItemCount() == 0) {
                                    mEmptyView.setVisibility(View.VISIBLE);
                                }
                            }
                        })
                        .content("Remove the item from Favorite ?")
                        .build().show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView
                .addItemDecoration(new LinerMarginDecoration(DisplayUtil.dip2px(getContext(), 4)));
        mRecyclerView.setHasFixedSize(true);

        mEmptyView.setTypeface(TypefaceHelper.getTypeface(getContext(), TypefaceHelper.Type.Book));
        mEmptyView.setCompoundDrawables(null,
                new IconicsDrawable(getContext())
                        .icon(GoogleMaterial.Icon.gmd_favorite)
                        .color(ResourceUtil.getColor(getContext(), R.color.accent))
                        .sizeDp(56),
                null, null);
        mEmptyView.setCompoundDrawablePadding(DisplayUtil.dip2px(getContext(), 16));

        initData();
        return view;
    }

    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onShow() {
        initData();
    }

    @Override
    public void onToolbarDoubleTap() {
        super.onToolbarDoubleTap();
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    protected void refreshUI() {
        super.refreshUI();
        mAdapter.setNightMode(MHApp.getInstance().getPrefs().getThemeNightMode().getValue());

        mEmptyView.setTextColor(
                ResourceUtil.getColor(getContext(), StyleUtil.getAccentColorRes(getContext())));

        mEmptyView.setCompoundDrawables(null,
                new IconicsDrawable(getContext())
                        .icon(GoogleMaterial.Icon.gmd_favorite)
                        .color(ResourceUtil
                                .getColor(getContext(), StyleUtil.getAccentColorRes(getContext())))
                        .sizeDp(56),
                null, null);
    }

    public void initData() {
        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                e.onNext(mProjectDBHelper.getLinks());
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> list) throws Exception {
                        if (list != null && list.size() > 0) {
                            mAdapter.setData(list);
                            mEmptyView.setVisibility(View.INVISIBLE);
                        } else {
                            mEmptyView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

}
