package com.example.skywalker.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.skywalker.popularmovies.R;
import com.example.skywalker.popularmovies.model.ReviewList;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    private ArrayList<ReviewList.Review> mReviews;
    private Context mContext;

    public ReviewAdapter(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_item,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return (mReviews != null ? mReviews.size() : 0);
    }

    public void setData(ArrayList<ReviewList.Review> reviews){
        mReviews = reviews;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.author) TextView tvAuthor;
        @BindView(R.id.review_content) TextView tvReview;

        @BindColor(R.color.colorPrimaryLight) int textColor;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(int position){
            tvAuthor.setText(mReviews.get(position).author);
            tvAuthor.setTextColor(textColor);
            tvReview.setText(mReviews.get(position).reviewContent);
            tvReview.setTextColor(textColor);

        }
    }
}
