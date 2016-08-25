package com.sorcererxw.matthiasheiderichphotography.ui.activities;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.sorcererxw.matthiasheiderichphotography.MHApplication;
import com.sorcererxw.matthiasheiderichphotography.ui.views.TypefaceToolbar;
import com.sorcererxw.matthiasheiderichphotography.util.DialogUtil;
import com.sorcererxw.matthiasheiderichphotography.util.PermissionsHelper;
import com.sorcererxw.matthiasheiderichphotography.util.ResourceUtil;
import com.sorcererxw.matthiasheiderichphotography.util.StringUtil;
import com.sorcererxw.matthiasheidericphotography.BuildConfig;
import com.sorcererxw.matthiasheidericphotography.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;

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
        setWallpapers();
    }

    @OnClick(R.id.fab_detail_save)
    void clickSave() {
        saveToLocal();
    }

    private String mLink;

    private Bitmap mRawBitmap;

    private MaterialDialog mSaveDialog;
    private MaterialDialog mSetDialog;

    private Snackbar.Callback mSnackbarCallback = new Snackbar.Callback() {
        @Override
        public void onDismissed(Snackbar snackbar, int event) {
            super.onDismissed(snackbar, event);
            mFAB.setTranslationY(0);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        mLink = getIntent().getStringExtra("link");
        initImage();
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mSaveDialog = DialogUtil.getProgressDialog(this, "Saving to local...");
        mSetDialog = DialogUtil.getProgressDialog(this, "Setting wallpaper...");

        mFAB.hideMenuButton(false);
    }

    private void initImage() {
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (MHApplication.getInstance().getTmpDrawable() != null) {
            mImageView.setImageDrawable(MHApplication.getInstance().getTmpDrawable());
            setFABColor();
        } else {
            Observable.just(mLink + "?format=1000w").map(new Func1<String, Drawable>() {
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
                                setFABColor();
                            } else {
                                finish();
                            }
                        }
                    });
        }
    }

    private interface DownloadCallback {
        void onFinish();

    }

    private void download(final DownloadCallback callback) {
        Observable.just(mLink)
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String s) {
                        try {
                            return Glide.with(DetailActivity.this)
                                    .load(mLink)
                                    .asBitmap()
                                    .into(-1, -1)
                                    .get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        mRawBitmap = bitmap;
                        if (callback != null) {
                            callback.onFinish();
                        }
                    }
                });
    }

    private void saveToLocal() {
        if (Build.VERSION.SDK_INT >= 23 && !PermissionsHelper.hasPermission(this,
                PermissionsHelper.WRITE_EXTERNAL_STORAGE_MANIFEST)) {
            PermissionsHelper.requestWriteExternalStorage(this);
            return;
        }
        if (mRawBitmap == null) {
            if (!mSaveDialog.isShowing()) {
                mSaveDialog.show();
            }
            download(new DownloadCallback() {
                @Override
                public void onFinish() {
                    saveToLocal();
                }
            });
        } else {
            FileOutputStream out = null;
            try {
                File path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File dir = new File(path, "/Matthisa Heideric");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                final File file = new File(path, "/Matthisa Heideric/"
                        + StringUtil.getFileNameFromLinkWithoutExtension(mLink) + ".png");
                if (file.exists()) {
                    Snackbar.make(mCoordinatorLayout, "Existed", Snackbar.LENGTH_LONG)
                            .setAction("Open", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(file), "image/*");
                                    startActivity(intent);
                                }
                            })
                            .setActionTextColor(ResourceUtil.getColor(this, R.color.white))
                            .setCallback(mSnackbarCallback)
                            .show();
                } else {
                    if (!mSaveDialog.isShowing()) {
                        mSaveDialog.show();
                    }
                    out = new FileOutputStream(file);
                    mRawBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    Snackbar.make(mCoordinatorLayout, "Success", Snackbar.LENGTH_LONG)
                            .setAction("Open", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(file), "image/*");
                                    startActivity(intent);
                                }
                            })
                            .setActionTextColor(ResourceUtil.getColor(this, R.color.white))
                            .setCallback(mSnackbarCallback)
                            .show();
                }
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
                Snackbar.make(mCoordinatorLayout, "Fail", Snackbar.LENGTH_LONG)
                        .setCallback(mSnackbarCallback).show();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (mSaveDialog.isShowing()) {
                mSaveDialog.dismiss();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionsHelper.WRITE_EXTERNAL_STORAGE_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveToLocal();
                // Permission Granted
            } else {
                Toast.makeText(this, "No permission :(", Toast.LENGTH_SHORT).show();
                // Permission Denied
            }
        }
    }

    private void setWallpapers() {
        if (!mSetDialog.isShowing()) {
            mSetDialog.show();
        }
        if (mRawBitmap == null) {
            download(new DownloadCallback() {
                @Override
                public void onFinish() {
                    setWallpapers();
                }
            });
        } else {
            WallpaperManager wallpaperManager
                    = WallpaperManager.getInstance(getApplicationContext());
            try {
                wallpaperManager.setWallpaperOffsets(
                        getWindow().getDecorView().getRootView().getWindowToken(), 2, 1);
                wallpaperManager.setBitmap(mRawBitmap);
                Snackbar.make(mCoordinatorLayout, "Success", Snackbar.LENGTH_LONG)
                        .setCallback(mSnackbarCallback).show();
            } catch (IOException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
                Snackbar.make(mCoordinatorLayout, "Fail", Snackbar.LENGTH_LONG)
                        .setCallback(mSnackbarCallback).show();
            }
            mSetDialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return false;
    }


    private void setFABColor() {
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
}
