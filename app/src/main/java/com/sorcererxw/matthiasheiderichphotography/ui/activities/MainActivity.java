package com.sorcererxw.matthiasheiderichphotography.ui.activities;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.BaseFragment;
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.FavoriteFragment;
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.HomeFragment;
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.MHFragment;
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.SettingsFragment;
import com.sorcererxw.matthiasheiderichphotography.util.ResourceUtil;
import com.sorcererxw.matthiasheiderichphotography.util.StringUtil;
import com.sorcererxw.matthiasheiderichphotography.util.StyleUtil;
import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;
import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.typefaceviews.TypefaceToolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_main)
    TypefaceToolbar mToolbar;

    @BindView(R.id.frameLayout_fragment_container)
    FrameLayout mContainer;

    private FragmentManager mFragmentManager;

    private BaseFragment mCurrentFragment;
    private BaseFragment mLastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Boolean isNightMode = MHApp.getInstance().getPrefs().getThemeNightMode().getValue();

        if (isNightMode) {
            setTheme(R.style.NightTheme);
        } else {
            setTheme(R.style.DayTheme);
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        final GestureDetector gestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        if (mCurrentFragment != null) {
                            mCurrentFragment.onToolbarDoubleTap();
                        }
                        return super.onDoubleTap(e);
                    }
                });
        mToolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        mFragmentManager = getSupportFragmentManager();

        initDrawer();

        setTheme(isNightMode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().hasExtra("fragment_tag")) {
            showFragment(getIntent().getStringExtra("fragment_tag"));
        } else {
            showFragment(MHApp.getInstance().getPrefs().getLastLeaveFragmentTag().getValue());
        }
    }

    private Drawer mDrawer;

    private void initDrawer() {

        View head = View.inflate(this, R.layout.layout_drawer_head, null);
        TextView headText = (TextView) head.findViewById(R.id.textView_drawer_head);
        headText.setText("MATTHIAS\nHEIDERICH");
        headText.setTypeface(TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Demi));

        mDrawer = new DrawerBuilder()
                .withCloseOnClick(true)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withHeader(head)
                .withHeaderDivider(false)
                .withHeaderHeight(DimenHolder.fromDp(178))
                .withActivity(this)
                .build();

        List<IDrawerItem> collectionList = new ArrayList<>();
        for (int i = 0; i < MHApp.PROJECTS_NAME.length; i++) {
            IDrawerItem item = new SecondaryDrawerItem()
                    .withName("        " + StringUtil
                            .handleProjectName(MHApp.PROJECTS_NAME[i]))
                    .withTypeface(TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book))
                    .withTag(MHApp.PROJECTS_NAME[i])
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position,
                                                   IDrawerItem drawerItem) {
                            showFragment((String) drawerItem.getTag());
                            return false;
                        }
                    });
            collectionList.add(item);
        }

        mDrawer.addItems(
                new PrimaryDrawerItem()
                        .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position,
                                                       IDrawerItem drawerItem) {
                                showFragment(FRAGMENT_TAG_HOME);
                                return false;
                            }
                        })
                        .withTypeface(TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book))
                        .withIcon(GoogleMaterial.Icon.gmd_home)
                        .withName("Home"),
                new PrimaryDrawerItem().withOnDrawerItemClickListener(
                        new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position,
                                                       IDrawerItem drawerItem) {
                                showFragment(FRAGMENT_TAG_FAVORITE);
                                return false;
                            }
                        })
                        .withTypeface(TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book))
                        .withIcon(GoogleMaterial.Icon.gmd_favorite)
                        .withName("Favorite"),
                new ExpandableDrawerItem()
                        .withTypeface(TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book))
                        .withIsExpanded(false)
                        .withSelectable(false)
                        .withName("Collections")
                        .withIcon(GoogleMaterial.Icon.gmd_photo_library)
                        .withSubItems(collectionList),
                new PrimaryDrawerItem().withOnDrawerItemClickListener(
                        new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position,
                                                       IDrawerItem drawerItem) {
                                showFragment(FRAGMENT_TAG_SETTINGS);
                                return false;
                            }
                        })
                        .withTypeface(TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book))
                        .withIcon(GoogleMaterial.Icon.gmd_settings)
                        .withName("Settings")
        );
    }

    public static final String FRAGMENT_TAG_FAVORITE = "Favorite";
    public static final String FRAGMENT_TAG_HOME = "Home";
    public static final String FRAGMENT_TAG_SETTINGS = "Settings";

    private String mFragmentTag = FRAGMENT_TAG_HOME;

    private void showFragment(String tag) {
        mFragmentTag = tag;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mCurrentFragment = (BaseFragment) mFragmentManager
                .findFragmentByTag(tag);
        if (mCurrentFragment == null) {
            switch (tag) {
                case FRAGMENT_TAG_HOME:
                    mCurrentFragment = HomeFragment.newInstance();
                    break;
                case FRAGMENT_TAG_FAVORITE:
                    mCurrentFragment = FavoriteFragment.newInstance();
                    break;
                case FRAGMENT_TAG_SETTINGS:
                    mCurrentFragment = SettingsFragment.newInstance();
                    break;
                default:
                    mCurrentFragment = MHFragment.newInstance(tag);
            }
            transaction.add(R.id.frameLayout_fragment_container,
                    mCurrentFragment,
                    tag);
        } else {
            mCurrentFragment.onShow();
        }

        if (mLastFragment != null) {
            transaction.hide(mLastFragment);
        }
        if (mCurrentFragment.isDetached()) {
            transaction.attach(mCurrentFragment);
        }
        transaction.show(mCurrentFragment);
        mLastFragment = mCurrentFragment;
        transaction.commit();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(StringUtil.handleProjectName(tag));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MHApp.getInstance().getPrefs().getLastLeaveFragmentTag().setValue(mFragmentTag);
    }

    public void setTheme(boolean isNightMode) {
        if (isNightMode) {
            setTheme(R.style.NightTheme);
        } else {
            setTheme(R.style.DayTheme);
        }
        refreshUI(isNightMode);
        refreshStatusBar(isNightMode);

        try {
            refreshFragments();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshUI(boolean isNightMode) {
        Resources.Theme theme = getTheme();
        TypedValue textPrimary = new TypedValue();
        TypedValue textSecondary = new TypedValue();
        TypedValue accent = new TypedValue();
        TypedValue primary = new TypedValue();
        TypedValue primaryDark = new TypedValue();
        TypedValue background = new TypedValue();

        theme.resolveAttribute(R.attr.colorPrimaryText, textPrimary, true);
        theme.resolveAttribute(R.attr.colorBackground, background, true);
        theme.resolveAttribute(R.attr.colorPrimary, primary, true);
        theme.resolveAttribute(R.attr.colorPrimaryDark, primaryDark, true);
        theme.resolveAttribute(R.attr.colorSecondaryText, textSecondary, true);
        theme.resolveAttribute(R.attr.colorAccent, accent, true);

        mContainer.setBackgroundResource(background.resourceId);

        ((TextView) mDrawer.getHeader().findViewById(R.id.textView_drawer_head))
                .setTextColor(ContextCompat.getColor(this, textPrimary.resourceId));
        mDrawer.getHeader().setBackgroundResource(background.resourceId);
        mDrawer.getSlider().setBackgroundResource(background.resourceId);

        for (IDrawerItem drawerItem : mDrawer.getDrawerItems()) {
            if (drawerItem instanceof PrimaryDrawerItem) {
                PrimaryDrawerItem primaryDrawerItem = (PrimaryDrawerItem) drawerItem;
                primaryDrawerItem.withSelectedColorRes(primaryDark.resourceId);
                primaryDrawerItem.withTextColorRes(textSecondary.resourceId);
                primaryDrawerItem.withSelectedTextColorRes(textPrimary.resourceId);

                primaryDrawerItem.withIconColorRes(textSecondary.resourceId);
                primaryDrawerItem.withSelectedIconColorRes(textPrimary.resourceId);
            } else if (drawerItem instanceof ExpandableDrawerItem) {
                ExpandableDrawerItem expandableDrawerItem = (ExpandableDrawerItem) drawerItem;
                expandableDrawerItem.withTextColorRes(textSecondary.resourceId);
                for (IDrawerItem subDrawable : expandableDrawerItem.getSubItems()) {
                    if (subDrawable instanceof PrimaryDrawerItem) {
                        PrimaryDrawerItem primarySubDrawerItem = (PrimaryDrawerItem) subDrawable;
                        primarySubDrawerItem.withTextColorRes(textSecondary.resourceId);
                        primarySubDrawerItem.withSelectedTextColorRes(textPrimary.resourceId);
                        primarySubDrawerItem.withSelectedColorRes(primaryDark.resourceId);
                    }
                }
                expandableDrawerItem.withIconColorRes(textSecondary.resourceId);
                expandableDrawerItem.withSelectedIconColorRes(textPrimary.resourceId);
            }
        }

        mToolbar.setBackgroundResource(primary.resourceId);
        mToolbar.setSubtitleTextColor(ContextCompat.getColor(this, textSecondary.resourceId));
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, textPrimary.resourceId));

        mDrawer.getActionBarDrawerToggle().getDrawerArrowDrawable()
                .setColor(ResourceUtil.getColor(this, textPrimary.resourceId));
        mDrawer.getAdapter().notifyDataSetChanged();
    }

    private void refreshStatusBar(boolean isNightMode) {

        getWindow().setStatusBarColor(
                ContextCompat.getColor(this, StyleUtil.getPrimaryDarkColorRes(this)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isNightMode) {
                mToolbar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            } else {
                mToolbar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    private void refreshFragments() {
        FragmentManager fm = getSupportFragmentManager();

        for (Fragment fragment : fm.getFragments()) {
            if (fragment != null) {
                ((BaseFragment) fragment).onThemeChanged();
            }
        }
    }
}
