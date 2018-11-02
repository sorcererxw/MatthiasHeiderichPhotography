package com.sorcererxw.matthiasheiderichphotography.ui.activities

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.transition.Transition
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.palette.graphics.Palette
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.list.ItemListener
import com.afollestad.materialdialogs.list.listItems
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.google.android.material.snackbar.Snackbar
import com.sorcererxw.matthiasheiderichphotography.MHApp
import com.sorcererxw.matthiasheiderichphotography.config.GlideApp
import com.sorcererxw.matthiasheiderichphotography.ui.others.Dialogs
import com.sorcererxw.matthiasheiderichphotography.ui.others.FilePickerSheetView
import com.sorcererxw.matthiasheiderichphotography.ui.others.SimpleTransitionListener
import com.sorcererxw.matthiasheiderichphotography.util.*
import com.sorcererxw.matthiasheidericphotography.BuildConfig
import com.sorcererxw.matthiasheidericphotography.R
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wang.avi.AVLoadingIndicatorView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import uk.co.senab.photoview.PhotoView
import uk.co.senab.photoview.PhotoViewAttacher
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.ExecutionException

private class DetailActivity : AppCompatActivity() {
    private lateinit var mCoordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout
    private lateinit var mToolbar: Toolbar
    private lateinit var mImageView: PhotoView
    private lateinit var mFAB: FloatingActionMenu
    private lateinit var mApplyFAB: FloatingActionButton
    private lateinit var mSaveFAB: FloatingActionButton
    private lateinit var mIndicatorView: AVLoadingIndicatorView
    private lateinit var mWallpaperSetter: WallpaperSetter

    private var mLink: String? = null
    private var mUri: Uri? = null

    private var mProgressDialog: MaterialDialog? = null

    private val mSnackbarCallback = object : com.google.android.material.snackbar.Snackbar.Callback() {
        override fun onDismissed(snackbar: com.google.android.material.snackbar.Snackbar?,
                                 event: Int) {
            super.onDismissed(snackbar, event)
            mFAB.animate().translationY(0f).setDuration(200).start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (AppPref.getInstance(this).themeNightMode.get()) {
            setTheme(R.style.NightTheme)
        } else {
            setTheme(R.style.DayTheme)
        }
//        setContentView(R.layout.activity_detail)

//        mCoordinatorLayout = findViewById(R.id.coordinatorLayout_detail)
//        mToolbar = findViewById(R.id.toolbar_detail)
//        mImageView = findViewById(R.id.imageView_detail)
//        mFAB = findViewById(R.id.fab_detail)
//        mApplyFAB = findViewById(R.id.fab_detail_apply)
//        mSaveFAB = findViewById(R.id.fab_detail_save)
//        mIndicatorView = findViewById(R.id.loadingIndicator_item)

        mWallpaperSetter = WallpaperSetter(this)

        mApplyFAB.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 24) {
                MaterialDialog(this).listItems(
                        items = listOf("Home Screen", "Lock Screen", "Home And Lock Screen"),
                        selection = object : ItemListener {
                            override fun invoke(dialog: MaterialDialog, index: Int,
                                                text: String) {
                                if (text.contains("Home") && text.contains("Lock")) {
                                    setWallpapers(true, true)
                                } else if (text.contains("Home")) {
                                    setWallpapers(true, false)
                                } else {
                                    setWallpapers(false, true)
                                }
                            }
                        }).show()
            } else {
                setWallpapers(false, false)
            }
        }

        mApplyFAB.setOnLongClickListener {
            setWallpapersMore()
            true
        }

        mSaveFAB.setOnClickListener {
            saveToLocal()
        }
        mSaveFAB.setOnLongClickListener {
            showFileChooser()
            true
        }

        mLink = intent.getStringExtra("link")
        initImage()

//        mIndicatorView.setIndicatorColor(ThemeHelper.getAccentColor(this))
//        mToolbar.setHasText(false)
        setSupportActionBar(mToolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        mFAB.hideMenuButton(false)
    }

