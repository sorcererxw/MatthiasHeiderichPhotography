package com.sorcererxw.matthiasheiderichphotography.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
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
import com.sorcererxw.matthiasheiderichphotography.MHApplication;
import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.HomeFragment;
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.MHFragment;
import com.sorcererxw.matthiasheiderichphotography.ui.views.TypefaceToolbar;
import com.sorcererxw.matthiasheiderichphotography.util.StringUtil;
import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_main)
    TypefaceToolbar mToolbar;

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

        if (savedInstanceState != null) {
            showFragment(savedInstanceState.getString("tag"));
        } else {
            showFragment(FRAGMENT_TAG_HOME);
        }
    }

    private void initDrawer() {

        View head = LayoutInflater.from(this).inflate(R.layout.layout_drawer_head, null);
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
        for (int i = 0; i < MHApplication.PROJECTS_NAME.length; i++) {
            IDrawerItem item = new SecondaryDrawerItem()
                    .withName("        " + StringUtil
                            .handleProjectName(MHApplication.PROJECTS_NAME[i]))
                    .withTypeface(TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book))
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
                        .withTypeface(TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book))
                        .withIcon(GoogleMaterial.Icon.gmd_home)
                        .withName("Home"),
                new ExpandableDrawerItem()
                        .withTypeface(TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book))
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(StringUtil.handleProjectName(tag));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tag", mCurrentFragment.getTag());
    }


}