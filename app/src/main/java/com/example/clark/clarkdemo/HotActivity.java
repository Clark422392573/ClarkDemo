package com.example.clark.clarkdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alexvasilkov.android.commons.utils.Views;
import com.example.clark.clarkdemo.foldable.UnfoldableView;
import com.example.clark.clarkdemo.foldable.adapter.PaintingsAdapter;
import com.example.clark.clarkdemo.foldable.shading.GlanceFoldShading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class HotActivity extends AppCompatActivity implements PaintingsAdapter.HotOnClick {

    @InjectView(R.id.public_toolbar_return_back)
    ImageView publicToolbarReturnBack;
    private View listTouchInterceptor;
    private View detailsLayout;
    private UnfoldableView unfoldableView;
    private PaintingsAdapter mPaintingsAdapter;

    // 设置适配器的图片资源
    int[] imageId = new int[]{
            R.mipmap.starry_night,
            R.mipmap.cafe_terrace,
            R.mipmap.starry_night_over_the_rhone,
            R.mipmap.sunflowers,
            R.mipmap.almond_blossoms
    };

    // 设置标题
    String[] title = new String[]{
            "starry night",
            "cafe terrace",
            "starry night over the rhone",
            "sunflowers",
            "almond blossoms"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot);
        ButterKnife.inject(this);

        ListView listView = Views.find(this, R.id.hot_list_view);

        List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();

        // 将上述资源转化为list集合
        for (int i = 0; i < title.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", imageId[i]);
            map.put("title", title[i]);

            listitem.add(map);
        }
        mPaintingsAdapter = new PaintingsAdapter(this, listitem, this);
        listView.setAdapter(mPaintingsAdapter);

        listTouchInterceptor = Views.find(this, R.id.touch_interceptor_view);
        listTouchInterceptor.setClickable(false);

        detailsLayout = Views.find(this, R.id.details_layout);
        detailsLayout.setVisibility(View.INVISIBLE);

        unfoldableView = Views.find(this, R.id.unfoldable_view);

        Bitmap glance = BitmapFactory.decodeResource(getResources(), R.mipmap.unfold_glance);
        unfoldableView.setFoldShading(new GlanceFoldShading(glance));

        unfoldableView.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {
            @Override
            public void onUnfolding(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(true);
                detailsLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUnfolded(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(false);
            }

            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(true);
            }

            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(false);
                detailsLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (unfoldableView != null
                && (unfoldableView.isUnfolded() || unfoldableView.isUnfolding())) {
            unfoldableView.foldBack();
        } else {
            super.onBackPressed();
        }
    }

    public void openDetails(View coverView) {
        final ImageView image = Views.find(detailsLayout, R.id.details_image);
        final TextView title = Views.find(detailsLayout, R.id.details_title);
        final TextView description = Views.find(detailsLayout, R.id.details_text);
        image.setImageDrawable(getResources().getDrawable(R.mipmap.sunflowers));
        title.setText("sunflowers");
        description.setText("year : 1899");
        unfoldableView.unfold(coverView, detailsLayout);
    }

    @OnClick(R.id.public_toolbar_return_back)
    public void onClick() {
        finish();
    }

    @Override
    public void OnHotClick(View view) {
        if (this instanceof HotActivity) {
            this.openDetails(view);
        }
    }
}
