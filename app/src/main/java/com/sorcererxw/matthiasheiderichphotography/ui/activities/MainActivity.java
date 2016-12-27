package com.sorcererxw.matthiasheiderichphotography.ui.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.model.BaseDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.data.Project;
import com.sorcererxw.matthiasheiderichphotography.data.db.ProjectTable;
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.BaseFragment;
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.FavoriteFragment;
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.HomeFragment;
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.MHFragment;
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.SettingsFragment;
import com.sorcererxw.matthiasheiderichphotography.util.ResourceUtil;
import com.sorcererxw.matthiasheiderichphotography.util.StringUtil;
import com.sorcererxw.matthiasheiderichphotography.util.ThemeHelper;
import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;
import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.typefaceviews.widgets.TypefaceToolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.linearLayout_main_root)
    LinearLayout mRoot;

    @BindView(R.id.toolbar_main)
    TypefaceToolbar mToolbar;

    @BindView(R.id.frameLayout_fragment_container)
    FrameLayout mContainer;

    private FragmentManager mFragmentManager;

    private BaseFragment mCurrentFragment;
    private BaseFragment mLastFragment;

    private PrimaryDrawerItem mHomeDrawerItem;
    private PrimaryDrawerItem mSettingsDrawerItem;
    private PrimaryDrawerItem mFavoriteDrawerItem;
    private ExpandableDrawerItem mCollectionsExpandableDrawerItem;
    private List<IDrawerItem> mCollectionsDrawerItemList;

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

    public static final String FRAGMENT_TAG_FAVORITE = "Favorite";
    public static final String FRAGMENT_TAG_HOME = "Home";
    public static final String FRAGMENT_TAG_SETTINGS = "Settings";

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

        mCollectionsDrawerItemList = new ArrayList<>();
        for (Project project : Project.values()) {
            IDrawerItem item = new SecondaryDrawerItem()
                    .withName("        " + project.toCollectionName())
                    .withTypeface(TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book))
                    .withTag(project.toString())
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position,
                                                   IDrawerItem drawerItem) {
                            showFragment((String) drawerItem.getTag());
                            return false;
                        }
                    });
            mCollectionsDrawerItemList.add(item);
        }

        String fragmentTag = MHApp.getInstance().getPrefs().getLastLeaveFragmentTag().getValue();
        boolean unexpandedCategories = !isCollectionTag(fragmentTag);

        mHomeDrawerItem = homeDrawerItem();
        mFavoriteDrawerItem = favoriteDrawerItem();
        mSettingsDrawerItem = settingsDrawerItem();
        mCollectionsExpandableDrawerItem = collectionsDrawerItem(!unexpandedCategories);

        mDrawer.addItems(
                mHomeDrawerItem,
                mFavoriteDrawerItem,
                mCollectionsExpandableDrawerItem,
                mSettingsDrawerItem
        );
    }

    private static boolean isCollectionTag(String tag) {
        return !(FRAGMENT_TAG_HOME.equals(tag)
                || FRAGMENT_TAG_FAVORITE.equals(tag)
                || FRAGMENT_TAG_SETTINGS.equals(tag));
    }

    private PrimaryDrawerItem homeDrawerItem() {
        return new PrimaryDrawerItem()
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
                .withName("Home");
    }

    private PrimaryDrawerItem favoriteDrawerItem() {
        return new PrimaryDrawerItem().withOnDrawerItemClickListener(
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
                .withName("Favorite");
    }

    private PrimaryDrawerItem settingsDrawerItem() {
        return new PrimaryDrawerItem().withOnDrawerItemClickListener(
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
                .withName("Settings");
    }

    private ExpandableDrawerItem collectionsDrawerItem(boolean expandedCategories) {
        return new ExpandableDrawerItem()
                .withTypeface(TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book))
                .withIsExpanded(expandedCategories)
                .withSelectable(false)
                .withName("Collections")
                .withIcon(GoogleMaterial.Icon.gmd_photo_library)
                .withSubItems(mCollectionsDrawerItemList);
    }

    private String mFragmentTag;

    public void showFragment(String tag) {
        if (tag.equals(mFragmentTag)) {
            return;
        }
        mFragmentTag = tag;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mCurrentFragment = (BaseFragment) mFragmentManager
                .findFragmentByTag(tag);
        if (mCurrentFragment == null) {
            switch (tag) {
                case FRAGMENT_TAG_HOME:
                    mDrawer.setSelection(mHomeDrawerItem, false);
                    mCurrentFragment = HomeFragment.newInstance();
                    break;
                case FRAGMENT_TAG_FAVORITE:
                    mDrawer.setSelection(mFavoriteDrawerItem, false);
                    mCurrentFragment = FavoriteFragment.newInstance();
                    break;
                case FRAGMENT_TAG_SETTINGS:
                    mDrawer.setSelection(mSettingsDrawerItem, false);
                    mCurrentFragment = SettingsFragment.newInstance();
                    break;
                default:
                    for (IDrawerItem item : mCollectionsDrawerItemList) {
                        if (tag.equals(item.getTag())) {
                            mDrawer.setSelection(item, false);
                        }
                    }
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
            if (isCollectionTag(tag)) {
                getSupportActionBar().setTitle(Project.valueOf(tag).toCollectionName());
            } else {
                getSupportActionBar().setTitle(tag);
            }
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
        int colorPrimaryText =
                ResourceUtil.getColor(this, ThemeHelper.getPrimaryTextColorRes(this));
        int colorBackground = ResourceUtil.getColor(this, ThemeHelper.getBackgroundColorRes(this));
        int colorPrimary = ResourceUtil.getColor(this, ThemeHelper.getPrimaryColorRes(this));
        int colorPrimaryDark =
                ResourceUtil.getColor(this, ThemeHelper.getPrimaryDarkColorRes(this));
        int colorSecondaryText =
                ResourceUtil.getColor(this, ThemeHelper.getSecondaryTextColorRes(this));

        mContainer.setBackgroundColor(colorBackground);

        ((TextView) mDrawer.getHeader().findViewById(R.id.textView_drawer_head))
                .setTextColor(colorPrimaryText);
        mDrawer.getHeader().setBackgroundColor(colorBackground);
        mDrawer.getSlider().setBackgroundColor(colorBackground);
        mDrawer.getRecyclerView().setHorizontalScrollBarEnabled(false);
        mDrawer.getRecyclerView().setVerticalScrollBarEnabled(false);

        for (IDrawerItem drawerItem : mDrawer.getDrawerItems()) {
            if (drawerItem instanceof PrimaryDrawerItem) {
                PrimaryDrawerItem primaryDrawerItem = (PrimaryDrawerItem) drawerItem;
                primaryDrawerItem.withSelectedColor(colorPrimaryDark);
                primaryDrawerItem.withTextColor(colorSecondaryText);
                primaryDrawerItem.withSelectedTextColor(colorPrimaryText);
                primaryDrawerItem.withIconColor(colorSecondaryText);
                primaryDrawerItem.withSelectedIconColor(colorPrimaryText);
            } else if (drawerItem instanceof ExpandableDrawerItem) {
                ExpandableDrawerItem expandableDrawerItem = (ExpandableDrawerItem) drawerItem;
                expandableDrawerItem.withTextColor(colorSecondaryText);
                for (IDrawerItem subDrawable : expandableDrawerItem.getSubItems()) {
                    if (subDrawable instanceof BaseDrawerItem) {
                        BaseDrawerItem subDrawerItem = (BaseDrawerItem) subDrawable;
                        subDrawerItem.withTextColor(colorSecondaryText);
                        subDrawerItem.withSelectedTextColor(colorPrimaryText);
                        subDrawerItem.withSelectedColor(colorPrimaryDark);
                    }
                }
                expandableDrawerItem.withIconColor(colorSecondaryText);
                expandableDrawerItem.withSelectedIconColor(colorPrimaryText);
            }
        }

        mToolbar.setBackgroundColor(colorPrimary);
        mToolbar.setSubtitleTextColor(colorSecondaryText);
        mToolbar.setTitleTextColor(colorPrimaryText);

        mDrawer.getActionBarDrawerToggle().getDrawerArrowDrawable().setColor(colorPrimaryText);
        mDrawer.getAdapter().notifyDataSetChanged();
    }

    private void refreshStatusBar(boolean isNightMode) {
        mRoot.setBackgroundColor(ThemeHelper.getPrimaryDarkColor(this));

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
