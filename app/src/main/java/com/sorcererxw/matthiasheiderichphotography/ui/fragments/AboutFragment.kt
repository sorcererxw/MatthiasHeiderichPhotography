package com.sorcererxw.matthiasheiderichphotography.ui.fragments

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.sorcererxw.matthiasheiderichphotography.models.LibraryBean
import com.sorcererxw.matthiasheiderichphotography.ui.adapters.LibAdapter
import com.sorcererxw.matthiasheiderichphotography.ui.fragments.base.BaseFragment
import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper
import com.sorcererxw.matthiasheidericphotography.R
import java.util.*

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/8/22
 */
class AboutFragment : BaseFragment() {

    private lateinit var mScrollView: NestedScrollView
    private lateinit var mIntroduce: TextView
    private lateinit var mProject: TextView
    private lateinit var mLibCard: CardView
    private lateinit var mIntroduceCard: CardView
    private lateinit var mProjectCard: CardView
    private lateinit var mLibRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_about, container, false)

        mScrollView = view.findViewById(R.id.nestedScrollView_about)
        mIntroduce = view.findViewById(R.id.textView_about_introduce)
        mProject = view.findViewById(R.id.textView_about_project)
        mLibCard = view.findViewById(R.id.cardView_about_lib)
        mIntroduceCard = view.findViewById(R.id.cardView_about_introduce)
        mProjectCard = view.findViewById(R.id.cardView_about_project)
        mLibRecyclerView = view.findViewById(R.id.recyclerView_fragment_about_lib)

        mIntroduce.typeface = TypefaceHelper.getTypeface(context, TypefaceHelper.Type.Book)
        mIntroduce.text = Html.fromHtml(
                "A simple client of Matthias Heiderich Photography, more information on <a href='http://www.matthias-heiderich.de'>www.matthias-heiderich.de</a>")
        mIntroduce.movementMethod = LinkMovementMethod.getInstance()

        mProject.typeface = TypefaceHelper.getTypeface(context, TypefaceHelper.Type.Book)
        mProject.text = Html.fromHtml(
                "This project address <a href='https://github.com/sorcererXW/MatthiasHeiderichPhotography'>Github</a>")
        mProject.movementMethod = LinkMovementMethod.getInstance()

        mLibRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                activity, RecyclerView.VERTICAL, false)
        mLibRecyclerView.adapter = LibAdapter(context, Arrays.asList(*LibraryBean.LIBRARY_BEEN))
        mLibRecyclerView.isNestedScrollingEnabled = false

        return view
    }

    override fun refreshUI() {
        if (mLibRecyclerView.adapter != null) {
            mLibRecyclerView.adapter!!.notifyDataSetChanged()
        }

//        mIntroduce.setLinkTextColor(ContextCompat
//                .getColor(context, ThemeHelper.getPrimaryTextColorRes(context)))
//        mIntroduce.setTextColor(ContextCompat
//                .getColor(context, ThemeHelper.getSecondaryTextColorRes(context)))
//
//        mProject.setLinkTextColor(ContextCompat
//                .getColor(context, ThemeHelper.getPrimaryTextColorRes(context)))
//        mProject.setTextColor(ContextCompat
//                .getColor(context, ThemeHelper.getSecondaryTextColorRes(context)))
//
//        mIntroduceCard.setCardBackgroundColor(
//                ContextCompat.getColor(context, ThemeHelper.getCardColorRes(context)))
//        mLibCard.setCardBackgroundColor(
//                ContextCompat.getColor(context, ThemeHelper.getCardColorRes(context)))
//        mProjectCard.setCardBackgroundColor(
//                ContextCompat.getColor(context, ThemeHelper.getCardColorRes(context)))
//
//        mProject.setTextColor(ContextCompat
//                .getColor(context, ThemeHelper.getSecondaryTextColorRes(context)))
//        mIntroduce.setTextColor(ContextCompat.getColor(context,
//                ThemeHelper.getSecondaryTextColorRes(context)))
    }

    override fun onToolbarDoubleTap() {
        super.onToolbarDoubleTap()
        mScrollView.smoothScrollTo(0, 0)
    }
}
