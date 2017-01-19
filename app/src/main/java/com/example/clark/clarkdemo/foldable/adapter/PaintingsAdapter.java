package com.example.clark.clarkdemo.foldable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.clark.clarkdemo.HotActivity;
import com.example.clark.clarkdemo.R;

import java.util.List;
import java.util.Map;

public class PaintingsAdapter extends BaseAdapter {

    private Context mContext;
    private List<Map<String, Object>> datas;

    public PaintingsAdapter(Context context, List<Map<String, Object>> data){
        this.mContext = context;
        this.datas = data;
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return datas != null ? datas.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        Map<String, Object> map = datas.get(position);
        mViewHolder.image.setImageResource((Integer) map.get("image"));
        mViewHolder.title.setText(map.get("title") + "");
        mViewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getContext() instanceof HotActivity) {
                    ((HotActivity) view.getContext()).openDetails(view);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView image;
        TextView title;

        public ViewHolder(View view){
            image = (ImageView) view.findViewById(R.id.list_item_image);
            title = (TextView) view.findViewById(R.id.list_item_title);
        }
    }

}
