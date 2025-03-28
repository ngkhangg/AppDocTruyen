package com.example.appdoctruyen.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.appdoctruyen.Object.TruyenTranh;
import com.example.appdoctruyen.R;

import java.util.ArrayList;

public class TruyenTranhAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TruyenTranh> truyenList;
    private LayoutInflater inflater;

    public TruyenTranhAdapter(@NonNull Context context, ArrayList<TruyenTranh> truyenList) {
        this.context = context;
        this.truyenList = truyenList;
        this.inflater = LayoutInflater.from(context);
    }

    // Sort based on the title
    public void sortTenTruyen(String query) {
        query = query.toUpperCase(); // Case-insensitive search
        ArrayList<TruyenTranh> sortedList = new ArrayList<>();
        for (TruyenTranh truyen : truyenList) {
            if (truyen.getTenTruyen().toUpperCase().contains(query)) {
                sortedList.add(truyen);
            }
        }
        truyenList.clear();
        truyenList.addAll(sortedList);
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<TruyenTranh> newTruyenList) {
        if (newTruyenList != null) {
            this.truyenList.clear();
            this.truyenList.addAll(newTruyenList);
            notifyDataSetChanged();  // Thông báo dữ liệu đã thay đổi
        }
    }


    @Override
    public int getCount() {
        return truyenList.size();
    }

    @Override
    public Object getItem(int position) {
        return truyenList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // ViewHolder pattern for performance optimization
    static class ViewHolder {
        TextView tenTruyenTranh;
        TextView tenTruyenChap;
        ImageView imgAnhTruyen;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_truyen, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tenTruyenTranh = convertView.findViewById(R.id.txvTenTruyenTranh);
            viewHolder.tenTruyenChap = convertView.findViewById(R.id.txvTenChap);
            viewHolder.imgAnhTruyen = convertView.findViewById(R.id.imgAnhTruyen);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TruyenTranh truyenTranh = truyenList.get(position);

        viewHolder.tenTruyenTranh.setText(truyenTranh.getTenTruyen());
        viewHolder.tenTruyenChap.setText(truyenTranh.getTenChap());
        Glide.with(context).load(truyenTranh.getLinkAnh()).into(viewHolder.imgAnhTruyen);

        return convertView;
    }
}
