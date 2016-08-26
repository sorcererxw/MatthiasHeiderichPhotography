package com.sorcererxw.matthiasheiderichphotography.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sorcererxw.matthiasheiderichphotography.models.SettingsBean;
import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;
import com.sorcererxw.matthiasheidericphotography.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sorcerer on 2016/8/26.
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder> {

    static class SettingsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_item_settings_divider)
        View divider;

        @BindView(R.id.textView_item_settings_label)
        TextView label;

        @BindView(R.id.textView_item_settings_value)
        TextView value;

        public SettingsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private Context mContext;
    private List<SettingsBean> mList;

    public SettingsAdapter(Context context, List<SettingsBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SettingsViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_settings, parent, false));
    }

    @Override
    public void onBindViewHolder(final SettingsViewHolder holder, int position) {
        holder.label.setTypeface(TypefaceHelper.getTypeface(mContext, TypefaceHelper.Type.Book));
        holder.value.setTypeface(TypefaceHelper.getTypeface(mContext, TypefaceHelper.Type.Book));

        if (position == getItemCount() - 1) {
            holder.divider.setVisibility(View.GONE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
        }
        holder.label.setText(mList.get(position).getLabel());
        holder.value.setText(mList.get(position).getValue());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mList.get(holder.getAdapterPosition()).getCallback() != null) {
                    mList.get(holder.getAdapterPosition()).getCallback()
                            .call(mList.get(holder.getAdapterPosition()),
                                    new SettingsBean.SettingCallbackCallback() {
                                        @Override
                                        public void call(SettingsBean item) {
                                            mList.set(holder.getAdapterPosition(), item);
                                            SettingsAdapter.this
                                                    .notifyItemChanged(holder.getAdapterPosition());
                                        }
                                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
