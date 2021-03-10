package com.example.tour;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public ViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View mView ) {
                mClickListener.onItemClick(mView , getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View mView ) {
                mClickListener.onItemLongClick(mView , getAdapterPosition());
                return true;
            }
        });
    }
    public void setDetails (Context ctx, String title, String description, String image, String cityName){
        TextView mTitleTv=  mView.findViewById(R.id.rTitleTv);
        TextView mDetailTv= mView.findViewById(R.id.rDescription);
        ImageView mImageTv= mView.findViewById(R.id.rImageView);
        TextView mCity= mView.findViewById(R.id.rCity);
        mTitleTv.setText(title);
        mDetailTv.setText(description);
        mCity.setText("Şehir:"+cityName);
        Picasso.get().load(image).into(mImageTv);
    }
    private ViewHolder.ClickListener mClickListener;
    public interface ClickListener{
        void  onItemClick(View view, int position);
        void  onItemLongClick(View view, int position);
    }
    public  void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
