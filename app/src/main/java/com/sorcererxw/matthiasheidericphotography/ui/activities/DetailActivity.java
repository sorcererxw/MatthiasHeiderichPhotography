package com.sorcererxw.matthiasheidericphotography.ui.activities;

import android.app.WallpaperManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.sorcererxw.matthiasheidericphotography.BuildConfig;
import com.sorcererxw.matthiasheidericphotography.MHApplication;
import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.matthiasheidericphotography.ui.views.TypefaceToolbar;
import com.sorcererxw.matthiasheidericphotography.util.DialogUtil;
import com.sorcererxw.matthiasheidericphotography.util.PermissionsHelper;

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

    @BindView(R.id.toolbar_detail)
    TypefaceToolbar mToolbar;
    @BindView(R.id.imageView_detail)
    PhotoView mImageView;
    @BindView(R.id.fab_detail)
    FloatingActionsMenu mFAB;
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

    @Override
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
    }

    private void initImage() {
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (MHApplication.getInstance().getTmpDrawable() != null) {
            mImageView.setImageDrawable(MHApplication.getInstance().getTmpDrawable());
        } else {
            Observable.just(mLink + "?format=1000w").map(new Func1<String, Drawable>() {
                @Override
                public Drawable call(String s) {
                    try {
                        return Glide.with(DetailActivity.this)
                                .load(s)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model,
                                                               Target<GlideDrawable> target,
                                                               boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource,
                                                                   String model,
                                                                   Target<GlideDrawable> target,
                                                                   boolean isFromMemoryCache,
                                                                   boolean isFirstResource) {
                                        return false;
                                    }
                                })
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
        if (!mSaveDialog.isShowing()) {
            mSaveDialog.show();
        }
        if (mRawBitmap == null) {
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
                File file = new File(path, "/Matthisa Heideric/" + "test.png");
                out = new FileOutputStream(file);
                mRawBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
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
                        getWindow().getDecorView().getRootView().getWindowToken(), 1, 1);
                wallpaperManager.setBitmap(mRawBitmap);
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
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
}
