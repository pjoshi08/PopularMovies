package com.example.skywalker.popularmovies.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.skywalker.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mImages;
    final private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int clickedItemIndex);
    }

    public ImageAdapter(Context context, ArrayList<String> images, OnItemClickListener listener) {
        mContext = context;
        mImages = images;
        mOnItemClickListener = listener;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final String posterBaseURL500w = mContext.getResources()
                .getString(R.string.poster_base_url_500w);
        ImageView moviePoster;

        ViewHolder(View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movie_poster);
            moviePoster.setAdjustViewBounds(true);

            itemView.setOnClickListener(this);
        }

        void bind(int listIndex){
            //Load image into GridView with the help of Picasso
            Picasso.get()
                    .load(posterBaseURL500w + mImages.get(listIndex))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(moviePoster);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnItemClickListener.onItemClick(clickedPosition);
        }
    }
}
