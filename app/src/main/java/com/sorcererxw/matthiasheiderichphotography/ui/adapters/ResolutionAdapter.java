package com.sorcererxw.matthiasheiderichphotography.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sorcererxw.matthiasheiderichphotography.util.StringUtil;
import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;
import com.sorcererxw.matthiasheidericphotography.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sorcerer on 2016/8/26.
 */
public class ResolutionAdapter
        extends RecyclerView.Adapter<ResolutionAdapter.ResolutionViewHolder> {

    static class ResolutionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_item_resolution)
        TextView value;

        @BindView(R.id.radioButton_item_resolution)
        RadioButton radio;

        public ResolutionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private Context mContext;

    private int[] mData;

    private int mCurrentValue;

    public ResolutionAdapter(Context context, int[] data, int currentValue) {
        mContext = context;
        mData = data;
        mCurrentValue = currentValue;
    }

    @Override
    public ResolutionViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        return new ResolutionViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_resolution, parent, false));
    }

    @Override
    public void onBindViewHolder(final ResolutionViewHolder holder, int position) {
        holder.value.setTypeface(TypefaceHelper.getTypeface(mContext, TypefaceHelper.Type.Book));
        holder.value.setText(StringUtil.resolutionToString(mData[position]));
        holder.radio.setChecked(mData[position] == mCurrentValue);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentValue != mData[holder.getAdapterPosition()]) {
                    mCurrentValue = mData[holder.getAdapterPosition()];
                    ResolutionAdapter.this.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    public int getCurrentValue() {
        return mCurrentValue;
    }


}
