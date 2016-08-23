package com.sorcererxw.matthiasheidericphotography.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.sorcererxw.matthiasheidericphotography.MHApplication;
import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.matthiasheidericphotography.ui.fragments.HomeFragment;
import com.sorcererxw.matthiasheidericphotography.ui.fragments.MHFragment;
import com.sorcererxw.matthiasheidericphotography.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;

    @BindView(R.id.frameLayout_fragment_container)
    FrameLayout mContainer;

    private Drawer mDrawer;

    private FragmentManager mFragmentManager;

    private Fragment mCurrentFragment;
    private Fragment mLastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        mFragmentManager = getSupportFragmentManager();

        initDrawer();

        showFragment(FRAGMENT_TAG_HOME);
    }

    private void initDrawer() {
        mDrawer = new DrawerBuilder()
                .withCloseOnClick(true)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(new AccountHeaderBuilder()
                        .withActivity(this)
                        .withHeightDp(178)
                        .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER_CROP)
                        .withProfileImagesClickable(false)
                        .withResetDrawerOnProfileListClick(false)
                        .withSelectionListEnabled(false)
                        .withDividerBelowHeader(false)
                        .withSelectionListEnabledForSingleProfile(false)
                        .build())
                .withActivity(this)
                .build();

        List<IDrawerItem> collectionList = new ArrayList<>();
        for (int i = 0; i < MHApplication.PROJECTS_NAME.length; i++) {
            IDrawerItem item = new SecondaryDrawerItem()
                    .withName("        " + StringUtil
                            .handleProjectName(MHApplication.PROJECTS_NAME[i]))
                    .withTag(MHApplication.PROJECTS_NAME[i])
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
                        .withIcon(GoogleMaterial.Icon.gmd_home)
                        .withName("Home"),
                new ExpandableDrawerItem()
                        .withIsExpanded(true)
                        .withSelectable(false)
                        .withName("Collections")
                        .withIcon(GoogleMaterial.Icon.gmd_photo_library)
                        .withSubItems(collectionList)
        );
    }

    private static final String FRAGMENT_TAG_HOME = "Home";

    private void showFragment(String tag) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mCurrentFragment = mFragmentManager
                .findFragmentByTag(tag);
        if (mCurrentFragment == null) {
            switch (tag) {
                case FRAGMENT_TAG_HOME:
                    mCurrentFragment = HomeFragment.newInstance();
                    break;
                default:
                    mCurrentFragment = MHFragment.newInstance(tag);
            }
            transaction.add(R.id.frameLayout_fragment_container,
                    mCurrentFragment,
                    tag);
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

        getSupportActionBar().setTitle(StringUtil.handleProjectName(tag));
    }
}
