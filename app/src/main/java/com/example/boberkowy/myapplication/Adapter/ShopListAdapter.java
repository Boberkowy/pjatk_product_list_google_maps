package com.example.boberkowy.myapplication.Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.boberkowy.myapplication.Activity.AddShopActivity;
import com.example.boberkowy.myapplication.DAO.Shop;
import com.example.boberkowy.myapplication.R;

import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ViewHolder> {
    private static final String TAG = "ShopListAdapter";

    private List<Shop> shops;

    public ShopListAdapter(List<Shop> shops) {
        this.shops = shops;
    }

    @NonNull
    @Override
    public ShopListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_list_item, viewGroup, false);

        return new ShopListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShopListAdapter.ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        viewHolder.shopName.setText(shops.get(i).getName());
        viewHolder.description.setText(shops.get(i).getDescription());
        viewHolder.latitude.setText(shops.get(i).getLatitude());
        viewHolder.longitude.setText(shops.get(i).getLongitude());
        viewHolder.radius.setText(shops.get(i).getRadius());

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddShopActivity.class);
                intent.putExtra("shop_id", shops.get(viewHolder.getAdapterPosition()).getId());
                intent.putExtra("shop_name", shops.get(viewHolder.getAdapterPosition()).getName());
                intent.putExtra("shop_desc", shops.get(viewHolder.getAdapterPosition()).getDescription());
                intent.putExtra("shop_latitude", shops.get(viewHolder.getAdapterPosition()).getLatitude());
                intent.putExtra("shop_longitude", shops.get(viewHolder.getAdapterPosition()).getLongitude());
                intent.putExtra("shop_radius", shops.get(viewHolder.getAdapterPosition()).getRadius());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView shopName;
        TextView description;
        TextView radius;
        TextView latitude;
        TextView longitude;

        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.shop_name);
            description = itemView.findViewById(R.id.desc);
            radius = itemView.findViewById(R.id.radius);
            latitude = itemView.findViewById(R.id.latitude);
            longitude = itemView.findViewById(R.id.longitude);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
