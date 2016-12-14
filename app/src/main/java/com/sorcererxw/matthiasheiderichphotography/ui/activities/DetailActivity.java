package com.sorcererxw.matthiasheiderichphotography.ui.activities;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.transition.Transition;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.sorcererxw.matthiasheiderichphotography.MHApp;
import com.sorcererxw.matthiasheiderichphotography.ui.others.Dialogs;
import com.sorcererxw.matthiasheiderichphotography.ui.others.SimpleTransitionListener;
import com.sorcererxw.matthiasheiderichphotography.util.DialogUtil;
import com.sorcererxw.matthiasheiderichphotography.util.ResourceUtil;
import com.sorcererxw.matthiasheiderichphotography.util.StringUtil;
import com.sorcererxw.matthiasheidericphotography.BuildConfig;
import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.typefaceviews.TypefaceSnackbar;
import com.sorcererxw.typefaceviews.TypefaceToolbar;
import com.tbruyelle.rxpermissions.RxPermissions;

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
        return true;
    }

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
        initImage();

        mToolbar.setHasText(false);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mFAB.hideMenuButton(false);
        mRxPermissions = new RxPermissions(this);
    }

    private void initImage() {
        mImageView.setOnSingleFlingListener(new PhotoViewAttacher.OnSingleFlingListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                Timber.d(e1.getY() + "  " + e2.getY());
                // if (Math.abs(e1.getRawX() - e2.getRawX()) > 250) {
                // // System.out.println("水平方向移动距离过大");
                // return true;
                // }
//                if (Math.abs(velocityY) < 100) {
//                    // System.out.println("手指移动的太慢了");
//                    return true;
//                }
//
//                // 手势向下 down
//                if ((e2.getRawY() - e1.getRawY()) > 200) {
//                    DetailActivity.this.finish();//在此处控制关闭
//                    return true;
//                }
//                // 手势向上 up
//                if ((e1.getRawY() - e2.getRawY()) > 200) {
//                    return true;
//                }
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
        getWindow().getSharedElementEnterTransition().addListener(
                new SimpleTransitionListener() {
                    @Override
                    public void onTransitionEnd(Transition transition) {
                        Observable.just(mLink + "?format=1000w")
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
                .doOnNext(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (!aBoolean) {
                            TypefaceSnackbar.make(mCoordinatorLayout, "No permission :(",
                                    Snackbar.LENGTH_LONG)
                                    .setCallback(mSnackbarCallback).show();
                        }
                    }
                })
                .filter(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        return aBoolean;
                    }
                });
    }

    private Observable<Uri> download() {
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
                    File path = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES);
                    File dir = new File(path, "/Matthisa Heiderich");
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    final File file = new File(path, "/Matthisa Heiderich/"
                            + StringUtil.getFileNameFromLinkWithoutExtension(mLink)
                            + ".png");
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
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Uri, Uri>() {
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
                String progressText = "Saving To Local...";
                if (mUri == null) {
                    showDialog(progressText);
                    download().subscribe(new Action1<Uri>() {
                        @Override
                        public void call(final Uri uri) {
                            dismissDialog();
                            if (uri != null) {
                                TypefaceSnackbar.make(mCoordinatorLayout, "Success",
                                        Snackbar.LENGTH_LONG)
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
                            } else {
                                TypefaceSnackbar.make(mCoordinatorLayout, "Fail",
                                        Snackbar.LENGTH_LONG)
                                        .setCallback(mSnackbarCallback).show();
                            }
                        }
                    });
                } else {
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
            }
        });

    }

    private void setWallpapers(final boolean homeScreen, final boolean lockScreen) {
        requestPermission().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                String progressText = "Setting wallpaper...";
                showDialog(progressText);
                if (mUri == null) {
                    download().subscribe(new Action1<Uri>() {
                        @Override
                        public void call(Uri uri) {
                            setWallpapers(homeScreen, lockScreen);
                        }
                    });
                } else {
                    Observable.just(mUri).map(new Func1<Uri, Boolean>() {
                        @Override
                        public Boolean call(Uri uri) {
                            try {
                                WallpaperManager wallpaperManager
                                        = WallpaperManager
                                        .getInstance(getApplicationContext());
                                wallpaperManager.setWallpaperOffsets(
                                        getWindow().getDecorView().getRootView()
                                                .getWindowToken(), 0.5f, 1);
                                if (lockScreen) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        wallpaperManager.setBitmap(
                                                cropBitmapFromCenterAndScreenSize(mUri),
                                                null,
                                                true,
                                                FLAG_LOCK);
                                    }
                                }
                                if (homeScreen) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        wallpaperManager.setStream(
                                                new FileInputStream(new File(mUri.getPath())),
                                                null,
                                                true,
                                                FLAG_SYSTEM);
                                    }
                                }
                                if (!homeScreen && !lockScreen) {
                                    setWallpaperSimple(wallpaperManager, uriToBitmap(mUri));
                                }
                                return true;
                            } catch (IOException e) {
                                if (BuildConfig.DEBUG) {
                                    e.printStackTrace();
                                }
                            }
                            return false;
                        }
                    })
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<Boolean>() {
                                @Override
                                public void call(Boolean aBoolean) {
                                    if (aBoolean) {
                                        TypefaceSnackbar snackbar = TypefaceSnackbar
                                                .make(mCoordinatorLayout, "Success",
                                                        Snackbar.LENGTH_LONG)
                                                .setCallback(mSnackbarCallback);
                                        if (!lockScreen) {
                                            snackbar.setAction("have a look",
                                                    new View.OnClickListener() {
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
                                                    ResourceUtil.getColor(DetailActivity.this,
                                                            R.color.white));
                                        }
                                        snackbar.show();
                                    } else {
                                        TypefaceSnackbar
                                                .make(mCoordinatorLayout, "Fail",
                                                        Snackbar.LENGTH_LONG)
                                                .setCallback(mSnackbarCallback)
                                                .show();
                                    }
                                    dismissDialog();
                                }
                            });
                }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
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

    private Bitmap uriToBitmap(Uri uri) {
        File image = new File(uri.getPath());
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
    }

    private void setWallpaperSimple(WallpaperManager manager, Bitmap wallPaperBitmap) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;

        int width = wallPaperBitmap.getWidth();
        width = (width * screenHeight) / wallPaperBitmap.getHeight();
        try {
            manager.setBitmap(
                    Bitmap.createScaledBitmap(wallPaperBitmap, width, screenHeight, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap cropBitmapFromCenterAndScreenSize(Uri uri) {
        return cropBitmapFromCenterAndScreenSize(uriToBitmap(uri));
    }

    private Bitmap cropBitmapFromCenterAndScreenSize(Bitmap bitmap) {
        float screenWidth, screenHeight;
        float bitmap_width = bitmap.getWidth(), bitmap_height = bitmap
                .getHeight();
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        float bitmap_ratio = bitmap_width / bitmap_height;
        float screen_ratio = screenWidth / screenHeight;
        int bitmapNewWidth, bitmapNewHeight;

        if (screen_ratio > bitmap_ratio) {
            bitmapNewWidth = (int) screenWidth;
            bitmapNewHeight = (int) (bitmapNewWidth / bitmap_ratio);
        } else {
            bitmapNewHeight = (int) screenHeight;
            bitmapNewWidth = (int) (bitmapNewHeight * bitmap_ratio);
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmapNewWidth,
                bitmapNewHeight, true);

        int bitmapGapX, bitmapGapY;
        bitmapGapX = (int) ((bitmapNewWidth - screenWidth) / 2.0f);
        bitmapGapY = (int) ((bitmapNewHeight - screenHeight) / 2.0f);

        bitmap = Bitmap.createBitmap(bitmap,
                bitmapGapX, bitmapGapY,
                (int) screenWidth, (int) screenHeight);
        return bitmap;
    }
}
