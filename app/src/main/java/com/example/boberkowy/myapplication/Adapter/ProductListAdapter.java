package com.example.boberkowy.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.boberkowy.myapplication.Activity.AddProductActivity;
import com.example.boberkowy.myapplication.DAO.ProductLab;
import com.example.boberkowy.myapplication.Model.Product;
import com.example.boberkowy.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private List<Product> products = new ArrayList<>();
    private Context mContext;

    public ProductListAdapter(List<Product> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        viewHolder.productName.setText(products.get(i).getName());
        viewHolder.price.setText(String.valueOf(products.get(i).getPrice()));
        viewHolder.count.setText(String.valueOf(products.get(i).getCount()));
        viewHolder.hasBeenBought.setChecked(products.get(i).isPurchased());

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddProductActivity.class);
                intent.putExtra("product_id", String.valueOf(products.get(viewHolder.getAdapterPosition()).getId()));
                v.getContext().startActivity(intent);
            }
        });

        viewHolder.hasBeenBought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product toUpdate = products.get(viewHolder.getAdapterPosition());

                if(viewHolder.hasBeenBought.isChecked()){
                    toUpdate.setPurchased(true);
                    ProductLab.get(v.getContext()).updateProduct(toUpdate);
                }else {
                    toUpdate.setPurchased(false);
                    ProductLab.get(v.getContext()).updateProduct(toUpdate);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName;
        TextView count;
        TextView price;
        CheckBox hasBeenBought;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            count = itemView.findViewById(R.id.count);
            price = itemView.findViewById(R.id.price);
            hasBeenBought = itemView.findViewById(R.id.hasBeenBought);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }

}
