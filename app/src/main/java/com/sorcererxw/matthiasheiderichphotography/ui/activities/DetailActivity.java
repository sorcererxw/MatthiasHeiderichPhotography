package com.sorcererxw.matthiasheiderichphotography.ui.activities;

import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.ui.others.Dialogs;
import com.sorcererxw.matthiasheiderichphotography.ui.others.FilePickerSheetView;
import com.sorcererxw.matthiasheiderichphotography.ui.others.SimpleTransitionListener;
import com.sorcererxw.matthiasheiderichphotography.util.DialogUtil;
import com.sorcererxw.matthiasheiderichphotography.util.ResourceUtil;
import com.sorcererxw.matthiasheiderichphotography.util.StringUtil;
import com.sorcererxw.matthiasheiderichphotography.util.ThemeHelper;
import com.sorcererxw.matthiasheiderichphotography.util.WallpaperSetter;
import com.sorcererxw.matthiasheidericphotography.BuildConfig;
import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.typefaceviews.widgets.TypefaceSnackbar;
import com.sorcererxw.typefaceviews.widgets.TypefaceToolbar;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.WallpaperManager.FLAG_LOCK;
import static android.app.WallpaperManager.FLAG_SYSTEM;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.coordinatorLayout_detail)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar_detail)
    TypefaceToolbar mToolbar;
    @BindView(R.id.imageView_detail)
    PhotoView mImageView;
    @BindView(R.id.fab_detail)
    FloatingActionMenu mFAB;
    @BindView(R.id.fab_detail_apply)
    FloatingActionButton mApplyFAB;
    @BindView(R.id.fab_detail_save)
    FloatingActionButton mSaveFAB;
    @BindView(R.id.loadingIndicator_item)
    AVLoadingIndicatorView mIndicatorView;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @OnClick(R.id.fab_detail_apply)
    void clickApply() {
        if (Build.VERSION.SDK_INT >= 24) {
            MaterialDialog.Builder builder = Dialogs.TypefaceMaterialDialogBuilder(this);
            builder.items("Home Screen", "Lock Screen", "Home And Lock Screen");
            builder.itemsCallback(new MaterialDialog.ListCallback() {
                @Override
                public void onSelection(MaterialDialog dialog, View itemView, int position,
                                        CharSequence text) {
                    String selection = text.toString();
                    if (selection.contains("Home") && selection.contains("Lock")) {
                        setWallpapers(true, true);
                    } else if (selection.contains("Home")) {
                        setWallpapers(true, false);
                    } else {
                        setWallpapers(false, true);
                    }
                }
            });
            builder.show();
        } else {
            setWallpapers(false, false);
        }
    }

    @OnLongClick(R.id.fab_detail_apply)
    boolean longClickApply() {
        setWallpapersMore();
        return true;
    }

    @OnClick(R.id.fab_detail_save)
    void clickSave() {
        saveToLocal();
    }

    @OnLongClick(R.id.fab_detail_save)
    boolean longClickSave() {
        showFileChooser();
        return true;
    }

    private WallpaperSetter mWallpaperSetter;

    private String mLink;
    private Uri mUri = null;

    private RxPermissions mRxPermissions;

    private MaterialDialog mProgressDialog;

    private Snackbar.Callback mSnackbarCallback = new Snackbar.Callback() {
        @Override
        public void onDismissed(Snackbar snackbar, int event) {
            super.onDismissed(snackbar, event);
            mFAB.animate().translationY(0).setDuration(200).start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MHApp.getInstance().getPrefs().getThemeNightMode().getValue()) {
            setTheme(R.style.NightTheme);
        } else {
            setTheme(R.style.DayTheme);
        }
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        mLink = getIntent().getStringExtra("link");
        mWallpaperSetter = new WallpaperSetter(this);
        initImage();

        mIndicatorView.setIndicatorColor(ThemeHelper.getAccentColor(this));
        mToolbar.setHasText(false);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mFAB.hideMenuButton(false);
        mRxPermissions = new RxPermissions(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initImage() {
        mImageView.setOnSingleFlingListener(new PhotoViewAttacher.OnSingleFlingListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                return false;
            }
        });
        mImageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (mFAB.isOpened()) {
                    mFAB.close(true);
                    return;
                }
                if (mFAB.isMenuButtonHidden()) {
                    mFAB.showMenuButton(true);
                    mFAB.setClickable(true);
                    mToolbar.animate().alpha(1).start();
                } else {
                    mFAB.hideMenuButton(true);
                    mFAB.setClickable(false);
                    mToolbar.animate().alpha(0).start();
                }
            }
        });
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if (MHApp.getTmpDrawable(this) != null) {
            mImageView.setImageDrawable(MHApp.getTmpDrawable(this));
        }
        getWindow().getSharedElementEnterTransition().addListener(new SimpleTransitionListener() {
            @Override
            public void onTransitionEnd(Transition transition) {
                Observable.just(mLink)
                        .map(new Func1<String, Drawable>() {
                            @Override
                            public Drawable call(String s) {
                                try {
                                    return Glide.with(DetailActivity.this)
                                            .load(s)
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(-1, -1)
                                            .get();
                                } catch (InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Drawable>() {
                            @Override
                            public void call(Drawable drawable) {
                                if (drawable != null) {
                                    mImageView.setImageDrawable(drawable);
                                    setupFAB();
                                } else {
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    private Observable<Boolean> requestPermission() {
        return mRxPermissions.request(WRITE_EXTERNAL_STORAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean granted) {
                        if (!granted) {
                            TypefaceSnackbar.make(mCoordinatorLayout, "No permission :(",
                                    Snackbar.LENGTH_LONG)
                                    .setCallback(mSnackbarCallback).show();
                        }
                        return granted;
                    }
                });
    }

    private Observable<Uri> download() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File dir = new File(path, "/Matthisa Heiderich");
        if (!dir.exists()) {
            dir.mkdir();
        }
        return download(dir);
    }

    private Observable<Uri> download(final File path) {
        return Observable.just(mLink).map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String s) {
                try {
                    return Glide.with(DetailActivity.this)
                            .load(mLink)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(-1, -1)
                            .get();
                } catch (InterruptedException | ExecutionException e) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace();
                    }
                    try {
                        return Glide.with(DetailActivity.this)
                                .load(mLink)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(-1, -1)
                                .get();
                    } catch (InterruptedException | ExecutionException e1) {
                        if (BuildConfig.DEBUG) {
                            e1.printStackTrace();
                        }
                    }
                }
                return null;
            }
        }).subscribeOn(Schedulers.io()).map(new Func1<Bitmap, Uri>() {
            @Override
            public Uri call(Bitmap bitmap) {
                if (bitmap == null) {
                    return null;
                }
                Uri uri = null;
                FileOutputStream out = null;
                try {
                    final File file = new File(path,
                            StringUtil.getFileNameFromLinkWithoutExtension(mLink) + ".png");
                    if (file.exists()) {
                        uri = Uri.fromFile(file);
                    } else {
                        out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        uri = Uri.fromFile(file);
                    }
                } catch (Exception e) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace();
                    }
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return uri;
            }
        }).subscribeOn(
                Schedulers.newThread()
        ).observeOn(
                AndroidSchedulers.mainThread()
        ).map(new Func1<Uri, Uri>() {
            @Override
            public Uri call(Uri uri) {
                mUri = uri;
                return uri;
            }
        });
    }

    private void saveToLocal() {
        requestPermission().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                File path = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File dir = new File(path, "/Matthisa Heiderich");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                saveToLocal(dir);
            }
        });
    }

    private void saveToLocal(final File path) {
        requestPermission().filter(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean granted) {
                if (mUri != null) {
                    TypefaceSnackbar
                            .make(mCoordinatorLayout, "Existed", Snackbar.LENGTH_LONG)
                            .setAction("Open", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    openOuterPhotoViewer(mUri);
                                }
                            })
                            .setActionTextColor(ResourceUtil
                                    .getColor(DetailActivity.this, R.color.white))
                            .setCallback(mSnackbarCallback)
                            .show();
                }
                return mUri == null;
            }
        }).flatMap(new Func1<Boolean, Observable<Uri>>() {
            @Override
            public Observable<Uri> call(Boolean aBoolean) {
                showDialog("Saving To Local...");
                return download(path);
            }
        }).filter(new Func1<Uri, Boolean>() {
            @Override
            public Boolean call(Uri uri) {
                dismissDialog();
                if (uri == null) {
                    TypefaceSnackbar.make(mCoordinatorLayout, "Failed",
                            Snackbar.LENGTH_LONG)
                            .setCallback(mSnackbarCallback).show();
                }
                return uri != null;
            }
        }).subscribe(new Action1<Uri>() {
            @Override
            public void call(final Uri uri) {
                TypefaceSnackbar.make(mCoordinatorLayout, "Success", Snackbar.LENGTH_LONG)
                        .setAction("Open", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openOuterPhotoViewer(uri);
                            }
                        })
                        .setActionTextColor(
                                ResourceUtil.getColor(DetailActivity.this,
                                        R.color.white))
                        .setCallback(mSnackbarCallback)
                        .show();
            }
        });
    }

    private void setWallpapers(final boolean homeScreen, final boolean lockScreen) {
        requestPermission().filter(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean) {
                showDialog("Setting wallpaper...");
                if (mUri == null) {
                    download().subscribe(new Action1<Uri>() {
                        @Override
                        public void call(Uri uri) {
                            setWallpapers(homeScreen, lockScreen);
                        }
                    });
                }
                return mUri != null;
            }
        }).flatMap(new Func1<Boolean, Observable<Uri>>() {
            @Override
            public Observable<Uri> call(Boolean aBoolean) {
                return Observable.just(mUri);
            }
        }).observeOn(Schedulers.newThread()).map(new Func1<Uri, Boolean>() {
            @Override
            public Boolean call(Uri uri) {
                try {
                    WallpaperManager wallpaperManager =
                            WallpaperManager.getInstance(getApplicationContext());
                    wallpaperManager.setWallpaperOffsets(
                            getWindow().getDecorView().getRootView().getWindowToken(), 0.5f, 1);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if (lockScreen) {
                            mWallpaperSetter.setLockScreenWallpaper(mUri);
                        }
                        if (homeScreen) {
                            mWallpaperSetter.setSystemWallpaper(mUri);
                        }
                    }
                    if (!homeScreen && !lockScreen) {
                        mWallpaperSetter.setWallpaperSimple(getWindowManager(), mUri);
                    }
                    return true;
                } catch (IOException e) {
                    Timber.e(e);
                }
                return false;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            TypefaceSnackbar snackbar = TypefaceSnackbar
                                    .make(mCoordinatorLayout, "Success",
                                            Snackbar.LENGTH_LONG)
                                    .setCallback(mSnackbarCallback);
                            if (!lockScreen) {
                                snackbar.setAction("have a look", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent startMain =
                                                new Intent(Intent.ACTION_MAIN);
                                        startMain.addCategory(
                                                Intent.CATEGORY_HOME);
                                        startMain.setFlags(
                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(startMain);
                                    }
                                }).setActionTextColor(
                                        ResourceUtil.getColor(DetailActivity.this, R.color.white));
                            }
                            snackbar.show();
                        } else {
                            TypefaceSnackbar
                                    .make(mCoordinatorLayout, "Fail", Snackbar.LENGTH_LONG)
                                    .setCallback(mSnackbarCallback)
                                    .show();
                        }
                        dismissDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.d(throwable);
                    }
                });
    }

    private void setWallpapersMore() {
        requestPermission().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                String progressText = "Preparing...";
                if (mUri == null) {
                    showDialog(progressText);
                    download().subscribe(new Action1<Uri>() {
                        @Override
                        public void call(Uri uri) {
                            dismissDialog();
                            setWallpapersMore();
                        }
                    });
                } else {
                    Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(mUri, "image/*");
                    intent.putExtra("mimeType", "image/*");
                    startActivity(Intent.createChooser(intent, "Set as:"));
                }
            }
        });
    }

    private void showDialog(String s) {
        if (mProgressDialog == null) {
            mProgressDialog = DialogUtil.getProgressDialog(this, s);
        } else if (mProgressDialog.isShowing()) {
            return;
        } else {
            if (mProgressDialog.getCustomView() != null) {
                ((TextView) mProgressDialog.getCustomView()
                        .findViewById(R.id.textView_progress_message)).setText(s);
            }
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void showFileChooser() {
        requestPermission().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                mFAB.hideMenuButton(true);
                final BottomSheetDialog dialog =
                        Dialogs.FilePickerBottomSheetDialog(DetailActivity.this,
                                "Select path to save...",
                                new FilePickerSheetView.OnFileSelectedCallBack() {
                                    @Override
                                    public void onFileSelected(File file) {
                                        saveToLocal(file);
                                    }
                                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mFAB.showMenuButton(true);
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (mToolbar.getAlpha() == 0) {
                return false;
            }
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setupFAB() {
        Observable.just(mLink + "?format=1000w").map(new Func1<String, Palette.Swatch>() {
            @Override
            public Palette.Swatch call(String s) {
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(DetailActivity.this)
                            .load(s)
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(-1, -1)
                            .get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if (bitmap != null) {
                    Palette palette = Palette.from(bitmap).generate();
                    Palette.Swatch[] swatches = new Palette.Swatch[]{
                            palette.getMutedSwatch(),
                            palette.getLightMutedSwatch(),
                            palette.getDarkMutedSwatch(),
                            palette.getVibrantSwatch(),
                            palette.getLightVibrantSwatch(),
                            palette.getDarkVibrantSwatch(),
                    };
                    for (Palette.Swatch sc : swatches) {
                        if (sc != null) {
                            return sc;
                        }
                    }
                    return null;
                }
                return null;
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Palette.Swatch>() {
                    @Override
                    public void call(Palette.Swatch swatch) {
                        int color = ResourceUtil.getColor(DetailActivity.this, R.color.accent);
                        if (swatch != null) {
                            color = swatch.getRgb();
                        }

                        mFAB.setVisibility(View.VISIBLE);
                        mFAB.setMenuButtonColorNormal(color);
                        mFAB.setMenuButtonColorPressed(color);
                        mApplyFAB.setColorNormal(color);
                        mApplyFAB.setColorPressed(color);
                        mSaveFAB.setColorNormal(color);
                        mSaveFAB.setColorPressed(color);

                        mFAB.showMenuButton(true);
                    }
                });
    }

    private void openOuterPhotoViewer(Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File file = new File(uri.getPath());
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this,
                    "com.sorcererxw.matthiasheiderichphotography.fileProvider",
                    file);
            intent.setDataAndType(contentUri, "image/*");
            intent.setAction(Intent.ACTION_VIEW);
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "image/*");
            startActivity(intent);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Detail Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mClient, getIndexApiAction());
        mClient.disconnect();
    }
}
