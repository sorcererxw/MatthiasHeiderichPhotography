package com.sorcererxw.matthiasheiderichphotography.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import com.sorcererxw.matthiasheiderichphotography.api.MHClient
import com.sorcererxw.matthiasheiderichphotography.room.photo.PhotoEntity
import com.sorcererxw.matthiasheiderichphotography.ui.adapters.MHAdapter
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.base.BaseFragment
import com.sorcererxw.matthiasheiderichphotography.ui.others.LinerMarginDecoration
import com.sorcererxw.matthiasheiderichphotography.util.AppPref
import com.sorcererxw.matthiasheiderichphotography.util.DisplayUtil
import com.sorcererxw.matthiasheiderichphotography.util.ResourceUtil
import com.sorcererxw.matthiasheiderichphotography.viewmodel.GalleryViewmodel
import com.sorcererxw.matthiasheidericphotography.R
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/22
 */
class GalleryFragment : BaseFragment() {
    private lateinit var model: GalleryViewmodel

    private lateinit var toolbar: Toolbar
    private lateinit var mRoot: CoordinatorLayout
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MHAdapter
    private lateinit var mLayoutManager: GridLayoutManager

    private lateinit var galleryName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProviders.of(this).get(GalleryViewmodel::class.java)

        galleryName = GalleryFragmentArgs.fromBundle(arguments).galleyName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        mRoot = view.findViewById(R.id.coordinatorLayout_fragment_mh)
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout_fragment_mh)
        mRecyclerView = view.findViewById(R.id.recyclerView_fragment_mh)
        toolbar = view.findViewById(R.id.toolbar_gallery)

        toolbar.setNavigationIcon(R.drawable.ic_outline_arrow_back_24px)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        mAdapter = MHAdapter(activity!!)
        mAdapter.onItemClickListener = object : MHAdapter.OnItemClickListener {
            override fun onLongClick(view: View, data: PhotoEntity, position: Int,
                                     holder: MHAdapter.MHViewHolder) {
                val extras = FragmentNavigator.Extras.Builder()
                        .addSharedElement(holder.image, "image")
                        .build()

//                Navigation.findNavController(this@GalleryFragment.view!!)
//                        .navigate(GalleryFragmentDirections.actionGalleryFragmentToDetailFragment(
//                                data.url), extras)
                Navigation.findNavController(this@GalleryFragment.view!!)
                        .navigate(R.id.action_galleryFragment_to_detailFragment,
                                Bundle().also { it.putString("link", data.url) },
                                null,
                                extras)
            }
        }
        mAdapter.onItemLongClickListener = object : MHAdapter.OnItemLongClickListener {
            override fun onLongClick(view: View, data: PhotoEntity, position: Int,
                                     holder: MHAdapter.MHViewHolder) {
                if (data.stared) {
                    model.unstarPhoto(data.url)
                    Snackbar.make(mRoot, "Removed from Favorite", LENGTH_LONG).show()
                    holder.playDislikeAnim(context)
                } else {
                    model.starPhoto(data.url)
                    Snackbar.make(mRoot, "Added to Favorite", LENGTH_LONG)
                            .setAction("show favorites") {
                                Navigation.findNavController(this@GalleryFragment.view!!)
                                        .navigate(
                                                GalleryFragmentDirections.actionGalleryFragmentToFavoriteFragment())
                            }
                            .setActionTextColor(ResourceUtil.getColor(context, R.color.white))
                            .show()
                    holder.playLikeAnim(context)
                }
            }
        }
        mRecyclerView.adapter = mAdapter
        mLayoutManager = GridLayoutManager(context, 1)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView
                .addItemDecoration(LinerMarginDecoration(DisplayUtil.dip2px(context, 4f)))
        mRecyclerView.setHasFixedSize(true)

        mSwipeRefreshLayout.setOnRefreshListener { catchData() }

        model.getPhotoList(galleryName).observe(this, androidx.lifecycle.Observer {
            mAdapter.setData(it)
        })
        catchData()

        return view
    }

    private fun catchData() {
        MHClient.getInstance().getPhotoList(galleryName)
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : Observer<List<PhotoEntity>> {
                    override fun onComplete() {
                        mSwipeRefreshLayout.post { mSwipeRefreshLayout.isRefreshing = false }
                    }

                    override fun onSubscribe(d: Disposable) {
                        mSwipeRefreshLayout.post { mSwipeRefreshLayout.isRefreshing = true }
                    }

                    override fun onNext(t: List<PhotoEntity>) {
                        model.savePhotos(t)
                    }

                    override fun onError(e: Throwable) {
                        mSwipeRefreshLayout.post { mSwipeRefreshLayout.isRefreshing = false }
                    }

                })
    }

    override fun refreshUI() {
        super.refreshUI()
        mAdapter.setNightMode(AppPref.getInstance(context).themeNightMode.get())
//        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(
//                ThemeHelper.getPrimaryColorRes(context))
//        mSwipeRefreshLayout.setColorSchemeResources(
//                ThemeHelper.getAccentColorRes(context))
    }

    override fun onToolbarDoubleTap() {
        super.onToolbarDoubleTap()
        mRecyclerView.smoothScrollToPosition(0)
    }
}
