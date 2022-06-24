package com.hao.trade;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.List;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import cn.leancloud.LCObject;

public class GoodsAdapter extends ArrayAdapter<LCObject>{
        private List<LCObject> mList;
        private Context mContext;
        private int resourceId;

        public GoodsAdapter(Context context,
                            int resourceId, List<LCObject> data) {
            super(context, resourceId, data);
            this.mContext = context;
            this.mList = data;
            this.resourceId = resourceId;
        }

        @Override
        public View getView(int position,
                            View convertView, ViewGroup parent) {
            View view;
            final ViewHolder vh;

            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
                vh = new ViewHolder(view);
                view.setTag(vh);
            } else {
                view = convertView;
                vh = (ViewHolder) view.getTag();
            }
            vh.mTitle.setText((CharSequence) mList.get(position).get("title"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            vh.mTime.setText(format.format(mList.get(position).getCreatedAt()));
            vh.mDescription.setText((CharSequence) mList.get(position).get("description"));
            boolean isSold = (boolean) mList.get(position).get("isSold");
            vh.mPrice.setText(isSold ? "已卖出" : "￥ " + mList.get(position).get("price"));
            vh.mName.setText(mList.get(position).getLCObject("owner") == null ? "" : mList.get(position).getLCObject("owner").getString("username"));
            vh.mView.setText(mList.get(position).get("view").toString());
            vh.mWant.setText(mList.get(position).get("want").toString()+"人想要");
            Glide.with(mContext)
                    .load(mList.get(position).get("imageUrl") == null ? null : mList.get(position).get("imageUrl"))
                    .into(vh.mPicture);
            vh.mItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("goodsObjectId", mList.get(position).getObjectId());
                    mContext.startActivity(intent);
                }
            });


            return view;
        }

    class ViewHolder {
        private TextView mName;
        private TextView mPrice;
        private TextView mDescription;
        private TextView mTitle;
        private CardView mItem;
        private ImageView mPicture;
        private TextView mTime;
        private TextView mView;
        private TextView mWant;

        public ViewHolder(View itemView) {
            mName = (TextView) itemView.findViewById(R.id.name_item_main);
            mTitle = (TextView) itemView.findViewById(R.id.title_item_main);
            mDescription = (TextView) itemView.findViewById(R.id.description_item_main);
            mPrice = (TextView) itemView.findViewById(R.id.price_item_main);
            mPicture = (ImageView) itemView.findViewById(R.id.picture_item_main);
            mItem = (CardView) itemView.findViewById(R.id.item_main);
            mTime = (TextView)  itemView.findViewById(R.id.tv_time);
            mView = (TextView) itemView.findViewById(R.id.tv_view);
            mWant = (TextView) itemView.findViewById(R.id.tv_want);
        }

    }
}
