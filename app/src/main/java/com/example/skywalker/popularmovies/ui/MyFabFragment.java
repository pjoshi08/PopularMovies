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

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyFabFragment extends AAH_FabulousFragment {

    @BindView(R.id.filter_popular) TextView tvPopular;
    @BindView(R.id.filter_rating) TextView tvRated;
    @BindView(R.id.filter_favorites) TextView tvFavorites;

    @BindString(R.string.filter_popular) String filterPopular;
    @BindString(R.string.filter_top_rated) String filterRated;
    @BindString(R.string.filter_favorites) String filterFavorites;

    private String applied_filter;
    private Unbinder unbinder;

    public static MyFabFragment newInstance() {
        return new MyFabFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            applied_filter = ((MainActivity) getActivity()).getApplied_filter();
            if(applied_filter == null) applied_filter = filterPopular;
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.filter_sample_view, null);
        RelativeLayout rl_content = contentView.findViewById(R.id.rl_content);
        LinearLayout ll_buttons = contentView.findViewById(R.id.ll_buttons);
        contentView.findViewById(R.id.btn_close).setOnClickListener(v -> closeFilter("closed"));

        unbinder = ButterKnife.bind(this, contentView);

        setFilterBackground();

        tvPopular.setOnClickListener(v -> {
            applied_filter = filterPopular;
            filterHandle(v);
        });

        tvRated.setOnClickListener(v -> {
            applied_filter = filterRated;
            filterHandle(v);
        });

        tvFavorites.setOnClickListener(v -> {
            applied_filter = filterFavorites;
            filterHandle(v);
        });

        contentView.findViewById(R.id.check).setOnClickListener(v -> closeFilter(applied_filter));

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
                tvFavorites.setBackgroundResource(R.drawable.chip_unselected);
                break;
            case R.id.filter_rating:
                tvPopular.setBackgroundResource(R.drawable.chip_unselected);
                tvRated.setBackgroundResource(R.drawable.chip_selected);
                tvFavorites.setBackgroundResource(R.drawable.chip_unselected);
                break;
            case R.id.filter_favorites:
                tvPopular.setBackgroundResource(R.drawable.chip_unselected);
                tvRated.setBackgroundResource(R.drawable.chip_unselected);
                tvFavorites.setBackgroundResource(R.drawable.chip_selected);
                break;
        }
    }

    private void setFilterBackground(){
        if(filterPopular.equals(applied_filter))
            tvPopular.setBackgroundResource(R.drawable.chip_selected);
        else if(filterRated.equals(applied_filter))
            tvRated.setBackgroundResource(R.drawable.chip_selected);
        else if(filterFavorites.equals(applied_filter))
            tvFavorites.setBackgroundResource(R.drawable.chip_selected);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}