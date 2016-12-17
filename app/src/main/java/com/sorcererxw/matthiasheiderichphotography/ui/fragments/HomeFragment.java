package com.sorcererxw.matthiasheiderichphotography.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sorcererxw.matthiasheiderichphotography.models.LibraryBean;
import com.sorcererxw.matthiasheiderichphotography.ui.adapters.LibAdapter;
import com.sorcererxw.matthiasheiderichphotography.util.ThemeHelper;
import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/22
 */
public class HomeFragment extends BaseFragment {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @BindView(R.id.nestedScrollView_home)
    NestedScrollView mScrollView;

    @BindView(R.id.textView_home_introduce)
    TextView mIntroduce;

    @BindView(R.id.textView_home_project)
    TextView mProject;

    @BindView(R.id.cardView_home_lib)
    CardView mLibCard;

    @BindView(R.id.cardView_home_introduce)
    CardView mIntroduceCard;

    @BindView(R.id.cardView_home_project)
    CardView mProjectCard;

    @BindView(R.id.recyclerView_fragment_home_lib)
    RecyclerView mLibRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        mIntroduce.setTypeface(TypefaceHelper.getTypeface(getContext(), TypefaceHelper.Type.Book));
        mIntroduce.setText(Html.fromHtml(
                "A simple client of Matthias Heiderich Photography, more information on <a href='http://www.matthias-heiderich.de'>www.matthias-heiderich.de</a>"));
        mIntroduce.setMovementMethod(LinkMovementMethod.getInstance());

        mProject.setTypeface(TypefaceHelper.getTypeface(getContext(), TypefaceHelper.Type.Book));
        mProject.setText(Html.fromHtml(
                "This project address <a href='https://github.com/sorcererXW/MatthiasHeiderichPhotography'>Github</a>"));
        mProject.setMovementMethod(LinkMovementMethod.getInstance());

        mLibRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mLibRecyclerView.setAdapter(
                new LibAdapter(getActivity(), Arrays.asList(LibraryBean.LIBRARY_BEEN)));
        mLibRecyclerView.setNestedScrollingEnabled(false);

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
    public void refreshUI() {
        if (mLibRecyclerView.getAdapter() != null) {
            mLibRecyclerView.getAdapter().notifyDataSetChanged();
        }

        mIntroduce.setLinkTextColor(ContextCompat
                .getColor(getContext(), ThemeHelper.getPrimaryTextColorRes(getContext())));
        mIntroduce.setTextColor(ContextCompat
                .getColor(getContext(), ThemeHelper.getSecondaryTextColorRes(getContext())));

        mProject.setLinkTextColor(ContextCompat
                .getColor(getContext(), ThemeHelper.getPrimaryTextColorRes(getContext())));
        mProject.setTextColor(ContextCompat
                .getColor(getContext(), ThemeHelper.getSecondaryTextColorRes(getContext())));

        mIntroduceCard.setCardBackgroundColor(
                ContextCompat.getColor(getContext(), ThemeHelper.getCardColorRes(getContext())));
        mLibCard.setCardBackgroundColor(
                ContextCompat.getColor(getContext(), ThemeHelper.getCardColorRes(getContext())));
        mProjectCard.setCardBackgroundColor(
                ContextCompat.getColor(getContext(), ThemeHelper.getCardColorRes(getContext())));

        mProject.setTextColor(ContextCompat
                .getColor(getContext(), ThemeHelper.getSecondaryTextColorRes(getContext())));
        mIntroduce.setTextColor(ContextCompat.getColor(getContext(),
                ThemeHelper.getSecondaryTextColorRes(getContext())));
    }

    @Override
    public void onToolbarDoubleTap() {
        super.onToolbarDoubleTap();
        mScrollView.smoothScrollTo(0, 0);
    }
}
