package com.sorcererxw.matthiasheiderichphotography.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sorcererxw.matthiasheidericphotography.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    /*
     private lateinit var model: HomeViewmodel

    private lateinit var mRoot: LinearLayout
    private lateinit var mToolbar: TypefaceToolbar
    private lateinit var mContainer: FrameLayout
    private lateinit var mDrawer: Drawer

    private var mFragmentManager: androidx.fragment.app.FragmentManager? = null

    private var mCurrentFragment: MHFragment? = null
    private var mLastFragment: MHFragment? = null

    private var mHomeDrawerItem: PrimaryDrawerItem? = null
    private var mSettingsDrawerItem: PrimaryDrawerItem? = null
    private var mFavoriteDrawerItem: PrimaryDrawerItem? = null
    private var mCollectionsDrawerItemList: MutableList<IDrawerItem<*, *>>? = null

    private val galleryItemIdentity = 0x2333L

    private var mFragmentTag: String = ""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProviders.of(this).get(HomeViewmodel::class.java)

        val isNightMode = AppPref.getInstance(this).themeNightMode.get()

        if (isNightMode) {
            setTheme(R.style.NightTheme)
        } else {
            setTheme(R.style.DayTheme)
        }

        setContentView(R.layout.activity_main)

        mRoot = findViewById(R.id.linearLayout_main_root)
        mToolbar = findViewById(R.id.toolbar_main)
        mContainer = findViewById(R.id.frameLayout_fragment_container)

        setSupportActionBar(mToolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        }

        val gestureDetector = GestureDetector(this,
                object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDoubleTap(e: MotionEvent): Boolean {
                        if (mCurrentFragment != null) {
                            mCurrentFragment!!.onToolbarDoubleTap()
                        }
                        return super.onDoubleTap(e)
                    }
                })
        mToolbar.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        mFragmentManager = supportFragmentManager

        initDrawer()

        setTheme(isNightMode)
    }

    override fun onStart() {
        super.onStart()
        if (intent.hasExtra("fragment_tag")) {
            showFragment(intent.getStringExtra("fragment_tag"))
        } else {
            showFragment(AppPref.getInstance(this).lastLeaveFragmentTag.get())
        }
    }

    private fun initDrawer() {

        val head = View.inflate(this, R.layout.layout_drawer_head, null)
        val headText = head.findViewById(R.id.textView_drawer_head) as TextView
        headText.text = "MATTHIAS\nHEIDERICH"
        headText.typeface = TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Demi)

        mDrawer = DrawerBuilder()
                .withCloseOnClick(true)
                .withToolbar(mToolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withHeader(head)
                .withHeaderDivider(false)
                .withHeaderHeight(DimenHolder.fromDp(178))
                .withActivity(this)
                .build()

        mHomeDrawerItem = homeDrawerItem()
        mFavoriteDrawerItem = favoriteDrawerItem()
        mSettingsDrawerItem = settingsDrawerItem()

        mDrawer.addItems(
                mHomeDrawerItem,
                mFavoriteDrawerItem,
                galleryItem(emptyList()),
                mSettingsDrawerItem
        )

        model.galleryList.observe(this, Observer {
            mDrawer.removeItem(galleryItemIdentity)
            mDrawer.addItemAtPosition(galleryItem(it), 2)
        })
    }

    private fun galleryItem(galleryList: List<GalleryEntity>): ExpandableDrawerItem {
        val fragmentTag = AppPref.getInstance(this).lastLeaveFragmentTag.get()
        val subItemList = galleryList.map {
            SecondaryDrawerItem()
                    .withName("        " + it.title)
                    .withTypeface(TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book))
                    .withTag(it.name)
                    .withOnDrawerItemClickListener { view, position, drawerItem ->
                        showFragment(drawerItem.tag as String)
                        false
                    }
        }
        return ExpandableDrawerItem()
                .withTypeface(TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book))
                .withIsExpanded(isCollectionTag(fragmentTag))
                .withSelectable(false)
                .withName("Collections")
                .withIcon(GoogleMaterial.Icon.gmd_photo_library)
                .withIdentifier(galleryItemIdentity)
                .withSubItems(subItemList)
    }

    private fun homeDrawerItem(): PrimaryDrawerItem {
        return PrimaryDrawerItem()
                .withOnDrawerItemClickListener { view, position, drawerItem ->
                    showFragment(FRAGMENT_TAG_HOME)
                    false
                }
                .withTypeface(TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book))
                .withIcon(GoogleMaterial.Icon.gmd_home)
                .withName("Home")
    }

    private fun favoriteDrawerItem(): PrimaryDrawerItem {
        return PrimaryDrawerItem().withOnDrawerItemClickListener { view, position, drawerItem ->
            showFragment(FRAGMENT_TAG_FAVORITE)
            false
        }
                .withTypeface(TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book))
                .withIcon(GoogleMaterial.Icon.gmd_favorite)
                .withName("Favorite")
    }

    private fun settingsDrawerItem(): PrimaryDrawerItem {
        return PrimaryDrawerItem().withOnDrawerItemClickListener { view, position, drawerItem ->
            showFragment(FRAGMENT_TAG_SETTINGS)
            false
        }
                .withTypeface(TypefaceHelper.getTypeface(this, TypefaceHelper.Type.Book))
                .withIcon(GoogleMaterial.Icon.gmd_settings)
                .withName("Settings")
    }


    fun showFragment(tag: String) {
        if (tag == mFragmentTag) {
            return
        }
        mFragmentTag = tag
        val transaction = mFragmentManager!!.beginTransaction()
        mCurrentFragment = mFragmentManager!!
                .findFragmentByTag(tag) as BaseFragment?
        if (mCurrentFragment == null) {
            when (tag) {
                FRAGMENT_TAG_HOME -> {
                    mDrawer.setSelection(mHomeDrawerItem!!, false)
                    mCurrentFragment = AboutFragment.newInstance()
                }
                FRAGMENT_TAG_FAVORITE -> {
                    mDrawer.setSelection(mFavoriteDrawerItem!!, false)
                    mCurrentFragment = FavoriteFragment.newInstance()
                }
                FRAGMENT_TAG_SETTINGS -> {
                    mDrawer.setSelection(mSettingsDrawerItem!!, false)
                    mCurrentFragment = SettingsFragment.newInstance()
                }
                else -> {
                    for (item in mCollectionsDrawerItemList!!) {
                        if (tag == item.tag) {
                            mDrawer.setSelection(item, false)
                        }
                    }
                    mCurrentFragment = GalleryFragment.newInstance(tag)
                }
            }
            transaction.add(R.id.frameLayout_fragment_container, mCurrentFragment as Fragment, tag)
        } else {
            mCurrentFragment!!.onShow()
        }

        if (mLastFragment != null) {
            transaction.hide(mLastFragment as Fragment)
        }
        if ((mCurrentFragment as Fragment).isDetached) {
            transaction.attach(mCurrentFragment as Fragment)
        }
        transaction.show(mCurrentFragment as Fragment)
        mLastFragment = mCurrentFragment
        transaction.commit()
        if (supportActionBar != null) {
            supportActionBar!!.title = tag
        }
    }

    override fun onStop() {
        super.onStop()
        AppPref.getInstance(this).lastLeaveFragmentTag.set(mFragmentTag)
    }

    fun setTheme(isNightMode: Boolean) {
        if (isNightMode) {
            setTheme(R.style.NightTheme)
        } else {
            setTheme(R.style.DayTheme)
        }
        refreshUI(isNightMode)
        refreshStatusBar(isNightMode)

        try {
            refreshFragments()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun refreshUI(isNightMode: Boolean) {
        val colorPrimaryText = ResourceUtil.getColor(this, ThemeHelper.getPrimaryTextColorRes(this))
        val colorBackground = ResourceUtil.getColor(this, ThemeHelper.getBackgroundColorRes(this))
        val colorPrimary = ResourceUtil.getColor(this, ThemeHelper.getPrimaryColorRes(this))
        val colorPrimaryDark = ResourceUtil.getColor(this, ThemeHelper.getPrimaryDarkColorRes(this))
        val colorSecondaryText = ResourceUtil.getColor(this,
                ThemeHelper.getSecondaryTextColorRes(this))

        mContainer.setBackgroundColor(colorBackground)

        (mDrawer.header.findViewById(R.id.textView_drawer_head) as TextView)
                .setTextColor(colorPrimaryText)
        mDrawer.header.setBackgroundColor(colorBackground)
        mDrawer.slider.setBackgroundColor(colorBackground)
        mDrawer.recyclerView.isHorizontalScrollBarEnabled = false
        mDrawer.recyclerView.isVerticalScrollBarEnabled = false

        for (drawerItem in mDrawer.drawerItems) {
            if (drawerItem is PrimaryDrawerItem) {
                drawerItem.withSelectedColor(colorPrimaryDark)
                drawerItem.withTextColor(colorSecondaryText)
                drawerItem.withSelectedTextColor(colorPrimaryText)
                drawerItem.withIconColor(colorSecondaryText)
                drawerItem.withSelectedIconColor(colorPrimaryText)
            } else if (drawerItem is ExpandableDrawerItem) {
                drawerItem.withTextColor(colorSecondaryText)
                for (subDrawable in drawerItem.subItems) {
                    if (subDrawable is BaseDrawerItem<*, *>) {
                        subDrawable.withTextColor(colorSecondaryText)
                        subDrawable.withSelectedTextColor(colorPrimaryText)
                        subDrawable.withSelectedColor(colorPrimaryDark)
                    }
                }
                drawerItem.withIconColor(colorSecondaryText)
                drawerItem.withSelectedIconColor(colorPrimaryText)
            }
        }

        mToolbar.setBackgroundColor(colorPrimary)
        mToolbar.setSubtitleTextColor(colorSecondaryText)
        mToolbar.setTitleTextColor(colorPrimaryText)

        mDrawer.actionBarDrawerToggle.drawerArrowDrawable.color = colorPrimaryText
        mDrawer.adapter.notifyDataSetChanged()
    }

    private fun refreshStatusBar(isNightMode: Boolean) {
        mRoot.setBackgroundColor(ThemeHelper.getPrimaryDarkColor(this))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isNightMode) {
                mToolbar.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            } else {
                mToolbar.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    private fun refreshFragments() {
        val fm = supportFragmentManager

        for (fragment in fm.fragments) {
            if (fragment != null) {
                (fragment as BaseFragment).onThemeChanged()
            }
        }
    }

    companion object {

        const val FRAGMENT_TAG_FAVORITE = "Favorite"
        const val FRAGMENT_TAG_HOME = "Home"
        const val FRAGMENT_TAG_SETTINGS = "Settings"

        private fun isCollectionTag(tag: String): Boolean {
            return !(FRAGMENT_TAG_HOME == tag
                    || FRAGMENT_TAG_FAVORITE == tag
                    || FRAGMENT_TAG_SETTINGS == tag)
        }
    }
     */
}
