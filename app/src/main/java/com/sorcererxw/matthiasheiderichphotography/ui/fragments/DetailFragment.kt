package com.sorcererxw.matthiasheiderichphotography.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.list.ItemListener
import com.afollestad.materialdialogs.list.listItems
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.materialize.util.UIUtils
import com.sorcererxw.matthiasheiderichphotography.config.GlideApp
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.base.BaseFragment
import com.sorcererxw.matthiasheiderichphotography.ui.others.Dialogs
import com.sorcererxw.matthiasheiderichphotography.ui.others.FilePickerSheetView
import com.sorcererxw.matthiasheiderichphotography.util.DialogUtil
import com.sorcererxw.matthiasheiderichphotography.util.ResourceUtil
import com.sorcererxw.matthiasheiderichphotography.util.StringUtil
import com.sorcererxw.matthiasheiderichphotography.util.WallpaperSetter
import com.sorcererxw.matthiasheidericphotography.BuildConfig
import com.sorcererxw.matthiasheidericphotography.R
import com.tbruyelle.rxpermissions2.RxPermissions
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

/**
 * @description:
 * @author: Sorcerer
 * @date: 11/1/2018
 */
class DetailFragment : BaseFragment() {
    private lateinit var mCoordinatorLayout: CoordinatorLayout
    private lateinit var mToolbar: Toolbar
    private lateinit var mImageView: PhotoView
    private lateinit var mFAB: FloatingActionMenu
    private lateinit var mApplyFAB: FloatingActionButton
    private lateinit var mSaveFAB: FloatingActionButton
    private lateinit var mWallpaperSetter: WallpaperSetter

    private lateinit var mLink: String
    private var mUri: Uri? = null

    private var mProgressDialog: MaterialDialog? = null

    private val mSnackbarCallback = object : Snackbar.Callback() {
        override fun onDismissed(snackbar: Snackbar?, event: Int) {
            super.onDismissed(snackbar, event)
            mFAB.animate().translationY(0f).setDuration(200).start()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_detail, container, false)
        mCoordinatorLayout = view.findViewById(R.id.coordinatorLayout_detail)
        mToolbar = view.findViewById(R.id.toolbar_detail)
        mImageView = view.findViewById(R.id.imageView_detail)
        mFAB = view.findViewById(R.id.fab_detail)
        mApplyFAB = view.findViewById(R.id.fab_detail_apply)
        mSaveFAB = view.findViewById(R.id.fab_detail_save)

        mWallpaperSetter = WallpaperSetter(context)

        mApplyFAB.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 24) {
                MaterialDialog(context).listItems(
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

        mLink = DetailFragmentArgs.fromBundle(arguments).link

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

        Observable.just(mLink)
                .observeOn(Schedulers.newThread())
                .map {
                    GlideApp.with(context).asBitmap().load(mLink).diskCacheStrategy(
                            DiskCacheStrategy.ALL).submit().get()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Bitmap> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    @SuppressLint("ShowToast")
                    override fun onNext(bitmap: Bitmap) {
                        mImageView.setImageBitmap(bitmap)
                        setupFAB(bitmap)

//                        val screenHeight = UIUtils.getScreenHeight(context)
//                        val statusHeight = UIUtils.getStatusBarHeight(context)
//                        val toolbarHeight = mToolbar.height
//                        val target = Bitmap.createBitmap(
//                                bitmap, 0,
//                                (1.0 * statusHeight / screenHeight * bitmap.height).toInt(),
//                                bitmap.width,
//                                (1.0 * (statusHeight + toolbarHeight) / screenHeight * bitmap.height).toInt()
//                        )
//
//                        if (calImageLuminance(target) > 0.5) {
//                        } else {
//                        }
                    }

                    override fun onError(e: Throwable) {
                        Timber.e(e)
                        activity!!.onBackPressed()
                    }
                })

        mToolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        mToolbar.setNavigationIcon(R.drawable.ic_outline_arrow_back_24px)

        mToolbar.setPadding(mToolbar.paddingLeft,
                UIUtils.getStatusBarHeight(context),
                mToolbar.paddingRight, mToolbar.paddingBottom)

        mFAB.hideMenuButton(false)

        return view
    }

