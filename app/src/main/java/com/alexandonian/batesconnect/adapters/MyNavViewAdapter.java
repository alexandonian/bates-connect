package com.alexandonian.batesconnect.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexandonian.batesconnect.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 7/23/2015.
 */
public class MyNavViewAdapter extends RecyclerView.Adapter<MyNavViewAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ClickListener mClickListener;
    private Context mContext;
    private View mSelectedView;
    List<NavInfo> mData = Collections.emptyList();

    public MyNavViewAdapter(Context context, List<NavInfo> data) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        NavInfo current = mData.get(position);
        viewHolder.title.setText(current.title);
        viewHolder.icon.setImageResource(current.iconId);
    }

    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.nav_list_title);
            icon = (ImageView) itemView.findViewById(R.id.nav_list_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.itemClicked(view, getAdapterPosition());
                view.setBackgroundColor(mContext.getResources().getColor(R.color.bates_accent));
            }

            if (mSelectedView != null) {
                mSelectedView.setBackgroundColor(Color.TRANSPARENT);
            }

            mSelectedView = view;


        }
    }

    public interface ClickListener {
        void itemClicked(View view, int position);
    }

    public static class NavInfo {
        public int iconId;
        public String title;
    }
}
