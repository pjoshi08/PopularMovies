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

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mYouTubeKeys;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int clickedItemIndex);
    }

    public VideoAdapter(Context context, OnItemClickListener onItemClickListener){
        mContext = context;
        mOnItemClickListener = onItemClickListener;
    }

    public void setKeys(ArrayList<String> youtubeKeys){
        mYouTubeKeys = youtubeKeys;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_item,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(mYouTubeKeys == null)
            return 0;
        return mYouTubeKeys.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.video_thumbnail) ImageView thumbnail;
        @BindDrawable(R.drawable.placeholder) Drawable placeholder;
        @BindDrawable(R.drawable.error) Drawable error;
        @BindString(R.string.uri_scheme) String scheme;
        @BindString(R.string.thumbnail_authority) String authority;
        @BindString(R.string.thumbnail_path1) String appendPath1;
        @BindString(R.string.thumbnail_path2) String appendPath2;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        void bind(int listIndex){
            loadImage(mYouTubeKeys.get(listIndex));
        }

        private Uri buildUri(String videoId){
            return new Uri.Builder()
                    .scheme(scheme)
                    .authority(authority)
                    .appendEncodedPath(appendPath1)
                    .appendEncodedPath(videoId)
                    .appendEncodedPath(appendPath2)
                    .build();
        }

        private void loadImage(String videoId){
            Picasso.get()
                    .load(buildUri(videoId))
                    .placeholder(placeholder)
                    .error(error)
                    .into(thumbnail);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnItemClickListener.onItemClick(clickedPosition);
        }
    }
}
