package com.example.skywalker.popularmovies.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.example.skywalker.popularmovies.R;

public class MyFabFragment extends AAH_FabulousFragment {
    private String applied_filter;
    private TextView tvPopular, tvRated;

    public static MyFabFragment newInstance() {
        return new MyFabFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            applied_filter = ((MainActivity) getActivity()).getApplied_filter();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.filter_sample_view, null);
        RelativeLayout rl_content = contentView.findViewById(R.id.rl_content);
        LinearLayout ll_buttons = contentView.findViewById(R.id.ll_buttons);
        contentView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFilter("closed");
            }
        });

        tvPopular = contentView.findViewById(R.id.filter_popular);
        tvRated = contentView.findViewById(R.id.filter_rating);

        setFilterBackground(applied_filter);

        tvPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applied_filter = "popular";
                filterHandle(v);
            }
        });

        tvRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applied_filter = "top_rated";
                filterHandle(v);
            }
        });

        contentView.findViewById(R.id.check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFilter(applied_filter);
            }
        });

        //params to set
        setAnimationDuration(600); //optional; default 500ms
        setPeekHeight(300); // optional; default 400dp
        setCallbacks((Callbacks) getActivity()); //optional; to get back result
        setAnimationListener((AnimationListener) getActivity()); //optional; to get animation callbacks
        setViewgroupStatic(ll_buttons); // optional; layout to stick at bottom on slide
        setViewMain(rl_content); //necessary; main bottomsheet view
        setMainContentView(contentView); // necessary; call at end before super
        super.setupDialog(dialog, style); //call super at last
    }

    private void filterHandle(View v){
        switch (v.getId()){
            case R.id.filter_popular:
                tvPopular.setBackgroundResource(R.drawable.chip_selected);
                tvRated.setBackgroundResource(R.drawable.chip_unselected);
                break;
            case R.id.filter_rating:
                tvPopular.setBackgroundResource(R.drawable.chip_unselected);
                tvRated.setBackgroundResource(R.drawable.chip_selected);
                break;
        }
    }

    private void setFilterBackground(String s){
        switch (s){
            case "popular":
                tvPopular.setBackgroundResource(R.drawable.chip_selected);
                break;
            case "top_rated":
                tvRated.setBackgroundResource(R.drawable.chip_selected);
                break;
        }
    }
}