package com.sorcererxw.matthiasheiderichphotography.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import com.sorcererxw.matthiasheiderichphotography.room.photo.PhotoEntity
import com.sorcererxw.matthiasheiderichphotography.ui.adapters.MHAdapter
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.base.BaseFragment
import com.sorcererxw.matthiasheiderichphotography.ui.others.LinerMarginDecoration
import com.sorcererxw.matthiasheiderichphotography.util.AppPref
import com.sorcererxw.matthiasheiderichphotography.util.DisplayUtil
import com.sorcererxw.matthiasheiderichphotography.util.ResourceUtil
import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper
import com.sorcererxw.matthiasheiderichphotography.viewmodel.GalleryViewmodel
import com.sorcererxw.matthiasheidericphotography.R

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/31
 */
class FavoriteFragment : BaseFragment() {

    private lateinit var model: GalleryViewmodel
    private lateinit var mEmptyView: TextView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MHAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this).get(GalleryViewmodel::class.java)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_favorite, container,
                false)
        mEmptyView = view.findViewById(R.id.textView_fragment_favorite_empty)
        mRecyclerView = view.findViewById(R.id.recyclerView_fragment_favorite)

        mAdapter = MHAdapter(activity!!)
        mAdapter.onItemLongClickListener = object : MHAdapter.OnItemLongClickListener {
            override fun onLongClick(view: View, data: PhotoEntity, position: Int,
                                     holder: MHAdapter.MHViewHolder) {
                MaterialDialog(context)
                        .message(text = "Remove the item from Favorite ?")
                        .positiveButton(text = "Yes", click = object : DialogCallback {
                            override fun invoke(p1: MaterialDialog) {
                                model.unstarPhoto(data.url)
                                mAdapter.removeItem(position)
                                if (mAdapter.itemCount == 0) {
                                    mEmptyView.visibility = View.VISIBLE
                                }
                            }
                        })
                        .negativeButton(text = "No")
                        .show()
            }
        }
        mRecyclerView.adapter = mAdapter

        val layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 1)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView
                .addItemDecoration(LinerMarginDecoration(DisplayUtil.dip2px(context, 4f)))
        mRecyclerView.setHasFixedSize(true)

        mEmptyView.typeface = TypefaceHelper.getTypeface(context, TypefaceHelper.Type.Book)
        mEmptyView.setCompoundDrawables(null,
                IconicsDrawable(context)
                        .icon(GoogleMaterial.Icon.gmd_favorite)
                        .color(ResourceUtil.getColor(context, R.color.accent))
                        .sizeDp(56), null, null)
        mEmptyView.compoundDrawablePadding = DisplayUtil.dip2px(context, 16f)

        initData()
        return view
    }


    override fun onShow() {
        initData()
    }

    override fun onToolbarDoubleTap() {
        super.onToolbarDoubleTap()
        mRecyclerView.smoothScrollToPosition(0)
    }

    override fun refreshUI() {
        super.refreshUI()
        mAdapter.setNightMode(AppPref.getInstance(context).themeNightMode.get())

        mEmptyView.setCompoundDrawables(null,
                IconicsDrawable(context)
                        .icon(GoogleMaterial.Icon.gmd_favorite)
//                        .color(ThemeHelper.getAccentColor(context))
                        .sizeDp(56), null, null)
    }

    private fun initData() {
        model.staredList.observe(this, Observer {
            mAdapter.setData(it)
        })
    }


}