    private fun initImage() {
        mImageView.setOnSingleFlingListener { e1, e2, velocityX, velocityY -> false }
        mImageView.setOnViewTapListener(PhotoViewAttacher.OnViewTapListener { view, x, y ->
            if (mFAB.isOpened) {
                mFAB.close(true)
                return@OnViewTapListener
            }
            if (mFAB.isMenuButtonHidden) {
                mFAB.showMenuButton(true)
                mFAB.isClickable = true
                mToolbar.animate().alpha(1f).start()
            } else {
                mFAB.hideMenuButton(true)
                mFAB.isClickable = false
                mToolbar.animate().alpha(0f).start()
            }
        })
        mImageView.scaleType = ImageView.ScaleType.CENTER_CROP

        if (MHApp.getTmpDrawable(this) != null) {
            mImageView.setImageDrawable(MHApp.getTmpDrawable(this))
        }
        window.sharedElementEnterTransition.addListener(object : SimpleTransitionListener() {
            override fun onTransitionEnd(transition: Transition) {
                Observable.just<String>(mLink)
                        .map { s ->
                            GlideApp.with(this@DetailActivity)
                                    .load(s)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .submit(-1, -1)
                                    .get()
                        }
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<Drawable> {
                            override fun onComplete() {}

                            override fun onSubscribe(d: Disposable) {}

                            override fun onNext(drawable: Drawable) {
                                mImageView.setImageDrawable(drawable)
                                setupFAB()
                            }

                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                            }
                        })
            }
        })
    }

    private fun requestPermission(): Observable<Boolean> {
        return RxPermissions(this)
                .request(WRITE_EXTERNAL_STORAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .filter { granted ->
                    if (!granted) {
                        Snackbar.make(mCoordinatorLayout, "No permission :(", Snackbar.LENGTH_LONG)
                                .setCallback(mSnackbarCallback).show()
                    }
                    granted
                }
    }

    private fun download(): Observable<Uri> {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val dir = File(path, "/Matthisa Heiderich")
        if (!dir.exists()) {
            dir.mkdir()
        }
        return download(dir)
    }

    private fun download(path: File?): Observable<Uri> {
        return Observable.just<String>(mLink).map {
            GlideApp.with(this@DetailActivity)
                    .asBitmap()
                    .load(mLink)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .submit(-1, -1)
                    .get()
        }
                .subscribeOn(Schedulers.io()).map { bitmap ->
                    var uri: Uri? = null
                    var out: FileOutputStream? = null
                    try {
                        val file = File(path,
                                StringUtil.getFileNameFromLinkWithoutExtension(mLink!!) + ".png")
                        if (file.exists()) {
                            uri = Uri.fromFile(file)
                        } else {
                            out = FileOutputStream(file)
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                            uri = Uri.fromFile(file)
                        }
                    } catch (e: Exception) {
                        if (BuildConfig.DEBUG) {
                            e.printStackTrace()
                        }
                    } finally {
                        try {
                            out?.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                    uri
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map { uri ->
                    mUri = uri
                    uri
                }
    }

    private fun saveToLocal() {
        requestPermission().subscribe(object : Observer<Boolean> {
            override fun onComplete() {}

            override fun onSubscribe(d: Disposable) {}

            override fun onNext(t: Boolean) {
                val path = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val dir = File(path, "/Matthisa Heiderich")
                if (!dir.exists()) {
                    dir.mkdir()
                }
                saveToLocal(dir)
            }

            override fun onError(e: Throwable) {}
        })
    }

    private fun saveToLocal(path: File?) {
        requestPermission().filter {
            if (mUri != null) {
                Snackbar.make(mCoordinatorLayout, "Existed", Snackbar.LENGTH_LONG)
                        .setAction("Open") { openOuterPhotoViewer(mUri) }
                        .setActionTextColor(ResourceUtil
                                .getColor(this@DetailActivity, R.color.white))
                        .setCallback(mSnackbarCallback)
                        .show()
            }
            mUri == null
        }.flatMap {
            showDialog("Saving To Local...")
            download(path)
        }.doOnNext {
            dismissDialog()
        }.subscribe(object : Observer<Uri> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(uri: Uri) {
                Snackbar.make(mCoordinatorLayout, "Success", Snackbar.LENGTH_LONG)
                        .setAction("Open") { openOuterPhotoViewer(uri) }
                        .setActionTextColor(
                                ResourceUtil.getColor(this@DetailActivity, R.color.white))
                        .setCallback(mSnackbarCallback)
                        .show()
            }

            override fun onError(e: Throwable) {
            }

        })
    }

    private fun setWallpapers(homeScreen: Boolean, lockScreen: Boolean) {
        requestPermission()
                .filter {
                    showDialog("Setting wallpaper...")
                    if (mUri == null) {
                        download().subscribe { setWallpapers(homeScreen, lockScreen) }
                    }
                    mUri != null
                }
                .flatMap { Observable.just(mUri) }.observeOn(Schedulers.newThread())
                .map {
                    try {
                        val wallpaperManager = WallpaperManager.getInstance(
                                applicationContext)
                        wallpaperManager.setWallpaperOffsets(
                                window.decorView.rootView.windowToken, 0.5f, 1f)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            if (lockScreen) {
                                mWallpaperSetter!!.setLockScreenWallpaper(it)
                            }
                            if (homeScreen) {
                                mWallpaperSetter!!.setSystemWallpaper(it)
                            }
                        }
                        if (!homeScreen && !lockScreen) {
                            mWallpaperSetter!!.setWallpaperSimple(windowManager, it)
                        }
                        return@map true
                    } catch (e: IOException) {
                        Timber.e(e)
                    }
                    false
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Boolean> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: Boolean) {
                        if (t) {
                            val snackbar = Snackbar.make(mCoordinatorLayout, "Success",
                                    Snackbar.LENGTH_LONG)
                                    .setCallback(mSnackbarCallback)
                            if (!lockScreen) {
                                snackbar.setAction("have a look") {
                                    val startMain = Intent(Intent.ACTION_MAIN)
                                    startMain.addCategory(
                                            Intent.CATEGORY_HOME)
                                    startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(startMain)
                                }.setActionTextColor(
                                        ResourceUtil.getColor(this@DetailActivity, R.color.white))
                            }
                            snackbar.show()
                        } else {
                            Snackbar.make(mCoordinatorLayout, "Fail", Snackbar.LENGTH_LONG)
                                    .setCallback(mSnackbarCallback)
                                    .show()
                        }
                        dismissDialog()
                    }

                    override fun onError(e: Throwable) {
                        Timber.e(e)
                    }
                })
    }

    private fun setWallpapersMore() {
        requestPermission().subscribe(object : Observer<Boolean> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: Boolean) {
                val progressText = "Preparing..."
                if (mUri == null) {
                    showDialog(progressText)
                    download().subscribe(object : Observer<Uri> {
                        override fun onComplete() {
                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(t: Uri) {
                            dismissDialog()
                            setWallpapersMore()
                        }

                        override fun onError(e: Throwable) {
                        }
                    })
                } else {
                    val intent = Intent(Intent.ACTION_ATTACH_DATA)
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    intent.setDataAndType(mUri, "image/*")
                    intent.putExtra("mimeType", "image/*")
                    startActivity(Intent.createChooser(intent, "Set as:"))
                }
            }

            override fun onError(e: Throwable) {
            }

        })
    }

    private fun showDialog(s: String) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogUtil.getProgressDialog(this, s)
        } else if (mProgressDialog!!.isShowing) {
            return
        } else {
            if (mProgressDialog!!.getCustomView() != null) {
                (mProgressDialog!!.getCustomView()!!
                        .findViewById(R.id.textView_progress_message) as TextView).text = s
            }
        }
        if (!mProgressDialog!!.isShowing) {
            mProgressDialog!!.show()
        }
    }

    private fun dismissDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }

    private fun showFileChooser() {
        requestPermission().subscribe(object : Observer<Boolean> {
            override fun onComplete() {}

            override fun onSubscribe(d: Disposable) {}

            override fun onNext(t: Boolean) {
                mFAB.hideMenuButton(true)
                val dialog = Dialogs.filePickerBottomSheetDialog(
                        this@DetailActivity,
                        "Select path to save...",
                        object : FilePickerSheetView.OnFileSelectedCallBack {
                            override fun onFileSelected(file: File?) {
                                saveToLocal(file)
                            }
                        }
                )
                dialog.setOnDismissListener { mFAB.showMenuButton(true) }
                dialog.show()
            }

            override fun onError(e: Throwable) {}
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            if (mToolbar.alpha == 0f) {
                return false
            }
            onBackPressed()
            return true
        }
        return false
    }

    private fun setupFAB() {
        Observable.just<String>(mLink)
                .map { s ->
                    var bitmap: Bitmap? = null
                    try {
                        bitmap = GlideApp.with(this@DetailActivity)
                                .asBitmap()
                                .load(s)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .submit(-1, -1)
                                .get()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    } catch (e: ExecutionException) {
                        e.printStackTrace()
                    }

                    if (bitmap != null) {
                        val palette = Palette.from(bitmap).generate()
                        val swatches = arrayOf(
                                palette.mutedSwatch,
                                palette.lightMutedSwatch,
                                palette.darkMutedSwatch,
                                palette.vibrantSwatch,
                                palette.lightVibrantSwatch,
                                palette.darkVibrantSwatch)
                        return@map swatches.firstOrNull()
                    }
                    null
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Palette.Swatch?> {
                    override fun onComplete() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onSubscribe(d: Disposable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onNext(swatch: Palette.Swatch) {
                        val color = swatch.rgb

                        mFAB.visibility = View.VISIBLE
                        mFAB.menuButtonColorNormal = color
                        mFAB.menuButtonColorPressed = color
                        mApplyFAB.colorNormal = color
                        mApplyFAB.colorPressed = color
                        mSaveFAB.colorNormal = color
                        mSaveFAB.colorPressed = color

                        mFAB.showMenuButton(true)
                    }

                    override fun onError(e: Throwable) {
                    }

                })
    }

    private fun openOuterPhotoViewer(uri: Uri?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val file = File(uri!!.path!!)
            val intent = Intent()
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri = FileProvider.getUriForFile(this,
                    "com.sorcererxw.matthiasheiderichphotography.fileProvider",
                    file)
            intent.setDataAndType(contentUri, "image/*")
            intent.action = Intent.ACTION_VIEW
            startActivity(intent)
        } else {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(uri, "image/*")
            startActivity(intent)
        }
    }
}
