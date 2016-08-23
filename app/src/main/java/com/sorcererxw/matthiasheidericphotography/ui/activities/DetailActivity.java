package com.sorcererxw.matthiasheidericphotography.ui.activities;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.sorcererxw.matthiasheidericphotography.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Url;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_detail)
    Toolbar mToolbar;

    @BindView(R.id.imageView_detail)
    ImageView mImageView;

    @BindView(R.id.fab_detail)
    FloatingActionsMenu mFAB;

    @BindView(R.id.fab_detail_apply)
    FloatingActionButton mApplyFAB;

    @BindView(R.id.fab_detail_save)
    FloatingActionButton mSaveFAB;

    private String mLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_actibity);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        mLink = intent.getStringExtra("link");
        Glide.with(this)
                .load(mLink + "?format=500w")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);
        mToolbar.setTitle("Detail");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mFAB.setVisibility(View.GONE);
        mSaveFAB.setIcon(R.drawable.ic_cloud_download_white_18dp);
        mApplyFAB.setIcon(R.drawable.ic_wallpaper_white_18dp);

        mApplyFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Bitmap>() {
                            @Override
                            public void call(Bitmap bitmap) {
                                setWallpapers(bitmap);
                            }
                        });
            }
        });
    }

    private void save() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading...");
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
                        if (bitmap != null) {
                            FileOutputStream out = null;
//                            File file = new File();
                            try {
//                                out = new FileOutputStream(filename);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                                        out); // bmp is your Bitmap instance
                                // PNG is a lossless format, the compression factor (100) is ignored
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (out != null) {
                                        out.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }

    private void setWallpapers(Bitmap bitmap) {
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());
        try {
            myWallpaperManager.setWallpaperOffsets(
                    getWindow().getDecorView().getRootView().getWindowToken(), 1, 1);
            myWallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mFAB.setVisibility(View.VISIBLE);
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
