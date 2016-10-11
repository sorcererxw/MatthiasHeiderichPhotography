package com.sorcererxw.matthiasheiderichphotography.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.Iconics;
import com.mikepenz.iconics.IconicsDrawable;
import com.sorcererxw.matthiasheiderichphotography.ui.adapters.MHAdapter;
import com.sorcererxw.matthiasheiderichphotography.ui.others.LinerMarginDecoration;
import com.sorcererxw.matthiasheiderichphotography.util.DisplayUtil;
import com.sorcererxw.matthiasheiderichphotography.util.ProjectDBHelper;
import com.sorcererxw.matthiasheiderichphotography.util.ResourceUtil;
import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;
import com.sorcererxw.matthiasheidericphotography.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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

        mAdapter = new MHAdapter(getContext());
        mAdapter.setOnItemLongClickListener(new MHAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(View view, final String data, final int position) {
                new MaterialDialog.Builder(getContext())
                        .typeface(
                                TypefaceHelper.getTypeface(getContext(), TypefaceHelper.Type.Demi),
                                TypefaceHelper.getTypeface(getContext(), TypefaceHelper.Type.Book))
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onShow() {
        initData();
    }

    public void initData() {
        Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                subscriber.onNext(mProjectDBHelper.getLinks());
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> list) {
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