    private fun calImageLuminance(bitmap: Bitmap): Double {
        var sum = 0.0
        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                sum += ColorUtils.calculateLuminance(bitmap.getPixel(x, y))
            }
        }
        return sum / (bitmap.height * bitmap.width)
    }

    private fun requestPermission(): Observable<Boolean> {
        return RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .filter { granted ->
                    if (!granted) {
                        Snackbar.make(mCoordinatorLayout, "No permission :(", Snackbar.LENGTH_LONG)
                                .addCallback(mSnackbarCallback).show()
                    }
                    granted
                }
    }

    private fun download(
            path: File = File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "/Matthisa Heiderich")
                    .also { if (!it.exists()) it.mkdir() }
    ): Observable<Uri> {
        return Observable.just<String>(mLink).map {
            GlideApp.with(context)
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
                                StringUtil.getFileNameFromLinkWithoutExtension(mLink) + ".png")
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

    private fun saveToLocal(path: File) {
        requestPermission().filter {
            if (mUri != null) {
                Snackbar
                        .make(mCoordinatorLayout, "Existed", Snackbar.LENGTH_LONG)
                        .setAction("Open") { openOuterPhotoViewer(mUri) }
                        .setActionTextColor(ResourceUtil.getColor(context, R.color.white))
                        .addCallback(mSnackbarCallback)
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
                        .setActionTextColor(ResourceUtil.getColor(context, R.color.white))
                        .addCallback(mSnackbarCallback)
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
                        val wallpaperManager = WallpaperManager.getInstance(context)
                        wallpaperManager.setWallpaperOffsets(
                                activity!!.window.decorView.rootView.windowToken, 0.5f, 1f)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            if (lockScreen) {
                                mWallpaperSetter.setLockScreenWallpaper(it)
                            }
                            if (homeScreen) {
                                mWallpaperSetter.setSystemWallpaper(it)
                            }
                        }
                        if (!homeScreen && !lockScreen) {
                            mWallpaperSetter.setWallpaperSimple(activity!!.windowManager, it)
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
                                    .addCallback(mSnackbarCallback)
                            if (!lockScreen) {
                                snackbar.setAction("have a look") {
                                    val startMain = Intent(Intent.ACTION_MAIN)
                                    startMain.addCategory(
                                            Intent.CATEGORY_HOME)
                                    startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(startMain)
                                }.setActionTextColor(
                                        ResourceUtil.getColor(context, R.color.white))
                            }
                            snackbar.show()
                        } else {
                            Snackbar
                                    .make(mCoordinatorLayout, "Fail", Snackbar.LENGTH_LONG)
                                    .addCallback(mSnackbarCallback)
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
            mProgressDialog = DialogUtil.getProgressDialog(context, s)
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
                        activity!!,
                        "Select path to save...",
                        object : FilePickerSheetView.OnFileSelectedCallBack {
                            override fun onFileSelected(file: File?) {
                                if (file != null) saveToLocal(file)
                            }
                        }
                )
                dialog.setOnDismissListener { mFAB.showMenuButton(true) }
                dialog.show()
            }

            override fun onError(e: Throwable) {}
        })
    }

    private fun setupFAB(bitmap: Bitmap) {
        val palette = Palette.from(bitmap).generate()
        val colorList = arrayOf(
                palette.mutedSwatch,
                palette.lightMutedSwatch,
                palette.darkMutedSwatch,
                palette.vibrantSwatch,
                palette.lightVibrantSwatch,
                palette.darkVibrantSwatch
        ).filter { it != null }.map { it!!.rgb }
        val color = if (colorList.isNotEmpty()) colorList.first()
        else ResourceUtil.getColor(context, R.color.accent)

        mFAB.visibility = View.VISIBLE
        mFAB.menuButtonColorNormal = color
        mFAB.menuButtonColorPressed = color
        mApplyFAB.colorNormal = color
        mApplyFAB.colorPressed = color
        mSaveFAB.colorNormal = color
        mSaveFAB.colorPressed = color

        mFAB.showMenuButton(true)
    }

    private fun openOuterPhotoViewer(uri: Uri?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val file = File(uri!!.path!!)
            val intent = Intent()
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri = FileProvider.getUriForFile(context,
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