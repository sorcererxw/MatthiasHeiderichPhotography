package com.sorcererxw.matthiasheiderichphotography.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sorcererxw.matthiasheiderichphotography.api.MHClient
import com.sorcererxw.matthiasheiderichphotography.room.gallery.GalleryEntity
import com.sorcererxw.matthiasheiderichphotography.ui.adapters.GalleryAdapter
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.base.BaseFragment
import com.sorcererxw.matthiasheiderichphotography.viewmodel.HomeViewmodel
import com.sorcererxw.matthiasheidericphotography.R
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * @description:
 * @author: Sorcerer
 * @date: 10/31/2018
 */
class HomeFragment : BaseFragment() {
    private lateinit var model: HomeViewmodel

    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar

    private lateinit var adapter: GalleryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        model = ViewModelProviders.of(this).get(HomeViewmodel::class.java)
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recyclerView_home)
        toolbar = view.findViewById(R.id.toolbar_home)

        toolbar.inflateMenu(R.menu.menu_home)
        toolbar.setOnMenuItemClickListener {
            when {
                it.itemId == R.id.action_favorite -> {
                    Navigation.findNavController(this@HomeFragment.view!!).navigate(
                            HomeFragmentDirections.actionHomeFragmentToFavoriteFragment()
                    )
                    true
                }
                it.itemId == R.id.action_settings -> {
                    Navigation.findNavController(this@HomeFragment.view!!).navigate(
                            HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
                    )
                    true
                }
                else -> false
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = GalleryAdapter(context)
        recyclerView.adapter = adapter
        adapter.onGalleryClickListener = object : GalleryAdapter.OnGalleryClickListener {
            override fun onClick(gallery: GalleryEntity) {
                Navigation.findNavController(this@HomeFragment.view!!).navigate(
                        HomeFragmentDirections.actionHomeFragmentToGalleryFragment(gallery.name)
                )
            }
        }
        model.galleryList.observe(this, Observer {
            adapter.list = it
            adapter.notifyDataSetChanged()
        })
        return view
    }

    override fun onStart() {
        super.onStart()
        MHClient.getInstance().getGalleryList()
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : io.reactivex.Observer<List<GalleryEntity>> {
                    override fun onComplete() {}

                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(t: List<GalleryEntity>) {
                        model.saveGalleryList(t)
                    }

                    override fun onError(e: Throwable) {
                        Timber.e(e)
                    }

                })
    }
}