package com.sorcererxw.matthiasheidericphotography.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.matthiasheidericphotography.models.LibraryBean;
import com.sorcererxw.matthiasheidericphotography.util.TypefaceHelper;

import org.w3c.dom.Text;

/**
 * Created by Sorcerer on 2016/8/25.
 */
public class LibraryListView extends LinearLayout {
    public LibraryListView(Context context) {
        super(context);
        init(context);
    }

    public LibraryListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LibraryListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LibraryListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {

    }


    public void addItem(LibraryBean bean) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_library, null);
        TextView name = (TextView) view.findViewById(R.id.textView_item_library_name);
        TextView author = (TextView) view.findViewById(R.id.textView_item_library_author);
        TextView licence = (TextView) view.findViewById(R.id.textView_item_library_licence);

        name.setTypeface(TypefaceHelper.getTypeface(getContext(), TypefaceHelper.Type.Book));
        author.setTypeface(TypefaceHelper.getTypeface(getContext(), TypefaceHelper.Type.Book));
        licence.setTypeface(TypefaceHelper.getTypeface(getContext(), TypefaceHelper.Type.Book));

        name.setText(bean.getName());
        author.setText(bean.getAuthor());
        licence.setText(bean.getLicense());

        addView(view);
    }


}
