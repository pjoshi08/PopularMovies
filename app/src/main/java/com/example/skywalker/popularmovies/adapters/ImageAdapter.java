package com.example.skywalker.popularmovies.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.skywalker.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mImages;
    final private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int clickedItemIndex);
    }

    @Inject
    public ImageAdapter(Context context, OnItemClickListener listener) {
        mContext = context;
        mOnItemClickListener = listener;
    }

    public void setImages(ArrayList<String> images){
        mImages = images;
        notifyDataSetChanged();
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
        if (mImages == null) {
            return 0;
        }
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.movie_poster) ImageView moviePoster;
        @BindDrawable(R.drawable.placeholder) Drawable placeholder;
        @BindDrawable(R.drawable.error) Drawable error;
        @BindString(R.string.uri_scheme) String scheme;
        @BindString(R.string.poster_authority) String authority;
        @BindString(R.string.append_path_list) String appendEncodedPath;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            moviePoster.setAdjustViewBounds(true);

            itemView.setOnClickListener(this);
        }

        void bind(int listIndex){
            //Load image into GridView with the help of Picasso
            Picasso.get()
                    .load(buildUri(mImages.get(listIndex)))
                    .placeholder(placeholder)
                    .error(error)
                    .into(moviePoster);
        }

        private Uri buildUri(String path){
            return new Uri.Builder()
                    .scheme(scheme)
                    .authority(authority)
                    .appendEncodedPath(appendEncodedPath)
                    .appendEncodedPath(path)
                    .build();
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnItemClickListener.onItemClick(clickedPosition);
        }
    }
}
