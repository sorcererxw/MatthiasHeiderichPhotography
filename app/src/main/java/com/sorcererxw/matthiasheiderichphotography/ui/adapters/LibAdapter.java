package com.sorcererxw.matthiasheiderichphotography.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sorcererxw.matthiasheiderichphotography.models.LibraryBean;
import com.sorcererxw.matthiasheiderichphotography.util.ResourceUtil;
import com.sorcererxw.matthiasheiderichphotography.util.ThemeHelper;
import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;
import com.sorcererxw.matthiasheidericphotography.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/23
 */

public class LibAdapter extends RecyclerView.Adapter<LibAdapter.LibViewHolder> {
    static class LibViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_item_library_name)
        TextView name;

        @BindView(R.id.textView_item_library_author)
        TextView author;

        @BindView(R.id.textView_item_library_licence)
        TextView licence;

        @BindView(R.id.frameLayout_item_library_container)
        FrameLayout licenceContainer;

        public LibViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Typeface typeface =
                    TypefaceHelper.getTypeface(itemView.getContext(), TypefaceHelper.Type.Book);
            name.setTypeface(typeface);
            author.setTypeface(typeface);
            licence.setTypeface(typeface);
        }
    }

    private Context mContext;

    private List<LibraryBean> mLibraryBeanList = new ArrayList<>();

    public LibAdapter(Context context, List<LibraryBean> libraryBeanList) {
        mContext = context;
        mLibraryBeanList = libraryBeanList;
    }

    @Override
    public LibViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LibViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_library, parent, false));
    }

    @Override
    public void onBindViewHolder(LibViewHolder holder, int position) {

        int colorPrimary = ResourceUtil.getColor(mContext, ThemeHelper.getPrimaryColorRes(mContext));
        int colorPrimaryText =
                ResourceUtil.getColor(mContext, ThemeHelper.getPrimaryTextColorRes(mContext));
        int colorSecondaryText =
                ResourceUtil.getColor(mContext, ThemeHelper.getSecondaryTextColorRes(mContext));
        int colorLibCopyrightBackground =
                ResourceUtil.getColor(mContext, ThemeHelper.getLibCopyrightBackgroundRes(mContext));

        holder.name.setTextColor(colorSecondaryText);
        holder.author.setTextColor(colorSecondaryText);
        holder.licence.setTextColor(colorSecondaryText);
        holder.licenceContainer.setBackgroundColor(colorLibCopyrightBackground);

        holder.name.setText(mLibraryBeanList.get(position).getName());
        holder.author.setText(mLibraryBeanList.get(position).getAuthor());
        holder.licence.setText(mLibraryBeanList.get(position).getLicense());
    }

    @Override
    public int getItemCount() {
        return mLibraryBeanList.size();
    }
}
