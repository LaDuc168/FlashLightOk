package com.example.minh.animenotification.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.minh.animenotification.R;
import com.example.minh.animenotification.model.MyIcon;

import java.util.ArrayList;

public class MyIconAdapter extends RecyclerView.Adapter<MyIconAdapter.ItemHolder> {
    private ArrayList<MyIcon> mIcons;
    private Context mContext;
    private IconListener mListener;
    public MyIconAdapter(Context context,IconListener listener) {
        mIcons = new ArrayList<>();
        mIcons.add(new MyIcon(R.drawable.i1, 1));
        mIcons.add(new MyIcon(R.drawable.i2, 1));
        mIcons.add(new MyIcon(R.drawable.i3, 2));
        mIcons.add(new MyIcon(R.drawable.i4, 2));
        mIcons.add(new MyIcon(R.drawable.i5, 2));
        mIcons.add(new MyIcon(R.drawable.i6, 2));
        mIcons.add(new MyIcon(R.drawable.i7, 2));
        mIcons.add(new MyIcon(R.drawable.i8, 2));
        mIcons.add(new MyIcon(R.drawable.i9, 2));
        mIcons.add(new MyIcon(R.drawable.i10, 2));
        mIcons.add(new MyIcon(R.drawable.i11, 2));
        mIcons.add(new MyIcon(R.drawable.i12, 2));
        mIcons.add(new MyIcon(R.drawable.i14, 2));
        mIcons.add(new MyIcon(R.drawable.i15, 2));
        mContext = context;
        mListener = listener;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icon, null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        final MyIcon icon = mIcons.get(position);
        Glide.with(mContext).asGif().load(icon.getmName()).into(holder.mImage);
//        holder.mImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ServiceIncomingCall.ICON_NAME = icon.getmName();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mIcons.size();
    }

    public interface IconListener{
         void chooseIcon(int icon);
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mImage;

        public ItemHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.image);
            mImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.chooseIcon(mIcons.get(getAdapterPosition()).getmName());
        }
    }
}